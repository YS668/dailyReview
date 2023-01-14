package com.back.controller.WX;

import java.math.BigDecimal;
import java.util.List;

import com.back.common.craw.CrawUtil;
import com.back.common.wx.Wxutils.EchostrCheckUtil;
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
    public void test(HttpServletRequest request){
        List<StockPushVo> historyHigh = CrawUtil.getHistoryHigh();
        List<StockPushVo> yearHigh = CrawUtil.getYearHigh();
        List<StockPushVo> yearLow = CrawUtil.getYearLow();
        List<StockPushVo> downLimit = CrawUtil.getDownLimit();
        Integer downFive = CrawUtil.getDownFive();
        List<StockPushVo> upLimit = CrawUtil.getUpLimit();
        Integer upFive = CrawUtil.getUpFive();
        List<StockPushVo> noOneUp = CrawUtil.getNoOneUp();
        int upAllToNineTwentyFive = CrawUtil.getUpAllToNineTwentyFive();
        int upAllToTen = CrawUtil.getUpAllToTen();
        int upAllToElevenThirty = CrawUtil.getUpAllToElevenThirty();
        int upAllToFourteen = CrawUtil.getUpAllToFourteen();
        int upAll = CrawUtil.getUpAllToDay();
        String turnOver = CrawUtil.getTurnOver();
        String szIndex = CrawUtil.getSzIndex();
        String busIndex = CrawUtil.getBusIndex();
    }

}
