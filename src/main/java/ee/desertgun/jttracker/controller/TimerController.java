package ee.desertgun.jttracker.controller;


import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;
import ee.desertgun.jttracker.service.timer.TrackedTimeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class TimerController {


    private final TrackedTimeService trackedTimeService;

    public TimerController(TrackedTimeService trackedTimeService) {
        this.trackedTimeService = trackedTimeService;
    }

    @GetMapping("/timer")
    public List<TrackedTime> getTimesOfUser(Principal principal) {
        return trackedTimeService.getTimesForUser(principal.getName());
    }

    @GetMapping("/timer/{timeID}")
    public TrackedTime getTimeOfUser(@PathVariable UUID timeID) {
        return trackedTimeService.getTimeByID(timeID);
    }

    @DeleteMapping("/timer/{timeID}")
    public void deleteTimer(@PathVariable UUID timeID) {
        trackedTimeService.deleteTimeByID(timeID);
    }

    @PutMapping("/timer/{timeID}")
    public void updateTimeDesc(@PathVariable UUID timeID, @RequestBody @Valid TrackedTimeDTO timer) {
        trackedTimeService.updateTime(timeID, timer);
    }

    @PostMapping("/timer")
    public void addTimes(@RequestBody @Valid TrackedTimeDTO trackedTimeDTO, Principal principal) {
        trackedTimeService.createTime(trackedTimeDTO, principal.getName());
    }
}
