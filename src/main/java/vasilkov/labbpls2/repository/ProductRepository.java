package vasilkov.labbpls2.repository;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vasilkov.labbpls2.entity.Product;

import java.util.Optional;

@Repository
@Hidden
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByArticle(Integer article);
    @Query(value = "SELECT count (*) from _product where _product.model_id =:modelId",nativeQuery = true)
    Long existsByModelId(@Param("modelId") Long modelId);
}

