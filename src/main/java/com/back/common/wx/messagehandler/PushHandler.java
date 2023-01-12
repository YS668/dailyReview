package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.craw.CrawUtil;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.pojo.Mystock;
import com.back.entity.vo.StockPushVo;
import com.back.mapper.MystockMapper;
import com.back.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 推送自选股
 */
@Component("pushHandler")
public class PushHandler implements WxMessageHandler{

    @Resource
    private  MystockMapper mystockMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openId,String content) {
        if (content.equals(WXConstant.HANDLER_PUSH)){
            //回复模板
            String resContent = TextMessageUtil.handMap.get(content).getContent();
            //自选股
            List<Mystock> list = mystockMapper.getByOpenId(openId);
            if (list == null || list.size() == CommonConstant.ZERO)
            //没有自选股
            if(list == null || list.size() == CommonConstant.ZERO){
                return WXConstant.WX_NO_MY_STOCK;
            }
            //开始填充
            StringBuilder resBulider = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                //填充值，第一个为股票名称，第二个为股票涨跌，第三个为现价，第四个为成交额，第五个为雪球链接
                List<String> fillList = new ArrayList<>();
                StockPushVo crawRes = CrawUtil.getOne(list.get(i).getStockcode());
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
