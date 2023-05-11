package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.service.Ipml.ApUserRealnameServiceImpl;

public interface ApUserRealnameService extends IService<ApUserRealname> {
    ResponseResult loadListByStatus(com.heima.model.user.dtos.AuthDTO DTO);
    /**
     * 根据状态进行审核
     * @param dto
     * @param status  2 审核失败   9 审核成功
     * @return
     */
    ResponseResult updateStatusById(com.heima.model.user.dtos.AuthDTO dto, Short status);

}
