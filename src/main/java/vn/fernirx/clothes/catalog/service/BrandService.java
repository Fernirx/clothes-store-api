package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.CreateBrandRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateBrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.common.response.PageResponse;

public interface BrandService {

    PageResponse<BrandResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    PageResponse<BrandResponse> getAllByActiveTrue(Integer page, Integer size, String sortBy, String sortDir);

    BrandResponse getById(Long id);

    BrandResponse getBySlug(String slug);

    BrandResponse create(CreateBrandRequest request);

    BrandResponse update(Long id, UpdateBrandRequest request);

    void delete(Long id);
}
