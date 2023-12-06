package ru.otus.andrk.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.role-config", ignoreInvalidFields = true)
public class RoleConfig {

    private boolean showUnavailable;

    private Map<String, List<String>> appRoles;

    public Set<String> getAuthRoles(Authentication auth) {
        return auth.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }

    public List<String> getRolesForAction(String action) {
        return appRoles.containsKey(action)
                ? appRoles.get(action)
                : List.of();
    }
}
