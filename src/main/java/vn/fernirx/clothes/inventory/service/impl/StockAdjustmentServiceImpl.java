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
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentItemRequest;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentResponse;
import vn.fernirx.clothes.inventory.entity.StockAdjustment;
import vn.fernirx.clothes.inventory.entity.StockAdjustmentItem;
import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;
import vn.fernirx.clothes.inventory.mapper.StockAdjustmentItemMapper;
import vn.fernirx.clothes.inventory.mapper.StockAdjustmentMapper;
import vn.fernirx.clothes.inventory.repository.StockAdjustmentRepository;
import vn.fernirx.clothes.inventory.service.StockAdjustmentService;
import vn.fernirx.clothes.security.SecurityUtils;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockAdjustmentServiceImpl implements StockAdjustmentService {

    private final StockAdjustmentRepository adjustmentRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final StockAdjustmentMapper adjustmentMapper;
    private final StockAdjustmentItemMapper stockAdjustmentItemMapper;

    @Override
    public PageResponse<StockAdjustmentResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<StockAdjustmentResponse> result = adjustmentRepository.findAll(pageable)
                .map(adjustmentMapper::toResponse);
        return PageResponse.of(result);
    }

    @Override
    public StockAdjustmentResponse getById(Long id) {
        StockAdjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockAdjustment"));
        return adjustmentMapper.toResponse(adjustment);
    }

    @Override
    @Transactional
    public StockAdjustmentResponse create(StockAdjustmentRequest request) {
        if (adjustmentRepository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("StockAdjustment with code '" + request.getCode() + "'");
        }
        StockAdjustment adjustment = adjustmentMapper.toEntity(request);
        adjustment.setCreatedBy(getCurrentUser());
        List<StockAdjustmentItem> items = buildItems(request.getItems(), adjustment);
        adjustment.getItems().addAll(items);
        return adjustmentMapper.toResponse(adjustmentRepository.save(adjustment));
    }

    @Override
    @Transactional
    public StockAdjustmentResponse update(Long id, StockAdjustmentRequest request) {
        StockAdjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockAdjustment"));

        if (adjustment.getStatus() != AdjustmentStatus.DRAFT) {
            throw new ResourceInUseException("StockAdjustment");
        }
        if (adjustmentRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new ResourceAlreadyExistsException("StockAdjustment with code '" + request.getCode() + "'");
        }

        adjustmentMapper.updateFromRequest(request, adjustment);

        adjustment.getItems().clear();
        List<StockAdjustmentItem> items = buildItems(request.getItems(), adjustment);
        adjustment.getItems().addAll(items);

        return adjustmentMapper.toResponse(adjustmentRepository.save(adjustment));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        StockAdjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockAdjustment"));
        if (adjustment.getStatus() != AdjustmentStatus.DRAFT) {
            throw new ResourceInUseException("StockAdjustment");
        }
        adjustmentRepository.delete(adjustment);
    }

    private User getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new ResourceNotFoundException("Current user"));
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }

    private List<StockAdjustmentItem> buildItems(List<StockAdjustmentItemRequest> itemRequests,
                                                  StockAdjustment adjustment) {
        List<StockAdjustmentItem> items = new ArrayList<>();
        if (itemRequests == null || itemRequests.isEmpty()) {
            return items;
        }
        for (StockAdjustmentItemRequest itemRequest : itemRequests) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductVariant"));
            StockAdjustmentItem item = stockAdjustmentItemMapper.toEntity(itemRequest);
            item.setAdjustment(adjustment);
            item.setVariant(variant);
            items.add(item);
        }
        return items;
    }
}
