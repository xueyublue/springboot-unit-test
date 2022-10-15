package sg.darren.unittest._03_spring_external_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class SupplyService {

    @Value("${supplyprovide.uri}")
    private String supplyServiceURI;

    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<Supply> getSupplyData(Integer id) {
        try {
            return Optional.ofNullable(restTemplate.getForObject(supplyServiceURI + "/" + id, Supply.class));
        } catch (HttpClientErrorException ex) {
            log.error("Failed to retrieve Supply data: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Supply> purchaseProduct(Integer productId, Integer quantity) {
        try {
            return Optional.ofNullable(
                    restTemplate.postForObject(
                            supplyServiceURI + "/" + productId + "/purchase",
                            new Sale(productId, quantity),
                            Supply.class
                    )
            );
        } catch (HttpClientErrorException ex) {
            log.error("Failed to purchase product: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
