package vn.fernirx.clothes.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentResponse;
import vn.fernirx.clothes.inventory.entity.StockAdjustment;
import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;

@Mapper(componentModel = "spring", uses = {StockAdjustmentItemMapper.class})
public interface StockAdjustmentMapper {

    @Mapping(source = "createdBy.id", target = "createdBy")
    StockAdjustmentResponse toResponse(StockAdjustment adjustment);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultExpression = "java(AdjustmentStatus.DRAFT)")
    StockAdjustment toEntity(StockAdjustmentRequest request);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(StockAdjustmentRequest request, @MappingTarget StockAdjustment adjustment);
}
