package com.heima.user;
import java.util.Date;

import com.heima.feigns.ArticleFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeignTest {
    @Autowired
    WemediaFeign wemediaFeign;
    @Test
    public void findByName() {
        ResponseResult<WmUser> result = wemediaFeign.findByname("admin");
        System.out.println(result);
    }
    @Test
    public void save() {
        WmUser wmUser = new WmUser();
        wmUser.setApUserId(5555);
        wmUser.setName("666");
        wmUser.setPassword("666666");
        wmUser.setSalt("123456");
        wmUser.setNickname("7");
        wmUser.setImage("");
        wmUser.setLocation("");
        wmUser.setPhone("");
        wmUser.setStatus(0);
        wmUser.setEmail("");
        wmUser.setType(0);
        wmUser.setScore(0);
        wmUser.setLoginTime(new Date());
        wmUser.setCreatedTime(new Date());

        ResponseResult<WmUser> result = wemediaFeign.save(wmUser);
        System.out.println(result);
    }


    @Autowired
    ArticleFeign articleFeign;
    @Test
    public void find1() {
        ResponseResult<ApAuthor> id = articleFeign.findByUserId(4);
        ApAuthor apAuthor = id.getData();
        System.out.println(apAuthor);
    }
}
