package ru.otus.andrk.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.security.RoleConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleConfig roleConfig;

    @Override
    public Set<String> getAuthRoles(Authentication auth) {
        return auth.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> getRolesForAction(String action) {
        return roleConfig.getAppRoles().containsKey(action)
                ? roleConfig.getAppRoles().get(action)
                : List.of();
    }

    @Override
    public boolean isShowUnavailableModes() {
        return roleConfig.isShowUnavailable();
    }
}
