package therooster.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import therooster.booking.entity.Employee;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
