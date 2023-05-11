package com.heima.model.common.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询： 返回PageResponseResult
 */
@Data
public class PageResponseResult extends ResponseResult implements Serializable {
    // 当前页码
    private Integer currentPage;
     /*一页显示记录*/
    private Integer size;
    private Long total; // 总记录

    public PageResponseResult(Integer currentPage, Integer size, Long total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }

    public PageResponseResult(Integer currentPage, Integer size, Long total, Object data) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
        super.setData(data);
    }


    public PageResponseResult() {

    }
}
