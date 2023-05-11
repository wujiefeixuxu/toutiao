package com.heima.feigns;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.ArticleFeignFallback;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.HTMLDocument;

@FeignClient(value = "leadnews-article",
fallbackFactory = ArticleFeignFallback.class,
configuration = HeimaFeignAutoConfiguration.class)
public interface  ArticleFeign {
    @GetMapping("/api/v1/author/findByUserId/{userId}")
    ResponseResult findByUserId (@PathVariable("userId")Integer userId);

    @PostMapping("/api/v1/author/save")
     ResponseResult save(@RequestBody ApAuthor apAuthor) ;
}
