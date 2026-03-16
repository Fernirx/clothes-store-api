package vn.fernirx.clothes.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.fernirx.clothes.common.constant.PaginationConstants;

@UtilityClass
public class PaginationUtil {
    public Pageable createPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        int pageNumber = (page == null || page < 0) ? PaginationConstants.DEFAULT_PAGE : page;
        int pageSize = (size == null || size <= 0) ? PaginationConstants.DEFAULT_SIZE
                : Math.min(PaginationConstants.MAX_SIZE, size);
        return PageRequest.of(pageNumber, pageSize, createSort(sortBy, sortDirection));
    }

    public Sort createSort(String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.isBlank()) return Sort.unsorted();
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
}
