package sg.darren.unittest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.darren.unittest.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
