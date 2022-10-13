package sg.darren.unittest._02_spring_with_sqldb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Style;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/students")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Iterable<Student> getAllStudents() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllStudent(@RequestParam Integer id) {
        Student student = studentService.findById(id);
        if (student != null) {
            try {
                return ResponseEntity
                        .ok()
                        .eTag(Integer.toString(student.getId()))
                        .location(new URI("/students/" + student.getId()))
                        .body(student);
            } catch (URISyntaxException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveStudent(@RequestBody Student student) {
        Student newStudent = studentService.save(student);
        try {
            return ResponseEntity
                    .created(new URI("/students/" + newStudent.getId()))
                    .eTag(Integer.toString(newStudent.getVersion()))
                    .body(newStudent);
        } catch (URISyntaxException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id,
                                           @RequestBody Student student,
                                           @RequestHeader("If-Match") Integer ifMatch) {
        Student existingStudent = studentService.findById(id);
        if (existingStudent != null) {
            if (!ifMatch.equals(existingStudent.getVersion())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            try {
                existingStudent.setFirstName(student.getFirstName());
                existingStudent.setLastName(student.getLastName());
                existingStudent.setVersion(student.getVersion() + 1);
                existingStudent = studentService.update(existingStudent);

                return ResponseEntity
                        .ok()
                        .eTag(Integer.toString(student.getVersion()))
                        .location(new URI("/students/" + existingStudent.getId()))
                        .body(existingStudent);
            } catch (URISyntaxException ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        Student student = studentService.findById(id);
        if (student != null) {
            studentService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
