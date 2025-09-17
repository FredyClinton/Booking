package therooster.booking.service;

import therooster.booking.dto.request.CreateScheduleDTO;
import therooster.booking.entity.Schedule;

import java.util.List;
import java.util.UUID;

public interface ScheduleService {
    Schedule createSchedule(CreateScheduleDTO createScheduleDTO);

    List<Schedule> listSchedules();

    Schedule lireSchedules(UUID scheduleId);

    void deleteScheduleById(UUID id);

    Schedule updateSchedule(Schedule schedule);
}
