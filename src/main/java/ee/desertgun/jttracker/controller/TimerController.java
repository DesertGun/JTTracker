package ee.desertgun.jttracker.controller;


import ee.desertgun.jttracker.domain.TrackedTime;
import ee.desertgun.jttracker.dto.TrackedTimeDTO;
import ee.desertgun.jttracker.service.TrackedTimeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(path = "/api")
@PreAuthorize("hasRole('ROLE_USER')")
public class TimerController {


    private final TrackedTimeService trackedTimeService;

    public TimerController(TrackedTimeService trackedTimeService) {
        this.trackedTimeService = trackedTimeService;
    }

    @GetMapping("/timer")
    public List<TrackedTime> getTimesOfUser(Authentication authentication) {
        return trackedTimeService.getTimesForUser(authentication.getName());
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
    public void addTimes(@RequestBody @Valid TrackedTimeDTO trackedTimeDTO, Authentication authentication) {
        trackedTimeService.createTime(trackedTimeDTO, authentication.getName());
    }
}
