package ru.otus.andrk.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.andrk.model.DeviceTrouble;

import java.util.Collection;

@MessagingGateway
public interface RegistrationTroubleGateway {
    @Gateway(requestChannel = "troubleSourceChannel")
    void process(Collection<DeviceTrouble> troubles);
}
