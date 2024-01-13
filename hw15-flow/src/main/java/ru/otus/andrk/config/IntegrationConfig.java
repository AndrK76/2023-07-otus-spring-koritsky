package ru.otus.andrk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.andrk.model.Ticket;
import ru.otus.andrk.model.TicketStatus;
import ru.otus.andrk.model.TroubleLevel;
import ru.otus.andrk.service.AlertService;
import ru.otus.andrk.service.ProcessTicketService;
import ru.otus.andrk.service.RegistrationService;

import java.time.Duration;

import static ru.otus.andrk.model.TroubleLevel.HIGH;
import static ru.otus.andrk.model.TroubleLevel.LOWER;
import static ru.otus.andrk.model.TroubleLevel.MEDIUM;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> troubleSourceChannel() {
        return MessageChannels.queue(200);
    }

    @Bean
    public MessageChannelSpec<?, ?> alertTicketChannel() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> processTicketChannel() {
        return MessageChannels.queue();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(Duration.ofMillis(1000L)).maxMessagesPerPoll(50L);
    }

    @Bean
    public IntegrationFlow regTroubleFlow(RegistrationService regService) {
        return IntegrationFlow.from(troubleSourceChannel())
                .split()
                .handle(regService, "registerTrouble")
                .<Ticket, TroubleLevel>route(
                        Ticket::getLevel,
                        mapping ->
                                mapping
                                        .subFlowMapping(HIGH, ticket -> ticket.channel(alertTicketChannel()))
                                        .subFlowMapping(MEDIUM, ticket -> ticket.channel(processTicketChannel()))
                                        .subFlowMapping(LOWER, ticket -> ticket.channel(processTicketChannel()))
                )
                .get();
    }

    @Bean
    public IntegrationFlow criticalTicketFlow(AlertService alertService) {
        return IntegrationFlow.from(alertTicketChannel())
                .handle(alertService, "sendRegCriticalEventAlert")
                .channel(processTicketChannel())
                .get();
    }

    @Bean
    public IntegrationFlow processTicketFlow(
            ProcessTicketService processService,
            AlertService alertService) {
        return IntegrationFlow.from(processTicketChannel())
                .handle(processService, "handle")
                .<Ticket>filter(ticket -> ticket.getStatus() != TicketStatus.DECIDED)
                .handle(alertService, "sendNoDecideEventAlert")
                .channel(processTicketChannel())
                .get();
    }
}
