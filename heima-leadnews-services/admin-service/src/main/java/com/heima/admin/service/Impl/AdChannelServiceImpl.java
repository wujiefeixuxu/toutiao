package com.heima.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.ChannelMapper;
import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dto.ChannelDTO;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Service
public class AdChannelServiceImpl extends ServiceImpl<ChannelMapper,AdChannel>implements AdChannelService {

    @Override
    public ResponseResult findByNameAndPage(ChannelDTO dto) {
        // 1. 校验参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "参数错误");

        }
        dto.checkParam();// 检查分页
        // 2. 封装条件 执行查询
        LambdaQueryWrapper<AdChannel> queryWrapper = Wrappers.<AdChannel>lambdaQuery();
        //name
        if (StringUtils.isNoneBlank(dto.getName())) {
            queryWrapper.like(AdChannel::getName, dto.getName());
        }

        if (dto.getStatus() != null) {
            queryWrapper.eq(AdChannel::getStatus, dto.getStatus());
        }
        queryWrapper.orderByAsc(AdChannel::getOrd);


        // 分页
        Page<AdChannel> pageReq = new Page<>(dto.getPage(), dto.getSize());

        IPage<AdChannel> page = this.page(pageReq, queryWrapper);
        return new PageResponseResult(dto.getPage(), dto.getSize(), page.getTotal(), page.getRecords());


    }

    /**
     * 新增
     *
     * @param adchannel
     * @return
     */
    @Override
    public ResponseResult insert(AdChannel adchannel) {
        if (adchannel.getClass() == null || StringUtils.isBlank(adchannel.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "频道名称不能为空");
        }
        if (adchannel.getName().length() > 10) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称不能大于10");
        }

            // 2 判断该频道是否存在
            int count = this.count(Wrappers.<AdChannel>lambdaQuery().eq(AdChannel::getName, adchannel.getName()));
            if (count > 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "该频道已存在");
            }
            adchannel.setCreatedTime(new Date());
            this.save(adchannel);
        return ResponseResult.okResult();

        }

    @Override
    public ResponseResult update(AdChannel adChannel) {
        if (adChannel == null||adChannel.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称不能为空");
        }
        AdChannel oldchannel = getById(adChannel.getId());
        if (oldchannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"频道信息不存在");}
        if(StringUtils.isNotBlank(adChannel.getName()) && !adChannel.getName().equals(oldchannel.getName())){
            int count = this.count(Wrappers.<AdChannel>lambdaQuery().eq(AdChannel::getName, adChannel.getName()));
            if (count > 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "该频道已存在");
            }
        }
        updateById(adChannel);
        return ResponseResult.okResult();

    }

    @Override
    public ResponseResult deleteById(Integer id) {

        //1.检查参数
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道id不能为空");
        }
        //2.判断当前频道是否存在 和 是否有效
        AdChannel adChannel = getById(id);
        if(adChannel==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"频道不存在");
        }
        // 启用状态下不能删除
        if (adChannel.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE,"频道有效,不能删除");
        }
        //3.删除频道
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}

