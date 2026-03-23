package vn.fernirx.clothes.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.request.PurchaseRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseResponse;
import vn.fernirx.clothes.inventory.entity.Purchase;
import vn.fernirx.clothes.inventory.enums.PaymentStatus;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    private final PurchaseItemMapper purchaseItemMapper;

    public PurchaseResponse toResponse(Purchase purchase) {
        if (purchase == null) return null;
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getSupplier() != null ? purchase.getSupplier().getId() : null,
                purchase.getSupplier() != null ? purchase.getSupplier().getName() : null,
                purchase.getPurchaseCode(),
                purchase.getTotalCost(),
                purchase.getPaymentStatus(),
                purchase.getStatus(),
                purchase.getNotes(),
                purchase.getItems() != null
                        ? purchase.getItems().stream()
                                .map(purchaseItemMapper::toResponse)
                                .collect(Collectors.toList())
                        : Collections.emptyList(),
                purchase.getCreatedAt(),
                purchase.getUpdatedAt()
        );
    }

    public Purchase toEntity(PurchaseRequest request) {
        if (request == null) return null;
        Purchase purchase = new Purchase();
        purchase.setPurchaseCode(request.getPurchaseCode());
        purchase.setNotes(request.getNotes());
        purchase.setPaymentStatus(request.getPaymentStatus() != null
                ? request.getPaymentStatus()
                : PaymentStatus.UNPAID);
        purchase.setStatus(request.getStatus() != null
                ? request.getStatus()
                : PurchaseStatus.DRAFT);
        purchase.setTotalCost(BigDecimal.ZERO);
        // supplier is set by the service
        return purchase;
    }
}
