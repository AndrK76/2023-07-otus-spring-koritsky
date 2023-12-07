package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.service.auth.RoleService;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final RoleService roleService;

    @GetMapping("/api/v1/info/access")
    public boolean getAccess(@RequestParam(name = "action") String canAction, Authentication auth) {
        if (roleService.isShowUnavailableModes()) {
            return true;
        } else {
            var roles = roleService.getAuthRoles(auth);
            return roleService.getRolesForAction(canAction).stream().anyMatch(roles::contains);
        }

    }
}
