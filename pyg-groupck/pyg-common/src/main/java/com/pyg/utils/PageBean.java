package com.pyg.utils;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable{
    // 当前页
    private int pageNum;
    // 页大小
    private int pageSize=5;
    // 总记录数
    private int totalCount;
    // 总页数
    private int totalPage;

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return (int) Math.ceil((double)totalCount/pageSize);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
