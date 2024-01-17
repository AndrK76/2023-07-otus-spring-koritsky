package ru.otus.andrk.config.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String REALM_ACCESS_CLAIM_NAME = "realm_access";

    private static final String RESOURCE_ACCESS_CLAIM_NAME = "resource_access";

    private static final String ROLE_LIST_CLAIM_NAME = "roles";


    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter
            = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;

    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractRolesFromKeyCloakRealms(jwt,
                                List.of(
                                        List.of(REALM_ACCESS_CLAIM_NAME),
                                        List.of(RESOURCE_ACCESS_CLAIM_NAME, properties.getResourceId())
                                )).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    Collection<? extends GrantedAuthority> extractRolesFromKeyCloakRealms(Jwt jwt, List<List<String>> pathToRoleClaims) {
        List<String> roleNames = new ArrayList<>();
        var allClaims = jwt.getClaims();
        for (var path : pathToRoleClaims) {
            var roleClaim = getClaimByPath(allClaims, path);
            if (roleClaim.containsKey(ROLE_LIST_CLAIM_NAME)) {
                roleNames.addAll((Collection<String>) roleClaim.get(ROLE_LIST_CLAIM_NAME));
            }
        }
        return roleNames.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    Map<String, Object> getClaimByPath(Map<String, Object> claims, List<String> path) {
        if (!path.isEmpty()) {
            var subClaims = (Map<String, Object>) claims.get(path.get(0));
            if (subClaims != null && subClaims.size() > 0) {
                return path.size() == 1 ? subClaims : getClaimByPath(subClaims, path.subList(1, path.size()));
            } else {
                return new HashMap<>();
            }
        } else {
            return claims;
        }
    }

}
