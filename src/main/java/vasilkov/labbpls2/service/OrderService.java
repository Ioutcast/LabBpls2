package vasilkov.labbpls2.service;

import nu.xom.ParsingException;
import org.springframework.http.ResponseEntity;
import vasilkov.labbpls2.api.request.GrantRequest;
import vasilkov.labbpls2.api.request.OrderRequest;
import vasilkov.labbpls2.api.response.MessageResponse;
import vasilkov.labbpls2.entity.Order;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    MessageResponse save(OrderRequest orderRequestModel) throws ParsingException, IOException;
    MessageResponse findAndSave(Integer id) throws ParsingException, IOException;

    List<Order> findAllOrdersBySpecification(Map<String, String> values);
    ResponseEntity<?> findOrdersList();
    void grantOrderWithEmail(GrantRequest grantRequest) throws ParsingException, IOException;
}
