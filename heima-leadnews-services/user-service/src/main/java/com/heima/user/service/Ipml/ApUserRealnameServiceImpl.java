package com.heima.user.service.Ipml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.admin.AdminConstants;
import com.heima.common.exception.CustException;
import com.heima.common.exception.CustomException;
import com.heima.feigns.ArticleFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDTO;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService{


    @Override
    public ResponseResult loadListByStatus(AuthDTO DTO) {
        if (DTO == null){
            throw  new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        DTO.checkParam();
        Page<ApUserRealname> pageReq = new Page<>(DTO.getPage(), DTO.getSize());
        LambdaQueryWrapper<ApUserRealname> queryWrapper = Wrappers.<ApUserRealname>lambdaQuery();
        queryWrapper.eq(DTO.getStatus()!=null,ApUserRealname::getStatus,DTO.getStatus());
        IPage<ApUserRealname> pageResult = this.page(pageReq, queryWrapper);
        return new PageResponseResult(DTO.getPage(),DTO.getSize(),pageResult.getTotal(),pageResult.getRecords());
    }


    @Autowired
    ApUserMapper apUserMapper;
    @Autowired
    WemediaFeign wemediaFeign;
    @Autowired
    ArticleFeign articleFeign;


    @Override
    public ResponseResult updateStatusById(AuthDTO dto, Short status) {
        //1 参数检查
        if (null == dto) {
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);

        }
        ApUserRealname apUserRealname = getOne(Wrappers.<ApUserRealname>lambdaQuery().eq(ApUserRealname::getId, dto.getId()));
        if (null == apUserRealname) {

            log.error("待审核 实名认证信息不存在   userRealnameId:{}");
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (!AdminConstants.WAIT_AUTH.equals(apUserRealname.getStatus())) {
            log.error("实名认证信息非待审核状态   userRealnameId:{}");
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_ALLOW);
        }
        ApUser apUser = apUserMapper.selectById(apUserRealname.getUserId());
        if (apUser == null) {
            throw new CustomException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3 更新认证用户信息
        apUserRealname.setStatus(dto.getStatus());
        if (StringUtils.isNotBlank(dto.getMsg())) {
            apUserRealname.setReason(dto.getMsg());
        }
        updateById(apUserRealname);
        if (AdminConstants.PASS_AUTH.equals(status)) {
            WmUser wmUser = createWmUser(apUser);
            createApAuthor(apUser, wmUser);
        }
    return ResponseResult.okResult();

}


    /**
     * 4.2 创建作者信息
     * @param wmUser
     * @return
     */
    private void createApAuthor(ApUser apUser, WmUser wmUser){
        ResponseResult<ApAuthor> apAuthorResult = articleFeign.findByUserId(wmUser.getApUserId());
        if (!apAuthorResult.checkCode()){
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,apAuthorResult.getErrorMessage());
        }
        ApAuthor author = apAuthorResult.getData();


        if (author != null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"作者信息已存在");
        }
        author = new ApAuthor();
        author.setName(apUser.getName());
        author.setType(2);
        author.setUserId(apUser.getId());
        author.setCreatedTime(new Date());
        author.setWmUserId(wmUser.getId());
        ResponseResult result = articleFeign.save(author);
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,result.getErrorMessage());
        }
    }




    /**
     * 4.1 创建自媒体账户
     * @param dto
     * @param apUser  APP端用户
     * @return
     */
    private WmUser createWmUser(ApUser apUser){

//1 查询自媒体账号是否存在（APP端用户密码和自媒体密码一致）
        ResponseResult<WmUser> result = wemediaFeign.findByname(apUser.getName());
        if(result.checkCode() !=true){
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        WmUser wmUser =result.getData();
//        如果存在，抛异常
        if (wmUser != null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"自媒体用户信息已存在");
        }
//        如果不存在，基于appUser创建自媒体账户
        wmUser = new WmUser();
        wmUser.setApUserId(apUser.getId());
        wmUser.setName(apUser.getName());
        wmUser.setPassword(apUser.getPassword());
        wmUser.setSalt(apUser.getSalt());
        wmUser.setImage(apUser.getImage());
        wmUser.setPhone(apUser.getPhone());
        wmUser.setStatus(9);
        wmUser.setType(0);
        wmUser.setScore(0);
        wmUser.setCreatedTime(new Date());
        ResponseResult<WmUser> wmUserResponseResult = wemediaFeign.save(wmUser);
        if(!wmUserResponseResult.checkCode()){
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,wmUserResponseResult.getErrorMessage());
        }
        return wmUserResponseResult.getData();

    }

}
