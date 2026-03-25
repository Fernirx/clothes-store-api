package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.BrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.entity.Brand;
import vn.fernirx.clothes.catalog.mapper.BrandMapper;
import vn.fernirx.clothes.catalog.repository.BrandRepository;
import vn.fernirx.clothes.catalog.service.BrandService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public PageResponse<BrandResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<BrandResponse> responsePage = brandRepository.findAll(pageable)
                .map(brandMapper::toResponse);
        return PageResponse.of(responsePage);
    }

    @Override
    public BrandResponse getById(Long id) {
        Brand brand = findBrandById(id);
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse getBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand"));
        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional
    public BrandResponse create(BrandRequest request) {
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new ResourceAlreadyExistsException("Brand with slug '" + request.getSlug() + "'");
        }

        Brand brand = brandMapper.toEntity(request);
        Brand saved = brandRepository.save(brand);
        return brandMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BrandResponse update(Long id, BrandRequest request) {
        Brand brand = findBrandById(id);

        if (brandRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new ResourceAlreadyExistsException("Brand with slug '" + request.getSlug() + "'");
        }

        brandMapper.updateFromRequest(request, brand);

        Brand saved = brandRepository.save(brand);
        return brandMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Brand brand = findBrandById(id);
        brandRepository.delete(brand);
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand"));
    }
}
