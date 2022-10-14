package sg.darren.unittest._02_spring_with_sqldb;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @DisplayName("Find student with id successfully")
    void testFindStudentById() {
        Optional<Student> mockOptionalStudent = Optional.of(
                new Student(1, "Linda", "Lee", 1)
        );

        doReturn(mockOptionalStudent).when(studentRepository).findById(1);

        Student foundStudent = studentService.findById(1);

        assertNotNull(foundStudent);
        assertSame("Linda", foundStudent.getFirstName());
        assertSame(1, foundStudent.getVersion());
    }

    @Test
    @DisplayName("Fail to find student by id")
    void testFailToFindStudentById() {
        Optional<Student> mockOptionalStudent = Optional.empty();

        doReturn(mockOptionalStudent).when(studentRepository).findById(1);

        Student foundStudent = studentService.findById(1);

        assertNull(foundStudent);
    }

    @Test
    @DisplayName("Find all students")
    void testFindAllStudents() {
        Student mockStudent = new Student(1, "Linda", "Lee", 1);
        Student mockStudent2 = new Student(2, "Joey", "Tan", 1);

        doReturn(Arrays.asList(mockStudent, mockStudent2)).when(studentRepository).findAll();

        List<Student> foundStudents = studentService.findAll();

        assertEquals(2, foundStudents.size());
        assertSame("Linda", foundStudents.get(0).getFirstName());
        assertSame("Joey", foundStudents.get(1).getFirstName());
    }

    @Test
    @DisplayName("Save new student successfully")
    void testSaveStudentSuccessfully() {
        Student mockStudent = new Student(1, "Linda", "Lee", 1);

        doReturn(mockStudent).when(studentRepository).save(any());

        Student savedStudent = studentService.save(mockStudent);

        AssertionErrors.assertNotNull("Student should not be null", savedStudent);
        assertSame(1, savedStudent.getId());
        assertSame("Linda", savedStudent.getFirstName());
    }

    @Test
    @DisplayName("Update an existing student successfully")
    void testUpdatingStudentSuccessfully() {
        Optional<Student> existingOptionalStudent = Optional.of(
                new Student(1, "Linda", "Lee", 1)
        );
        Student updatedStudent = new Student(1, "Joey", "Lee", 2);

        doReturn(existingOptionalStudent).when(studentRepository).findById(1);
        doReturn(updatedStudent).when(studentRepository).save(any());

        Student savedStudent = studentService.update(existingOptionalStudent.get());

        assertSame("Joey", savedStudent.getFirstName());
        assertSame(2, savedStudent.getVersion());
    }

    @Test
    @DisplayName("Fail to update an non-existing student")
    void testFailToUpdateExistingStudent() {
        Optional<Student> nullStudent = Optional.empty();
        Student mockStudent = new Student(1, "Linda", "Lee", 1);

        doReturn(nullStudent).when(studentRepository).findById(1);

        Student savedStudent = studentService.update(mockStudent);

        AssertionErrors.assertNull("Student should be null", savedStudent);
    }
}