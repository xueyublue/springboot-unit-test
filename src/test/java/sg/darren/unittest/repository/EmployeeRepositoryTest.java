package sg.darren.unittest.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sg.darren.unittest.model.Employee;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given
        Employee e = Employee.builder()
                .firstName("Darren")
                .lastName("Xue")
                .email("darrenxy@gmail.com")
                .build();

        // when
        Employee savedE = employeeRepository.save(e);

        // then
        Assertions.assertThat(savedE).isNotNull();
        Assertions.assertThat(savedE.getId()).isGreaterThan(0);
    }

}