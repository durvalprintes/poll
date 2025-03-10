package dev.printes.poll.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollSchedule {

    private final PollFacade pollFacade;

    @Scheduled(cron = "${poll.session.result.cron}")
    public void calculateSessionsResult() {
        log.info( "Scheduling the calculation of results.");
        pollFacade.findSessionsToCalculateResult();
    }

}
