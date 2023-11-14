package vasilkov.labbpls2.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasilkov.labbpls2.api.request.OrderRequest;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.service.impl.OrderServiceImpl;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "Bad request")
})
@Slf4j
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    @PostMapping()
    public ResponseEntity<?> addNewOrder(@Valid @RequestBody OrderRequest orderRequestModel) throws ParsingException, IOException {
        log.info("NewOrder with product: " + orderRequestModel.toString());
        return ResponseEntity.ok(orderServiceImpl.save(orderRequestModel));
    }

    @PostMapping("/product/{id}")
    public ResponseEntity<?> addNewOrderWithId(@Valid @PathVariable Integer id) throws ParsingException, IOException {
        log.info("NewOrder with id" + id);
        return ResponseEntity.ok(orderServiceImpl.findAndSave(id));
    }

    @GetMapping()
    public ResponseEntity<?> getUserOrder(@RequestParam(required = false) Map<String, String> values) {
        log.info("Get UserOrder with values: " + values);
        if (values.isEmpty())
            return orderServiceImpl.findOrdersList();

        List<Order> orders = orderServiceImpl.findAllOrdersBySpecification(values);

        return ResponseEntity.ok(orders);

    }
}