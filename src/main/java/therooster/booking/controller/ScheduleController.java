package therooster.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import therooster.booking.dto.request.CreateScheduleDTO;
import therooster.booking.entity.Schedule;
import therooster.booking.service.Impl.ScheduleServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleServiceImpl scheduleService;

    @PreAuthorize("hasAnyAuthority('READ_SCHEDULE')")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable UUID scheduleId) {
        Schedule schedule = this.scheduleService.lireSchedules(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_SCHEDULE')")
    @GetMapping()
    public ResponseEntity<List<Schedule>> listSchedules() {
        List<Schedule> schedules = this.scheduleService.listSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_SCHEDULE')")
    @PostMapping()
    public ResponseEntity<Void> createSchedule(@RequestBody CreateScheduleDTO createScheduleDTO,
                                               UriComponentsBuilder ucb) {

        Schedule schedule = this.scheduleService.createSchedule(createScheduleDTO);
        String scheduleId = schedule.getId().toString();
        URI scheduleUrl = ucb
                .path("/api/schedules/{scheduleId}")
                .buildAndExpand(scheduleId)
                .toUri();
        return ResponseEntity.created(scheduleUrl).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    @PutMapping()
    public ResponseEntity<Schedule> updateSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(this.scheduleService.updateSchedule(schedule));
    }

    @PreAuthorize("hasAnyRole('ROLE_AMDIN', 'ROLE_EMPLOYEE')")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID scheduleId) {
        this.scheduleService.deleteScheduleById(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
