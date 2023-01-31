package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.CrawConstant;
import com.back.common.constant.WXConstant;
import com.back.common.craw.CrawUtil;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.vo.ReviewDataVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 复盘数据操作类
 */
@Component
public class ReviewDataHandler extends HandlerAdapter {


    @Override
    public String handler(String openId, String handler) {
        //回复模板
        String resContent = WXConstant.REVIEW_DATA_TEXT;
        ReviewDataVo vo = (ReviewDataVo) CrawUtil.dayReviewMap.get(CrawConstant.REVIEW);
        String str = vo.show();
        return str+"\n";
    }
}
