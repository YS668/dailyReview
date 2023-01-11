package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.craw.CrawUtil;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.vo.StockPushVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看股票
 */
@Component("watchHandler")
public class WatchHandler implements WxMessageHandler{


    @Override
    public String handler(String openId,String content) {
        //content：查看 股票名称 股票名称
        String[] split = content.split(" ");
        if (WXConstant.HANDLER_PLAN.equals(split[CommonConstant.ZERO])){
            //回复模板
            String resContent = TextMessageUtil.handMap.get(content).getContent();
            //查看的股票名称
            List<String> list = new ArrayList<>();
            for (int i = 1; i < split.length; i++) {
                list.add(split[i]);
            }
            //开始填充
            StringBuilder resBulider = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                //填充值，第一个为股票名称，第二个为股票涨跌，第三个为现价，第四个为成交额，第五个为雪球链接
                List<String> fillList = new ArrayList<>();
                //股票名称/股票代码转换器
                StockPushVo crawRes = CrawUtil.getOne(list.get(i));
                if (crawRes == null){
                    return WXConstant.WX_FAIl_HANDLER;
                }
                /** 股票名称 */
                fillList.add(crawRes.getStockName());
                /** 股票涨跌 */
                fillList.add(crawRes.getTrend());
                /** 现价 */
                fillList.add(crawRes.getNowPrice());
                /** 成交额 */
                fillList.add(crawRes.getTurnover());
                /** 雪球链接 */
                fillList.add(crawRes.getXueQiuLink());
                String str = TextMessageUtil.fillMessage(resContent,fillList );
                resBulider.append(str+"\n");
            }
            return resBulider.toString();
        }else {
            return WXConstant.WX_FAIL_CONTENT;
        }
    }
}
