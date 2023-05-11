package com.heima.admin.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.common.exception.CustException;
import com.heima.model.admin.dto.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.admin.vo.AdUserVO;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    /**
     * 登录功能
     *
     * @param DTO
     * @return
     */
    @Override
    public ResponseResult login(AdUserDTO DTO) {
        String password = DTO.getPassword();

        //参数校验
        if (StringUtils.isBlank(DTO.getName())||StringUtils.isBlank(DTO.getPassword())) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"用户名密码不能为空！！");
}
        //2 根据用户名查询用户信息
        AdUser adUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, DTO.getName()));
        if (adUser == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
        //3 获取数据库密码和盐， 匹配密码
        String dbPwd = DigestUtils.md5DigestAsHex((password + adUser.getSalt()).getBytes());
        String oldPassword = adUser.getPassword();
        if (!dbPwd.equals(oldPassword)) {
            CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "用户名或密码错误");
        }
        if (adUser.getStatus().intValue() != 9){
            CustException.cust(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }
        //4 修改登录时间
            adUser.setLoginTime(new Date());
            updateById(adUser);
        //5 颁发token jwt 令牌
        String token = AppJwtUtil.getToken(Long.valueOf(adUser.getId()));
        // 用户信息返回 VO
        AdUserVO adUserVO = new AdUserVO();
        BeanUtils.copyProperties(adUser, adUserVO);

        HashMap hashMap = new HashMap();
        hashMap.put("token",token);
        hashMap.put("user",adUser);
        return ResponseResult.okResult(hashMap);
        //6 返回结果（jwt）
    }
}
