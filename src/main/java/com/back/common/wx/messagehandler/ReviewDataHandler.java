package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
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
@Component("reviewDataHandler")
public class ReviewDataHandler implements WxMessageHandler{


    @Override
    public String handler(String openId, String handler) {
        //回复模板
        String resContent = TextMessageUtil.handMap.get(handler).getContent();
        //准备填充数据
        List<String> fillList = new ArrayList<>();
        ReviewDataVo data = CrawUtil.getReviewData();
        /** 历史新高数 */
        fillList.add(String.valueOf(data.getHistoryHigh().size()));
        /** 一年新高数 */
        fillList.add(String.valueOf(data.getYearHigh().size()));
        /** 一年新低数 */
        fillList.add(String.valueOf(data.getYearLow().size()));
        /** 跌停数 */
        fillList.add(String.valueOf(data.getDownLimit().size()));
        /** 跌停超5% */
        fillList.add(String.valueOf(data.getDownFive()));
        /** 涨停数 */
        fillList.add(String.valueOf(data.getUpLimit().size()));
        /** 今日非一字涨停数 */
        fillList.add(String.valueOf(data.getNoOneUp().size()));
        /** 涨停超5% */
        fillList.add(String.valueOf(data.getUpFive()));
        /** 上涨家数 */
        fillList.add(String.valueOf(data.getUpAll()));
        /** 上证指数 */
        fillList.add(String.valueOf(data.getSZ_INDEX()));
        /** 创业板指 */
        fillList.add(String.valueOf(data.getBusiness_INDEX()));
        /** 成交额 */
        fillList.add(String.valueOf(data.getTurnOver()));
        String str = TextMessageUtil.fillMessage(resContent,fillList );
        return str+"\n";
    }
}
