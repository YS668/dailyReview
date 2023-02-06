package com.back.common.wx.messagehandler;


import com.back.common.constant.WXConstant;
import com.back.common.craw.CrawUtil;
import org.springframework.stereotype.Component;

/**
 * 复盘数据操作类
 */
@Component
public class ReviewDataHandler extends HandlerAdapter {


    @Override
    public String handler(String openId, String handler) {
        //回复模板
        String resContent = WXConstant.REVIEW_DATA_TEXT;
        return CrawUtil.vo.show();
    }

}
