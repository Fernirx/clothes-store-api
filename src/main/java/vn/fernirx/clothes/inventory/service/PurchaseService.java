package vn.fernirx.clothes.inventory.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.inventory.dto.request.PurchaseRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseResponse;
import vn.fernirx.clothes.inventory.enums.PaymentStatus;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;

public interface PurchaseService {

    PageResponse<PurchaseResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    PurchaseResponse getById(Long id);

    PurchaseResponse create(PurchaseRequest request);

    PurchaseResponse update(Long id, PurchaseRequest request);

    void delete(Long id);

    void updateStatus(Long id, PurchaseStatus status);

    void updatePaymentStatus(Long id, PaymentStatus paymentStatus);
}
