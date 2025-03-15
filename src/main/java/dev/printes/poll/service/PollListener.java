package dev.printes.poll.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import dev.printes.poll.model.dto.PollMessageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollListener {

    private final PollService service;

    @RabbitListener(queues = "${poll.session.result.queue}")
    public void calculateSessionResult(@Payload(required = true) @Valid PollMessageDTO message) {
        log.info("Received message: {}", message);

        var session = service.findPollSessionWithVoting(message.sessionId());
        service.updateSessionResult(session.getId(), session.getResult(), message.closedBy());

        log.info("Result updated for session: {}", message.sessionId());
    }

}
