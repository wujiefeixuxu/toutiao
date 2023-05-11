package com.heima.admin.controller.v1;

import com.heima.admin.service.AdChannelService;
import com.heima.model.admin.dto.ChannelDTO;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "频道管理控制类",tags = "频道管理控制类" )
@RequestMapping("/api/v1/channel")
public class AdChannelController {
    @Autowired
    private AdChannelService adChannelService;
    @ApiOperation("频道分页列表查询")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody ChannelDTO dto,@RequestHeader("userId") String userId){
        System.out.println(userId);
        return adChannelService.findByNameAndPage(dto);

    }
    @ApiOperation("频道新增")
    @PostMapping("/save")
    public ResponseResult insert(@RequestBody AdChannel channel) {
        return adChannelService.insert(channel);
    }
    @ApiOperation("频道修改")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdChannel adChannel) {
        return adChannelService.update(adChannel);
    }

    @ApiOperation("根据频道ID删除")
    @GetMapping("/del/{id}")
    public ResponseResult deleteById(@PathVariable("id") Integer id) {
        return adChannelService.deleteById(id);
    }
}
