package com.pyg.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/8/12.
 */
public class PageResult implements Serializable {

    //总记录数
    private Long total;

    private List<?> rows;

    public PageResult(Long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
