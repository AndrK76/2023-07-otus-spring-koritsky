package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.config.security.RoleConfig;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private static final String MODIFY_BOOK = "modify_book";

    private final RoleConfig roleConfig;



    ///api/v1/info/access?action=modify_book
    @GetMapping("/api/v1/info/access")
    public boolean getAccess(@RequestParam(name = "action") String canAction, Authentication auth) {
        var roles = auth.getAuthorities().stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .map(role->role.replace("ROLE_",""))
                .collect(Collectors.toSet());
        return roleConfig.getAppRoles().containsKey(canAction)
                && roles.contains(roleConfig.getAppRoles().get(canAction));
    }
}
