package com.dimovecar.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long total;
    private List<T> records;
    private Long pageNum;
    private Long pageSize;

    public PageResult() {}

    public PageResult(Long total, List<T> records, Long pageNum, Long pageSize) {
        this.total = total;
        this.records = records;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> of(Long total, List<T> records, Long pageNum, Long pageSize) {
        return new PageResult<>(total, records, pageNum, pageSize);
    }
}
