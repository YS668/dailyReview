package com.back.controller.WX;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.back.common.craw.CrawUtil;
import com.back.common.wx.Wxutils.EchostrCheckUtil;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.StockPushVo;
import com.back.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class WxController {

    @Resource
    private MessageService messageService;

    /**
     * 微信验证接口
     * @param request
     * @return
     */
    @GetMapping("/wx")
    public String verifyToken(HttpServletRequest request){
        return EchostrCheckUtil.checkSignature(request);
    }

    /**
     * 回复微信消息
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/wx")
    public String answerMessage(HttpServletRequest request, HttpServletResponse response){
        return messageService.answerMessage(request,response);
    }


    @GetMapping("/test")
    public String test(HttpServletRequest request){
        ReviewDataVo vo = new ReviewDataVo();
        vo.setHistoryHigh(CrawUtil.getHistoryHigh().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setYearHigh(CrawUtil.getYearHigh().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setYearLow(CrawUtil.getYearLow().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setDownLimit(CrawUtil.getDownLimit().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setUpLimit(CrawUtil.getUpLimit().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setNoOneUp(CrawUtil.getNoOneUp().stream().collect(Collectors.toMap(StockPushVo::getStockCode,i -> i)));
        vo.setDownFive(CrawUtil.getDownFive());
        vo.setUpFive(CrawUtil.getUpFive());
        vo.setTurnOver(CrawUtil.getTurnOver());
        vo.setSZ_INDEX(CrawUtil.getSzIndex());
        vo.setBusiness_INDEX(CrawUtil.getBusIndex());
        vo.setUpAll(CrawUtil.getUpAllToDay());
        
        int upAllToNineTwentyFive = CrawUtil.getUpAllToNineTwentyFive();
        int upAllToTen = CrawUtil.getUpAllToTen();
        int upAllToElevenThirty = CrawUtil.getUpAllToElevenThirty();
        int upAllToFourteen = CrawUtil.getUpAllToFourteen();
        return vo.show();
    }

}
