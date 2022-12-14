package sg.darren.unittest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.darren.unittest.model.Employee;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    public Employee getEmployee(@RequestParam Long id) {
        return Employee.builder()
                .id(id)
                .firstName("Spring-boot")
                .lastName("Unit Test")
                .email("spring-boot.unit.test@.mail.com")
                .build();
    }
}
