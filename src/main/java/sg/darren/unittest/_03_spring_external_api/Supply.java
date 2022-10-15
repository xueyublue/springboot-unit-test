package sg.darren.unittest._03_spring_external_api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supply {

    private Integer productId;
    private String productName;
    private String productCategory;
    private Integer quantity;

}
