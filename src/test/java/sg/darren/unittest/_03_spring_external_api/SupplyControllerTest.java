package sg.darren.unittest._03_spring_external_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class SupplyControllerTest {

    @MockBean
    private SupplyService supplyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET - /supply/{id} - success")
    void testGetSupplySuccess() throws Exception {
        Supply mockSupply = new Supply(1, "New Product", "Utilities", 100);

        Mockito.doReturn(Optional.of(mockSupply)).when(supplyService).getSupplyData(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/supply/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("New Product")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Matchers.is("Utilities")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(100)));
    }

    @Test
    @DisplayName("GET - /supply/{id} - fail")
    void testGetSupplyFail() throws Exception {
        Mockito.doReturn(Optional.empty()).when(supplyService).getSupplyData(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/supply/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST - /supply/sale - success")
    void testAddSaleSuccess() throws Exception {
        Supply mockSupply = new Supply(1, "New Product", "Utilities", 100);
        Sale mockSale = new Sale(mockSupply.getProductId(), 10);

        Mockito.doReturn(Optional.of(mockSupply)).when(supplyService)
                .purchaseProduct(mockSale.getProductId(), mockSale.getQuantity());

        mockMvc.perform(MockMvcRequestBuilders.post("/supply/sale")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(mockSale)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/supply/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Matchers.is("New Product")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Matchers.is("Utilities")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", Matchers.is(100)));
    }

}