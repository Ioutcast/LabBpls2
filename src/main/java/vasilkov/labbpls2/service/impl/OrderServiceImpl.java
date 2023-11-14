package vasilkov.labbpls2.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vasilkov.labbpls2.api.request.GrantRequest;
import vasilkov.labbpls2.api.response.MessageResponse;
import vasilkov.labbpls2.api.request.OrderRequest;
import vasilkov.labbpls2.entity.Model;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.entity.Product;
import vasilkov.labbpls2.entity.User;
import vasilkov.labbpls2.exception.ResourceIsNotValidException;
import vasilkov.labbpls2.exception.ResourceNotFoundException;
import vasilkov.labbpls2.repository.BrandRepository;
import vasilkov.labbpls2.repository.ModelRepository;
import vasilkov.labbpls2.repository.OrderRepository;
import vasilkov.labbpls2.repository.ProductRepository;
import vasilkov.labbpls2.service.OrderService;
import vasilkov.labbpls2.specifications.OrderWithBrandName;
import vasilkov.labbpls2.specifications.OrderWithCityName;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserServiceImpl userServiceImpl;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RabbitMQProducerServiceImpl rabbitMQProducerServiceImpl;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public MessageResponse save(OrderRequest orderRequestModel) throws ParsingException, IOException {

        Order order = modelMapper.map(orderRequestModel, Order.class);

        if (
                orderRequestModel.getBrandName().equals(order.getModel().getBrand().getName())
        )
            throw new ResourceNotFoundException("Error: This brand doesn't have this model");


        User user = userServiceImpl.getByEmail(String.valueOf((SecurityContextHolder.getContext().getAuthentication().getPrincipal())))
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"));

        order.setUserEmail(user.getEmail());
        orderRepository.save(order);

        return (new MessageResponse("order registered successfully!"));

    }

    @Override
    @Transactional
    public MessageResponse findAndSave(Integer id) throws ParsingException, IOException {

        Product product = productRepository.findByArticle(id).orElseThrow(() -> new ResourceNotFoundException("Error: Product Not Found"));

        save(modelMapper.map(product,OrderRequest.class));

        return (new MessageResponse("order registered successfully!"));
    }

    @Override
    public List<Order> findAllOrdersBySpecification(Map<String, String> values) {
        if (values.get("page") == null || values.get("size") == null ||
                Integer.parseInt(values.get("page")) < 0 || Integer.parseInt(values.get("size")) <= 0)
            throw new ResourceIsNotValidException("Error: page and size are necessarily and must be positive");
        return orderRepository.findAll(Specification
                        .where(new OrderWithBrandName(values.get("brand")))
                        .and(new OrderWithCityName(values.get("city"))),
                PageRequest.of(Integer.parseInt(values.get("page")),
                        Integer.parseInt(values.get("size")))).toList();
    }

    @Override
    public ResponseEntity<?> findOrdersList() {
        Optional<List<Order>> ordersList = orderRepository.findAllByUserEmail(
                String.valueOf((SecurityContextHolder.getContext().getAuthentication().getPrincipal())));

        if (ordersList.get().isEmpty()) return ResponseEntity.ok(new MessageResponse("You haven't any orders yet"));

        return ResponseEntity.ok(ordersList);
    }

    @Override
    @Transactional
    public void grantOrderWithEmail(GrantRequest grantRequest) throws ParsingException, IOException {
        System.out.println(grantRequest.getId());
        Order order = orderRepository.findById(Math.toIntExact(grantRequest.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Error: Order Not Found"));
        order.setStatus(grantRequest.getFinalStatus());

        orderRepository.save(order);
        rabbitMQProducerServiceImpl.sendMessage("STATUS", order);

    }

}