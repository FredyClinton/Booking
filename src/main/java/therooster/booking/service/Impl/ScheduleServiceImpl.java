package therooster.booking.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import therooster.booking.config.security.SecurityUtils;
import therooster.booking.dto.request.CreateScheduleDTO;
import therooster.booking.entity.Employee;
import therooster.booking.entity.Schedule;
import therooster.booking.entity.UserEntity;
import therooster.booking.repository.EmployeeRepository;
import therooster.booking.repository.ScheduleRepository;
import therooster.booking.service.ScheduleService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Schedule createSchedule(CreateScheduleDTO createScheduleDTO) {
        Employee nullEmployee = new Employee(); // TODO : Get current employee

        UserEntity currentUser = SecurityUtils.getCurrentUser();
        Schedule schedule = new Schedule();

        schedule.setDetails(createScheduleDTO.getDetails());
        schedule.setEndTime(createScheduleDTO.getEndTime());
        schedule.setStartTime(createScheduleDTO.getStartTime());

        System.out.println("Affectation du user");
        if (currentUser instanceof Employee) {
            System.out.println("Affectation du courrent");
            schedule.setEmployee((Employee) currentUser);
        } else if (SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            System.out.println("Affectation du null");
            schedule.setEmployee(nullEmployee);
            System.out.println("Sauvegarde");
        } else {
            throw new RuntimeException("Unauthorized");
        }
        return this.scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> listSchedules() {
        UserEntity user = SecurityUtils.getCurrentUser();

        if (SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            return scheduleRepository.findAll();
        } else if (SecurityUtils.currentUserHasRole("ROLE_EMPLOYEE")) {
            assert user != null;
            return scheduleRepository.findByEmployee_Email(user.getEmail());
        } else {
            throw new RuntimeException("Non authorisé");
        }


    }

    @Override
    public Schedule lireSchedules(UUID scheduleId) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        Schedule savedSchedule = this.scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule not found"));

        if ((currentUsername != null && currentUsername.equals(savedSchedule.getEmployee().getEmail()))
                || SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            return savedSchedule;
        } else {
            throw new RuntimeException("Unauthorized Schedule not found");
        }


    }

    @Override
    public void deleteScheduleById(UUID scheduleId) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Schedule savedSchedule = this.scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule not found"));

        if ((currentUsername != null && currentUsername.equals(savedSchedule.getEmployee().getEmail()))
                || SecurityUtils.currentUserHasRole("ROLE_ADMIN"))
            this.scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        Schedule savedSchedule = this.scheduleRepository.findById(schedule.getId()).orElseThrow(
                () -> new EntityNotFoundException("Schedule not found"));

        if (!(currentUsername != null && currentUsername.equals(savedSchedule.getEmployee().getEmail()))
                || !SecurityUtils.currentUserHasRole("ROLE_ADMIN")) {
            throw new RuntimeException("Unauthorized Schedule not found");
        }

        if (schedule.getDetails() != null) {
            savedSchedule.setDetails(schedule.getDetails());
        }
        if (schedule.getStartTime() != null) {
            savedSchedule.setStartTime(schedule.getStartTime());
        }
        if (schedule.getEndTime() != null) {
            savedSchedule.setEndTime(schedule.getEndTime());
        }
        return this.scheduleRepository.save(savedSchedule);
    }
}
