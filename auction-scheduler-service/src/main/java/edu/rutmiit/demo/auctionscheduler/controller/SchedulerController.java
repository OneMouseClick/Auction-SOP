package edu.rutmiit.demo.auctionscheduler.controller;

import edu.rutmiit.demo.auctionscheduler.storage.LotTracker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final LotTracker lotTracker;

    public SchedulerController(LotTracker lotTracker) {
        this.lotTracker = lotTracker;
    }

    @GetMapping
    public Map<String, Object> getStatus() {
        return Map.of(
                "service", "auction-scheduler",
                "activeLots", lotTracker.activeCount(),
                "trackedLots", lotTracker.getAll().stream()
                        .map(l -> Map.of(
                                "lotId", l.lotId(),
                                "title", l.title(),
                                "endTime", l.endTime().toString()
                        ))
                        .toList()
        );
    }
}
