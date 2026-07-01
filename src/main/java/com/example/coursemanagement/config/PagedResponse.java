package com.example.coursemanagement.config;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class PagedResponse<T> {

    private final List<T> items;
    private final PaginationMeta pagination;

    @Getter
    public static class PaginationMeta {
        private final int currentPage;
        private final int pageSize;
        private final int totalPages;
        private final long totalItems;

        public PaginationMeta(int currentPage, int pageSize,
                              int totalPages, long totalItems) {
            this.currentPage = currentPage;
            this.pageSize    = pageSize;
            this.totalPages  = totalPages;
            this.totalItems  = totalItems;
        }
    }

    private PagedResponse(List<T> items, PaginationMeta pagination) {
        this.items      = items;
        this.pagination = pagination;
    }

    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                new PaginationMeta(
                        page.getNumber() + 1,
                        page.getSize(),
                        page.getTotalPages(),
                        page.getTotalElements()
                )
        );
    }
}