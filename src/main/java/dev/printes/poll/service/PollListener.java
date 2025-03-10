package dev.printes.poll.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollListener {

    private final PollService service;

    @RabbitListener(queues = "${poll.session.result.queue}")
    public void calculateSessionsResult(@Payload(required = true) List<Long> sessionIds) {
        log.info("Receive message: {}", sessionIds);
        for (Long id : sessionIds) {
            var session = service.findPollSessionWithVoting(id);
            service.updateSessionResult(session.getId(), session.getResult());
        }
    }

}
