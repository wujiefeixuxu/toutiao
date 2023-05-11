package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dto.ChannelDTO;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelService extends IService<AdChannel> {
    public ResponseResult findByNameAndPage(ChannelDTO dto);

    /**
     * 新增
     * @param channel
     * @return
     */
    public ResponseResult insert(AdChannel channel);


    public ResponseResult update(AdChannel channel);
    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseResult deleteById(Integer id);
}
