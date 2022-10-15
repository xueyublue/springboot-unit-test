package sg.darren.unittest._03_spring_external_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(value = "classpath:__files/test.properties")
class SupplyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
    }

    @AfterEach
    void cleanup() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("GET - /supply/{id} - success")
    void testGetSupplySuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/supply/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("New Product")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Matchers.is("Utilities")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(1000)));
    }

    @Test
    @DisplayName("GET - /supply/{id} - fail")
    void testGetSupplyFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST - /supply/sale - success")
    void testAddSaleSuccess() throws Exception {
        Sale mockSale = new Sale(1, 10);

        mockMvc.perform(MockMvcRequestBuilders.post("/supply/sale")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(mockSale)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/supply/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("New Product")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Matchers.is("Utilities")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(900)));
    }

}