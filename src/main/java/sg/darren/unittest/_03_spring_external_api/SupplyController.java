package sg.darren.unittest._03_spring_external_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@Slf4j
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    SupplyService supplyService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getSupply(@PathVariable Integer id) {
        log.info("Finding supply daya for product with id: {}", id);
        return supplyService.getSupplyData(id)
                .map(s -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .location(new URI("/supply/" + s.getProductId()))
                                .body(s);
                    } catch (URISyntaxException ex) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> newSale(@RequestBody Sale sale) {
        log.info("Creating sale data");
        return supplyService.purchaseProduct(sale.getProductId(), sale.getQuantity())
                .map(s -> {
                    try {
                        return ResponseEntity
                                .created(new URI("/supply/" + s.getProductId()))
                                .body(s);
                    } catch (URISyntaxException ex) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
