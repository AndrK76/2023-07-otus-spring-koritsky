package ru.otus.andrk.service.auth;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Set<String> getAuthRoles(Authentication auth);

    List<String> getRolesForAction(String action);

    boolean isShowUnavailableModes();
}
