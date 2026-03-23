package vn.fernirx.clothes.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.catalog.repository.ProductVariantRepository;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.inventory.dto.request.InventoryTransactionRequest;
import vn.fernirx.clothes.inventory.dto.response.InventoryTransactionResponse;
import vn.fernirx.clothes.inventory.entity.InventoryTransaction;
import vn.fernirx.clothes.inventory.mapper.InventoryTransactionMapper;
import vn.fernirx.clothes.inventory.repository.InventoryTransactionRepository;
import vn.fernirx.clothes.inventory.service.InventoryTransactionService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryTransactionMapper transactionMapper;

    @Override
    public PageResponse<InventoryTransactionResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<InventoryTransactionResponse> result = transactionRepository.findAll(pageable)
                .map(transactionMapper::toResponse);
        return PageResponse.of(result);
    }

    @Override
    public PageResponse<InventoryTransactionResponse> getByVariantId(Long variantId, Integer page, Integer size) {
        if (!productVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("ProductVariant with id " + variantId);
        }
        Pageable pageable = PaginationUtil.createPageable(page, size, "createdAt", "desc");
        Page<InventoryTransactionResponse> result =
                transactionRepository.findByVariantIdOrderByCreatedAtDesc(variantId, pageable)
                        .map(transactionMapper::toResponse);
        return PageResponse.of(result);
    }

    @Override
    public InventoryTransactionResponse getById(Long id) {
        InventoryTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryTransaction with id " + id));
        return transactionMapper.toResponse(transaction);
    }

    @Override
    @Transactional
    public InventoryTransactionResponse create(InventoryTransactionRequest request) {
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant with id " + request.getVariantId()));

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setVariant(variant);
        transaction.setType(request.getType());
        transaction.setQuantity(request.getQuantity());
        transaction.setOldStock(request.getOldStock());
        transaction.setNewStock(request.getNewStock());
        transaction.setReferenceType(request.getReferenceType());
        transaction.setReferenceId(request.getReferenceId());
        transaction.setNotes(request.getNotes());

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }
}
