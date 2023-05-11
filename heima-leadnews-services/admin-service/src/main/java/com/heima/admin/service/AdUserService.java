package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dto.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.common.dtos.ResponseResult;

public interface AdUserService extends IService<AdUser> {
    ResponseResult login(AdUserDTO DTO);
}
