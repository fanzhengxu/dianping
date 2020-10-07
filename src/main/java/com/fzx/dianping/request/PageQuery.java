package com.fzx.dianping.request;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 21:20 2020/2/14
 */
public class PageQuery {

    private Integer page = 1;

    private Integer size = 20;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public PageQuery(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
