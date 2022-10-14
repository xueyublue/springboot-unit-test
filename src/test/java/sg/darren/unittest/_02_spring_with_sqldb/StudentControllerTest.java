package sg.darren.unittest._02_spring_with_sqldb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test student found - GET /students/1")
    public void testGetStudentByIdFindsStudent() throws Exception {
        // Prepare mock student
        Student mockStudent = new Student(1, "Linda", "Lee", 1);

        // Prepare mocked service method
        doReturn(mockStudent).when(studentService).findById(1);

        // Perform GET request
        mockMvc.perform(get("/students/{id}", 1))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/students/1"))
                // Validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Linda")))
                .andExpect(jsonPath("$.lastName", is("Lee")))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("Test all students found - GET /students")
    public void testAllStudentsFound() throws Exception {
        // Prepare mock student
        Student mockStudent1 = new Student(1, "Linda", "Lee", 1);
        Student mockStudent2 = new Student(2, "Joey", "Tan", 1);

        // Prepare mocked service method
        doReturn(Arrays.asList(mockStudent1, mockStudent2)).when(studentService).findAll();

        // Perform GET request
        mockMvc.perform(get("/students"))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response body
                .andExpect(jsonPath("$[0].firstName", is("Linda")))
                .andExpect(jsonPath("$[1].firstName", is("Joey")));
    }

    @Test
    @DisplayName("Add a new student - POST /students")
    public void testAddNewStudent() throws Exception {
        // Prepare mock student
        Student mockStudent = new Student(null, "Linda", "Lee", null);
        Student savedStudent = new Student(1, "Linda", "Lee", 1);

        // Prepare mock service method
        doReturn(savedStudent).when(studentService).save(ArgumentMatchers.any());

        // Perform POST request
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(mockStudent)))
                // Validate 201 CREATED and JSON response type received
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/students/1"))
                // Validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Linda")))
                .andExpect(jsonPath("$.lastName", is("Lee")))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("Update an existing student with success - PUT /students/1")
    public void testUpdatingStudentWithSuccess() throws Exception {
        // Prepare mock student
        Student oldStudent = new Student(1, "Linda", "Lee", 1);
        Student savedStudent = new Student(1, "Joey", "Tan", 2);

        // Prepare mock service method
        doReturn(oldStudent).when(studentService).findById(1);
        doReturn(savedStudent).when(studentService).update(ArgumentMatchers.any());

        // Perform PUT request
        mockMvc.perform(put("/students/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(new ObjectMapper().writeValueAsString(oldStudent)))
                // Validate 201 CREATED and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/students/1"))
                // Validate response body
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Joey")))
                .andExpect(jsonPath("$.lastName", is("Tan")))
                .andExpect(jsonPath("$.version", is(2)));
    }

}