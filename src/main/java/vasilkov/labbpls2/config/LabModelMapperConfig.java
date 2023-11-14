package vasilkov.labbpls2.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vasilkov.labbpls2.api.request.OrderRequest;
import vasilkov.labbpls2.entity.Brand;
import vasilkov.labbpls2.entity.Model;
import vasilkov.labbpls2.entity.Order;
import vasilkov.labbpls2.entity.Product;
import vasilkov.labbpls2.exception.ResourceNotFoundException;
import vasilkov.labbpls2.repository.BrandRepository;
import vasilkov.labbpls2.repository.ModelRepository;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class LabModelMapperConfig {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;


    @Bean
    public ModelMapper modelMapperBean() {

        ModelMapper mapper = new ModelMapper();

        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        map_OrderRequest_To_Order(mapper, brandRepository, modelRepository);

        map_Product_To_Order(mapper);

        return mapper;
    }

    private static void map_OrderRequest_To_Order(
            ModelMapper mapper,
            BrandRepository brandRepository,
            ModelRepository modelRepository

    ) {
        TypeMap<OrderRequest, Order> orderRequestMapping = mapper
                .createTypeMap(OrderRequest.class, Order.class);

        Converter<String, Brand> brandFromDb = c ->
                brandRepository.findBrandByName(c.getSource()).orElseThrow(() -> new ResourceNotFoundException("Error: Brand Not Found"));

        Converter<String, Model> modelFromDb = c ->
                modelRepository.findModelByName(c.getSource()).orElseThrow(() -> new ResourceNotFoundException("Error: Model Not Found"));


        orderRequestMapping.addMappings(mapping -> {
            mapping.using(brandFromDb).map(
                    OrderRequest::getBrandName,
                    Order::setBrand
            );
            mapping.using(modelFromDb).map(
                    OrderRequest::getModelName,
                    Order::setModel
            );
        });
    }

    private static void map_Product_To_Order(
            ModelMapper mapper) {
        TypeMap<Product, OrderRequest> productMapping = mapper
                .createTypeMap(Product.class, OrderRequest.class);

        Converter<Brand, String> brandNameFromBrand = c -> c.getSource().getName();

        Converter<Model, String> ModelNameFromModel = c -> c.getSource().getName();


        productMapping.addMappings(mapping -> {
            mapping.using(brandNameFromBrand).map(
                    Product::getBrand,
                    OrderRequest::setBrandName
            );
            mapping.using(ModelNameFromModel).map(
                    Product::getModel,
                    OrderRequest::setModelName
            );
        });
    }

}
