package sg.darren.unittest._02_spring_with_sqldb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StudentRepositoryTest {

    private static final File DATA_JSON = Paths.get("src", "test", "resources", "_02_students.json").toFile();

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() throws IOException {
        Student[] students = new ObjectMapper().readValue(DATA_JSON, Student[].class);
        Arrays.stream(students).forEach(studentRepository::save);
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("Test student not found with non-existing id")
    void testStudentNotFoundForNonExistingId() {
        Student student = studentRepository.findById(100).orElse(null);

        Assertions.assertNull(student);
    }

    @Test
    @DisplayName("Test student saved successfully")
    void testStudentSavedSuccessfully() {
        Student newStudent = new Student(null, "Linda", "Lee", null);

        Student savedStudent = studentRepository.save(newStudent);

        Assertions.assertNotNull(savedStudent);
        Assertions.assertNotNull(savedStudent.getId(), "Student should have an id when saved");
        Assertions.assertEquals(newStudent.getFirstName(), savedStudent.getFirstName());
        Assertions.assertEquals(newStudent.getLastName(), savedStudent.getLastName());
    }

    @Test
    @DisplayName("Test student updated successfully")
    void testStudentUpdatedSuccessfully() {
        Student studentToUpdate = new Student(1, "Linda", "Lee Updated", 2);

        Student updatedStudent = studentRepository.save(studentToUpdate);

        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals(studentToUpdate.getFirstName(), updatedStudent.getFirstName());
        Assertions.assertEquals(studentToUpdate.getLastName(), updatedStudent.getLastName());
        Assertions.assertEquals(studentToUpdate.getVersion(), updatedStudent.getVersion());
    }

    @Test
    @DisplayName("Test student deleted successfully")
    void testStudentDeletedSuccessfully() {
        Student firstStudent = studentRepository.findAll().get(0);
        studentRepository.deleteById(firstStudent.getId());

        Assertions.assertEquals(1, studentRepository.count());
    }

}