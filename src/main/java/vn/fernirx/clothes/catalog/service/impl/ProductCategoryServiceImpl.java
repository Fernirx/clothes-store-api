package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.AssignCategoriesRequest;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductCategory;
import vn.fernirx.clothes.catalog.entity.ProductCategoryId;
import vn.fernirx.clothes.catalog.repository.CategoryRepository;
import vn.fernirx.clothes.catalog.repository.ProductCategoryRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.ProductCategoryService;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void assignCategories(Long productId, AssignCategoriesRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        List<Category> categoryList = categoryRepository.findAllById(request.categoryIds());
        if (categoryList.size() != request.categoryIds().size()) {
            throw new ResourceNotFoundException("Category");
        }
        productCategoryRepository.deleteByProductId(productId);
        List<ProductCategory> mappings = categoryList.stream()
                .map(c -> ProductCategory.builder()
                        .id(new ProductCategoryId(productId, c.getId()))
                        .product(product)
                        .category(c)
                        .build())
                .toList();
        productCategoryRepository.saveAll(mappings);
    }
}
