package ru.otus.andrk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.andrk.model.DeviceTrouble;
import ru.otus.andrk.model.Ticket;
import ru.otus.andrk.model.TroubleLevel;
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
    public MessageChannelSpec<?, ?> criticalTicketChannel() {
        //return MessageChannels.direct();
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> otherTicketChannel() {
        //return MessageChannels.direct();
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(Duration.ofMillis(1000L)).maxMessagesPerPoll(50L);
    }

    @Bean
    public IntegrationFlow regTroubleFlow(RegistrationService regService) {
        return IntegrationFlow.from(troubleSourceChannel())
                .split()
                .handle(regService,"registerTrouble")
                .<Ticket, TroubleLevel>route(
                        Ticket::getLevel,
                        mapping ->
                                mapping
                                        .subFlowMapping(HIGH, ticket -> ticket.channel(criticalTicketChannel()))
                                        .subFlowMapping(MEDIUM, ticket -> ticket.channel(otherTicketChannel()))
                                        .subFlowMapping(LOWER, ticket -> ticket.channel(otherTicketChannel()))
                )
                .get();
    }

}
