package ru.otus.andrk.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.role-config", ignoreInvalidFields = true)
public class RoleConfig {

    private boolean showUnavailable;

    private Map<String, List<String>> appRoles;

}
