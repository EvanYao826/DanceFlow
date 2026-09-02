package com.danceflow.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一分页返回结构：records / total / page / pageSize。
 */
@Data
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;
    private long total;
    private long page;
    private long pageSize;
}
