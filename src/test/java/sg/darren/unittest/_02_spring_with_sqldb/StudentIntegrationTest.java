package sg.darren.unittest._02_spring_with_sqldb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudentIntegrationTest {

    private static final File DATA_JSON = Paths.get("src", "test", "resources", "_02_students.json").toFile();

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Test student found by id - GET /students/{id}")
    void testGetStudentFoundById() throws Exception {
        // retrieve the first student due to ID could be different between unit test cases even if it was hardcoded in JSON file
        Student firstStudent = studentRepository.findAll().get(0);

        mockMvc.perform(get("/students/{id}", firstStudent.getId()))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, String.format("\"%s\"", firstStudent.getId())))
                .andExpect(header().string(HttpHeaders.LOCATION, String.format("/students/%s", firstStudent.getId())))
                // Validate response body
                .andExpect(jsonPath("$.firstName", Matchers.is("Linda")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Lee")))
                .andExpect(jsonPath("$.version", Matchers.is(1)));
    }

    @Test
    @DisplayName("Test all students found - GET /students")
    void testGetAllStudentsFound() throws Exception {
        mockMvc.perform(get("/students"))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response body
                .andExpect(jsonPath("$[0].firstName", Matchers.is("Linda")))
                .andExpect(jsonPath("$[1].firstName", Matchers.is("Joey")));
    }

    @Test
    @DisplayName("Test add a new student - POST /students")
    void testAddNewStudent() throws Exception {
        Student mockStudent = new Student(null, "3rd", "Spring", null);

        MvcResult mvcResult = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(mockStudent)))
                // Validate 201 CREATED and JSON response type received
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                // Validate response body
                .andExpect(jsonPath("$.firstName", Matchers.is("3rd")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Spring")))
                .andExpect(jsonPath("$.version", Matchers.is(1)))
                .andReturn();

        Integer id = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.id", Integer.class);
        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertEquals("/students/" + id, location);
    }

    @Test
    @DisplayName("Test update an existing student - PUT /students/{id}")
    void testUpdateExistingStudentWithSuccess() throws Exception {
        Student firstStudent = studentRepository.findAll().get(0);
        firstStudent.setFirstName("Updated");
        firstStudent.setLastName("Spring");

        mockMvc.perform(put("/students/{id}", firstStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.IF_MATCH, firstStudent.getVersion())
                        .content(new ObjectMapper().writeValueAsString(firstStudent)))
                // Validate 200 OK and JSON response type received
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Validate response headers
                .andExpect(header().string(HttpHeaders.ETAG, String.format("\"%s\"", firstStudent.getVersion() + 1)))
                .andExpect(header().string(HttpHeaders.LOCATION, String.format("/students/%s", firstStudent.getId())))
                // Validate response body
                .andExpect(jsonPath("$.id", Matchers.is(firstStudent.getId())))
                .andExpect(jsonPath("$.firstName", Matchers.is("Updated")))
                .andExpect(jsonPath("$.lastName", Matchers.is("Spring")))
                .andExpect(jsonPath("$.version", Matchers.is(firstStudent.getVersion() + 1)));
    }

    @Test
    @DisplayName("Test update an existing student with version mismatch - PUT /students/{id}")
    void testUpdateExistingStudentWithVersionMismatch() throws Exception {
        Student firstStudent = studentRepository.findAll().get(0);

        mockMvc.perform(put("/students/{id}", firstStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.IF_MATCH, firstStudent.getVersion() + 10)
                        .content(new ObjectMapper().writeValueAsString(firstStudent)))
                // Validate 409 CONFLICT received
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test update an non-existing student - PUT /students/{id}")
    void testUpdateNonExistingStudent() throws Exception {
        Student firstStudent = studentRepository.findAll().get(0);

        mockMvc.perform(put("/students/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(new ObjectMapper().writeValueAsString(firstStudent)))
                // Validate 404 NOT_FOUND received
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test delete an existing student - DELETE /students/{id}")
    void testDeleteExistingStudent() throws Exception {
        Student firstStudent = studentRepository.findAll().get(0);

        mockMvc.perform(delete("/students/{id}", firstStudent.getId()))
                // Validate 200 OK received
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test delete an non-existing student - DELETE /students/{id}")
    void testDeleteNonExistingStudent() throws Exception {
        mockMvc.perform(delete("/students/{id}", 0))
                // Validate 404 NOT_FOUND received
                .andExpect(status().isNotFound());
    }


}