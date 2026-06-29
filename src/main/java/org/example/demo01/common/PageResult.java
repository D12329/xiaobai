package org.example.demo01.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long totalElements;
    private List<T> content;
    private long currentPage;
    private long pageSize;
    private int totalPages;

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
