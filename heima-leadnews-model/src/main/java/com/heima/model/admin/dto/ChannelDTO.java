package com.heima.model.admin.dto;

import com.heima.model.common.dtos.PageRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelDTO extends PageRequestDTO {
    /*
    *频道名称
     */
    @ApiModelProperty("频道名称")
    private String name;
    private Integer status;

}
