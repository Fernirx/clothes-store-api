package vn.fernirx.clothes.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.catalog.repository.ProductVariantRepository;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceInUseException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.inventory.dto.request.PurchaseItemRequest;
import vn.fernirx.clothes.inventory.dto.request.PurchaseRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseResponse;
import vn.fernirx.clothes.inventory.entity.Purchase;
import vn.fernirx.clothes.inventory.entity.PurchaseItem;
import vn.fernirx.clothes.inventory.entity.Supplier;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;
import vn.fernirx.clothes.inventory.enums.QualityStatus;
import vn.fernirx.clothes.inventory.mapper.PurchaseMapper;
import vn.fernirx.clothes.inventory.repository.PurchaseRepository;
import vn.fernirx.clothes.inventory.repository.SupplierRepository;
import vn.fernirx.clothes.inventory.service.PurchaseService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;
    private final PurchaseMapper purchaseMapper;

    @Override
    public PageResponse<PurchaseResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<PurchaseResponse> result = purchaseRepository.findAll(pageable)
                .map(purchaseMapper::toResponse);
        return PageResponse.of(result);
    }

    @Override
    public PurchaseResponse getById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase with id " + id));
        return purchaseMapper.toResponse(purchase);
    }

    @Override
    @Transactional
    public PurchaseResponse create(PurchaseRequest request) {
        if (purchaseRepository.existsByPurchaseCode(request.getPurchaseCode())) {
            throw new ResourceAlreadyExistsException("Purchase with code '" + request.getPurchaseCode() + "'");
        }
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + request.getSupplierId()));

        Purchase purchase = purchaseMapper.toEntity(request);
        purchase.setSupplier(supplier);

        List<PurchaseItem> items = buildItems(request.getItems(), purchase);
        purchase.getItems().addAll(items);
        purchase.setTotalCost(calculateTotalCost(items));

        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public PurchaseResponse update(Long id, PurchaseRequest request) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase with id " + id));

        if (!purchase.getPurchaseCode().equals(request.getPurchaseCode())
                && purchaseRepository.existsByPurchaseCode(request.getPurchaseCode())) {
            throw new ResourceAlreadyExistsException("Purchase with code '" + request.getPurchaseCode() + "'");
        }

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier with id " + request.getSupplierId()));

        purchase.setPurchaseCode(request.getPurchaseCode());
        purchase.setSupplier(supplier);
        purchase.setNotes(request.getNotes());
        if (request.getPaymentStatus() != null) {
            purchase.setPaymentStatus(request.getPaymentStatus());
        }
        if (request.getStatus() != null) {
            purchase.setStatus(request.getStatus());
        }

        purchase.getItems().clear();
        List<PurchaseItem> items = buildItems(request.getItems(), purchase);
        purchase.getItems().addAll(items);
        purchase.setTotalCost(calculateTotalCost(items));

        return purchaseMapper.toResponse(purchaseRepository.save(purchase));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase with id " + id));
        if (purchase.getStatus() != PurchaseStatus.DRAFT) {
            throw new ResourceInUseException("Purchase with id " + id + " (only DRAFT purchases can be deleted)");
        }
        purchaseRepository.delete(purchase);
    }

    private List<PurchaseItem> buildItems(List<PurchaseItemRequest> itemRequests, Purchase purchase) {
        List<PurchaseItem> items = new ArrayList<>();
        if (itemRequests == null || itemRequests.isEmpty()) {
            return items;
        }
        for (PurchaseItemRequest itemRequest : itemRequests) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductVariant with id " + itemRequest.getVariantId()));
            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setVariant(variant);
            item.setQuantityOrdered(itemRequest.getQuantityOrdered());
            item.setUnitCost(itemRequest.getUnitCost());
            item.setQualityStatus(itemRequest.getQualityStatus() != null
                    ? itemRequest.getQualityStatus()
                    : QualityStatus.PENDING);
            items.add(item);
        }
        return items;
    }

    private BigDecimal calculateTotalCost(List<PurchaseItem> items) {
        return items.stream()
                .map(item -> item.getUnitCost().multiply(BigDecimal.valueOf(item.getQuantityOrdered())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
