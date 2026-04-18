package vn.fernirx.clothes.catalog.service.impl;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.CreateBrandRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateBrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.entity.Brand;
import vn.fernirx.clothes.catalog.mapper.BrandMapper;
import vn.fernirx.clothes.catalog.repository.BrandRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.BrandService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceInUseException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;
    private final Slugify slugify;

    @Override
    public PageResponse<BrandResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<BrandResponse> responsePage = brandRepository.findAll(pageable)
                .map(brandMapper::toDto);
        return PageResponse.of(responsePage);
    }

    @Override
    public PageResponse<BrandResponse> getAllByActiveTrue(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<BrandResponse> responsePage = brandRepository.findAllByIsActiveTrue(pageable)
                .map(brandMapper::toDto);
        return PageResponse.of(responsePage);
    }

    @Override
    public BrandResponse getById(Long id) {
        Brand brand = findBrandById(id);
        return brandMapper.toDto(brand);
    }

    @Override
    public BrandResponse getBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand"));
        return brandMapper.toDto(brand);
    }

    @Override
    @Transactional
    public BrandResponse create(CreateBrandRequest request) {
        if (brandRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException("Brand");
        }
        Brand brand = brandMapper.toEntity(request);
        brand.setSlug(slugify.slugify(brand.getName()));
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    @Override
    @Transactional
    public BrandResponse update(Long id, UpdateBrandRequest request) {
        Brand brand = findBrandById(id);

        if (request.name() != null && !request.name().equals(brand.getName())
                && brandRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException("Brand");
        }

        brandMapper.updateFromRequest(request, brand);
        if (request.name() != null) {
            brand.setSlug(slugify.slugify(request.name()));
        }
        brandRepository.save(brand);
        return brandMapper.toDto(brand);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Brand brand = findBrandById(id);
        if (productRepository.existsByBrandId(id)) {
            throw new ResourceInUseException("Brand");
        }
        brandRepository.delete(brand);
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand"));
    }
}
