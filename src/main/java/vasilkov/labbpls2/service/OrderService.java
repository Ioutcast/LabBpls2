package vasilkov.labbpls2.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
import vasilkov.labbpls2.specifications.OrderWithBrandName;
import vasilkov.labbpls2.specifications.OrderWithCityName;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.client.RestTemplate;
import static org.springframework.http.HttpMethod.POST;


@Slf4j
@Service @RequiredArgsConstructor
public class OrderService {

    private final EmailService emailService;
    private final UserService userService;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
//    private final JavaMailSenderImpl javaMailSender;
    private final RabbitMQProducerService rabbitMQProducerService;

    private final RestTemplate restTemplate;

    @Transactional
    public MessageResponse save(OrderRequest orderRequestModel) throws ParsingException, IOException {
        Order order = new Order();
        order.setDescription(orderRequestModel.getDescription());
        order.setColor(orderRequestModel.getColor());
        order.setMaterial(orderRequestModel.getMaterial());
        order.setCountry_of_origin(orderRequestModel.getCountry_of_origin());
        order.setNumber_of_pieces_in_a_package(orderRequestModel.getNumber_of_pieces_in_a_package());
        order.setGuarantee_period(orderRequestModel.getGuarantee_period());
        log.info("!! check brand");
        order.setBrand(brandRepository.findBrandByName(orderRequestModel.getBrandName())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Brand Not Found")));

        Model model = modelRepository.findModelByName(orderRequestModel.getModelName())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Model Not Found"));
        if (orderRequestModel.getBrandName().equals(model.getBrand().getName())) {
            order.setModel(model);
        } else {
            throw new ResourceNotFoundException("Error: This brand doesn't have this model");
        }
        log.info("save");
        order.setUserEmail("vasilkov.a.c@yandex.ru");
        orderRepository.save(order);
        return (new MessageResponse("order registered successfully!"));

    }

    @Transactional
    public MessageResponse findAndSave(Integer id) throws ParsingException, IOException {
        Product product = productRepository.findByArticle(id).orElseThrow(() -> new ResourceNotFoundException("Error: Product Not Found"));
        save(new OrderRequest(product.getDescription(), product.getColor(), product.getMaterial(),
                product.getNumber_of_pieces_in_a_package(), product.getCountry_of_origin(),
                product.getBrand().getName(), product.getModel().getName(), product.getGuarantee_period())
        );
        return (new MessageResponse("order registered successfully!"));
    }

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

    public ResponseEntity<?> findOrdersList() {
        Optional<List<Order>> ordersList = orderRepository.findAllByUserEmail(
                String.valueOf((SecurityContextHolder.getContext().getAuthentication().getPrincipal())));

        if (ordersList.get().isEmpty()) return ResponseEntity.ok(new MessageResponse("You haven't any orders yet"));

        return ResponseEntity.ok(ordersList);
    }

    @Transactional
    public void grantOrderWithEmail(GrantRequest grantRequest) throws ParsingException, IOException {
        System.out.println(grantRequest.getId());
        Order order = orderRepository.findById(Math.toIntExact(grantRequest.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Error: Order Not Found"));
        order.setStatus(grantRequest.getFinalStatus());
        orderRepository.save(order);
    }

}