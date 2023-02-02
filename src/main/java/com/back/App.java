package com.back;

import com.alibaba.fastjson.JSON;
import com.back.common.constant.CrawConstant;
import com.back.common.wx.Wxutils.TextMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
        log.info("base_cd：{}",CrawConstant.BASE_CD);
    }
}
