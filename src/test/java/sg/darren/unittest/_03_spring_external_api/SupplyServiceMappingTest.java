package sg.darren.unittest._03_spring_external_api;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@TestPropertySource(value = "classpath:__files/test.properties")
class SupplyServiceMappingTest {

    @Autowired
    private SupplyService supplyService;

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
    @DisplayName("Get supply success")
    void testGetSupplySuccess() {
        Optional<Supply> supply = supplyService.getSupplyData(1);

        Assertions.assertTrue(supply.isPresent(), "Supply data should be exist");
    }

    @Test
    @DisplayName("Get supply fail")
    void testGetSupplyFail() {
        Optional<Supply> supply = supplyService.getSupplyData(2);

        Assertions.assertFalse(supply.isPresent(), "Supply data should not be exist");
    }

    @Test
    @DisplayName("Purchase product success")
    void testPurchaseProductSuccess() {
        Optional<Supply> supply = supplyService.purchaseProduct(1, 1000);

        Assertions.assertTrue(supply.isPresent(), "Supply data should be exist");
        Assertions.assertEquals(900, supply.get().getQuantity().intValue(), "New supply quantity should be 900");
    }

}