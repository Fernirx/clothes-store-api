package vn.fernirx.clothes.catalog.service.impl;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.CreateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.mapper.CategoryMapper;
import vn.fernirx.clothes.catalog.repository.CategoryRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.CategoryService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceInUseException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final Slugify slugify;

    @Override
    public PageResponse<CategoryResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<CategoryResponse> responsePage = categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
        return PageResponse.of(responsePage);
    }

    @Override
    public List<CategoryResponse> getRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = findCategoryById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException("Category");
        }

        Category category = categoryMapper.toEntity(request);
        if (request.parentId() != null) {
            Category parent = findCategoryById(request.parentId());
            category.setParent(parent);
        }
        category.setSlug(slugify.slugify(category.getName()));
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = findCategoryById(id);

        if (request.name() != null && !request.name().equals(category.getName())
                && categoryRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException("Category");
        }

        categoryMapper.updateFromRequest(request, category);
        if (request.parentId() != null) {
            Category parent = findCategoryById(request.parentId());
            category.setParent(parent);
        }
        if (request.name() != null)
            category.setSlug(slugify.slugify(request.name()));
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = findCategoryById(id);
        if (categoryRepository.existsByParentId(id)) {
            throw new ResourceInUseException("Category");
        }
        if (productRepository.existsByCategoriesId(id)) {
            throw new ResourceInUseException("Category");
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category"));
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getChildrenBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category"));
        return categoryRepository.findByParent(category)
                .stream().map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category"));
    }
}
