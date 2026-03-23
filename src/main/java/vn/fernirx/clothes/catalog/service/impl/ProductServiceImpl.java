package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.ProductRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductResponse;
import vn.fernirx.clothes.catalog.entity.Brand;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.mapper.ProductMapper;
import vn.fernirx.clothes.catalog.repository.BrandRepository;
import vn.fernirx.clothes.catalog.repository.CategoryRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.ProductService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public PageResponse<ProductResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<ProductResponse> responsePage = productRepository.findAll(pageable)
                .map(productMapper::toResponse);
        return PageResponse.of(responsePage);
    }

    @Override
    public PageResponse<ProductResponse> getActive(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<ProductResponse> responsePage = productRepository.findByIsActiveTrue(pageable)
                .map(productMapper::toResponse);
        return PageResponse.of(responsePage);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new ResourceAlreadyExistsException("Product with slug '" + request.getSlug() + "'");
        }
        if (productRepository.existsByCode(request.getCode())) {
            throw new ResourceAlreadyExistsException("Product with code '" + request.getCode() + "'");
        }

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id " + request.getBrandId()));

        Product product = productMapper.toEntity(request);
        product.setBrand(brand);

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = resolveCategories(request.getCategoryIds());
            product.setCategories(categories);
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProductById(id);

        if (productRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new ResourceAlreadyExistsException("Product with slug '" + request.getSlug() + "'");
        }

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id " + request.getBrandId()));

        product.setBrand(brand);
        product.setCode(request.getCode());
        product.setSlug(request.getSlug());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setGender(request.getGender());
        product.setMaterial(request.getMaterial());
        product.setOriginCountry(request.getOriginCountry());
        product.setBasePrice(request.getBasePrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setCostPrice(request.getCostPrice());

        if (request.getIsNew() != null) product.setIsNew(request.getIsNew());
        if (request.getIsOnSale() != null) product.setIsOnSale(request.getIsOnSale());
        if (request.getIsActive() != null) product.setIsActive(request.getIsActive());

        if (request.getCategoryIds() != null) {
            Set<Category> categories = resolveCategories(request.getCategoryIds());
            product.setCategories(categories);
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id));
    }

    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId));
            categories.add(category);
        }
        return categories;
    }
}
