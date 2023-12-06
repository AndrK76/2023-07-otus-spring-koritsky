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


    @GetMapping("/api/v1/info/access")
    public boolean getAccess(@RequestParam(name = "action") String canAction, Authentication auth) {
        if (roleConfig.isShowUnavailable()) {
            return true;
        } else {
            var roles = roleConfig.getAuthRoles(auth);
            return roleConfig.getRolesForAction(canAction).stream().anyMatch(roles::contains);
        }

    }
}
