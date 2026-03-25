package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.BrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.common.response.PageResponse;

import java.util.List;

public interface BrandService {

    PageResponse<BrandResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    BrandResponse getById(Long id);

    BrandResponse getBySlug(String slug);

    BrandResponse create(BrandRequest request);

    BrandResponse update(Long id, BrandRequest request);

    void delete(Long id);
}
