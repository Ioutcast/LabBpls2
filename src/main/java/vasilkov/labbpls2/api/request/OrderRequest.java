package vasilkov.labbpls2.api.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderRequest implements Serializable {

    @NotBlank(message = "description не должен быть пустым!")
    private String description;

    @NotNull(message = "color не должен быть пустым!")
    private String color;

    @NotNull(message = "material не должен быть пустым!")
    private String material;

    @NotNull(message = "number_of_pieces_in_a_package не должен быть пустым!")
    private Integer number_of_pieces_in_a_package;

    @NotNull(message = "country_of_origin не должен быть пустым!")
    private String country_of_origin;

    @NotBlank(message = "Brand не должен быть пустым!")
    private String brandName;

    @NotBlank(message = "Model не должен быть пустым!")
    private String modelName;

    private Integer guarantee_period;


}
