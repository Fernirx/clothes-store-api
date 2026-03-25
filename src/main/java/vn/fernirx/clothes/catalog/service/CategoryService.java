package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.CategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.common.response.PageResponse;

import java.util.List;

public interface CategoryService {

    PageResponse<CategoryResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    List<CategoryResponse> getRootCategories();

    CategoryResponse getById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
