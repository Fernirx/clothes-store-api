package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.CreateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.common.response.PageResponse;

import java.util.List;

public interface CategoryService {

    PageResponse<CategoryResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    List<CategoryResponse> getRootCategories();

    CategoryResponse getById(Long id);

    CategoryResponse create(CreateCategoryRequest request);

    CategoryResponse update(Long id, UpdateCategoryRequest request);

    void delete(Long id);

    CategoryResponse getBySlug(String slug);

    List<CategoryResponse> getChildrenBySlug(String slug);
}
