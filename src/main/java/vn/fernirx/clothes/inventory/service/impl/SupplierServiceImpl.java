package vn.fernirx.clothes.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceInUseException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.inventory.repository.PurchaseRepository;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.inventory.dto.request.SupplierRequest;
import vn.fernirx.clothes.inventory.dto.response.SupplierResponse;
import vn.fernirx.clothes.inventory.entity.Supplier;
import vn.fernirx.clothes.inventory.mapper.SupplierMapper;
import vn.fernirx.clothes.inventory.repository.SupplierRepository;
import vn.fernirx.clothes.inventory.service.SupplierService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public PageResponse<SupplierResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<SupplierResponse> result = supplierRepository.findAll(pageable)
                .map(supplierMapper::toResponse);
        return PageResponse.of(result);
    }

    @Override
    public SupplierResponse getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier"));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        if (supplierRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Supplier with name '" + request.getName() + "'");
        }
        if (supplierRepository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("Supplier with code '" + request.getCode() + "'");
        }
        Supplier supplier = supplierMapper.toEntity(request);
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier"));
        if (supplierRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ResourceAlreadyExistsException("Supplier with name '" + request.getName() + "'");
        }
        if (supplierRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new ResourceAlreadyExistsException("Supplier with code '" + request.getCode() + "'");
        }
        supplierMapper.updateFromRequest(request, supplier);
        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier");
        }
        if (purchaseRepository.existsBySupplierId(id)) {
            throw new ResourceInUseException("Supplier");
        }
        supplierRepository.deleteById(id);
    }
}
