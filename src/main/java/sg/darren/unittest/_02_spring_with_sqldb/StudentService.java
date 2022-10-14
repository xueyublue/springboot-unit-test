package sg.darren.unittest._02_spring_with_sqldb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        log.info("Finding all products.");
        return studentRepository.findAll();
    }

    public Student findById(Integer id) {
        log.info("Finding product with id:{}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student save(Student student) {
        log.info("Saving product with id:{}", student.getId());
        student.setVersion(1);
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        log.info("Updating product with id:{}", student.getId());
        Student existingStudent = studentRepository.findById(student.getId()).orElse(null);
        if (existingStudent != null) {
            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setVersion(student.getVersion() + 1);
            existingStudent = studentRepository.save(existingStudent);
        } else {
            log.error("Product with id {} could not be updated!", student.getId());
        }
        return existingStudent;
    }

    public void delete(Integer id) {
        log.info("Deleting student with id:{}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            studentRepository.delete(student);
        } else {
            log.error("Product with id {} could not be deleted!", id);
        }
    }
}
