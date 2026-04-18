package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.AssignCategoriesRequest;

public interface ProductCategoryService {
    void assignCategories(Long productId, AssignCategoriesRequest request);
}
