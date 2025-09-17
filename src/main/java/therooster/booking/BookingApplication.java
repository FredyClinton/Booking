package therooster.booking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import therooster.booking.entity.Admin;
import therooster.booking.entity.Employee;
import therooster.booking.entity.Role;
import therooster.booking.enums.TypeDeRole;
import therooster.booking.repository.RoleRepository;
import therooster.booking.repository.UsersRepository;

import java.util.Date;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdminUser(UsersRepository usersRepository,
                                           RoleRepository roleRepository,
                                           BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Ensure ADMIN role exists
            Role adminRole = roleRepository.findByLibelle(TypeDeRole.ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().libelle(TypeDeRole.ADMIN).build()));

            // Create default admin if not exists
            String adminEmail = "admin@local";
            if (!usersRepository.existsByEmail(adminEmail)) {
                Admin admin = new Admin();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhone("0000000000");
                admin.setBirthDate(new Date());
                admin.setRole(adminRole);
                // activate admin account
                // In UserEntity, 'actif' gates account flags; set true
                admin.setActif(true);
                usersRepository.save(admin);
            }
        };
    }


    @Bean
    public CommandLineRunner initEmployeeUser(UsersRepository usersRepository,
                                              RoleRepository roleRepository,
                                              BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            // Ensure ADMIN role exists
            Role employeeRole = roleRepository.findByLibelle(TypeDeRole.EMPLOYEE)
                    .orElseGet(() -> roleRepository.save(Role.builder().libelle(TypeDeRole.EMPLOYEE).build()));

            // Create default employee if not exists
            String employeeEmail = "employee@local";
            if (!usersRepository.existsByEmail(employeeEmail)) {
                Employee employee = new Employee();
                employee.setFirstName("Local");
                employee.setLastName("Employee");
                employee.setEmail(employeeEmail);
                employee.setPassword(passwordEncoder.encode("employee123"));
                employee.setPhone("0000000000");
                employee.setBirthDate(new Date());
                employee.setRole(employeeRole);
                // activate admin account
                // In UserEntity, 'actif' gates account flags; set true
                employee.setActif(true);
                usersRepository.save(employee);
            }
        };
    }
}
