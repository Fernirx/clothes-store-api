package vn.fernirx.clothes.catalog.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.catalog.dto.request.ProductRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductResponse;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        Set<Long> categoryIds = product.getCategories() == null ? Set.of()
                : product.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet());

        return new ProductResponse(
                product.getId(),
                product.getBrand() != null ? product.getBrand().getId() : null,
                product.getBrand() != null ? product.getBrand().getName() : null,
                product.getCode(),
                product.getSlug(),
                product.getName(),
                product.getDescription(),
                product.getGender(),
                product.getMaterial(),
                product.getOriginCountry(),
                product.getBasePrice(),
                product.getOriginalPrice(),
                product.getCostPrice(),
                product.getIsNew(),
                product.getIsOnSale(),
                product.getIsActive(),
                product.getSoldCount(),
                product.getViewCount(),
                categoryIds,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public Product toEntity(ProductRequest request) {
        if (request == null) return null;
        Product product = new Product();
        product.setCode(request.getCode());
        product.setSlug(request.getSlug());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setGender(request.getGender());
        product.setMaterial(request.getMaterial());
        product.setOriginCountry(request.getOriginCountry());
        product.setBasePrice(request.getBasePrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCostPrice(request.getCostPrice());
        product.setIsNew(request.getIsNew() != null ? request.getIsNew() : false);
        product.setIsOnSale(request.getIsOnSale() != null ? request.getIsOnSale() : false);
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        // brand and categories are set by the service
        return product;
    }
}
