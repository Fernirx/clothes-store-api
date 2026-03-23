package vn.fernirx.clothes.catalog.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.catalog.dto.request.BrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.entity.Brand;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand brand) {
        if (brand == null) return null;
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getSlug(),
                brand.getDescription(),
                brand.getLogoUrl(),
                brand.getIsActive(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }

    public Brand toEntity(BrandRequest request) {
        if (request == null) return null;
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setSlug(request.getSlug());
        brand.setDescription(request.getDescription());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return brand;
    }
}
