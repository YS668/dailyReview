package com.back.common.wx.messagehandler;

import lombok.extern.slf4j.Slf4j;

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
@Component
@Slf4j
public class WatchHandler extends HandlerAdapter {

    @Override
    public String handler(String openId,String content) {
        //content：查看 股票名称 股票名称
        String[] split = content.split(" ");
        if (split.length < 2){
            return WXConstant.WX_NO_WATCH;
        }
        if (WXConstant.HANDLER_WATCH.equals(split[CommonConstant.ZERO])){
            //回复模板
            String resContent =  WXConstant.WATCH_TEXT;
            //查看的正确股票代码
            List<String> codeList = new ArrayList<>();
            //查看的错误输入
            List<String> mistakeList = new ArrayList<>();
            for (int i = 1; i < split.length; i++) {
                String str = split[i];
                //判断输入是否正确
                //输入的是代码
                log.info("开始处理查看的股票：{}",str);
                if (CrawUtil.StockCodeMap.containsKey(str)){
                    codeList.add(str);
                    continue;
                }
                //输入名称
                if (CrawUtil.StockNameMap.containsKey(str)){
                    String stockCode = CrawUtil.StockNameMap.get(str).getStockCode();
                    codeList.add(stockCode);
                    continue;
                }
                //输入不正确的
                mistakeList.add(str);
            }
            StringBuilder resBulider = new StringBuilder();
            for (int i = 0; i < codeList.size(); i++) {
                String stockCode = codeList.get(i);
                //股票名称/股票代码转换器
                StockPushVo crawRes = CrawUtil.getOne(stockCode);
                if (crawRes == null){
                    mistakeList.add(stockCode);
                    continue;
                }
                resBulider.append(crawRes.show()+"\n");
            }
            //最后加上不正确的提示
            for (String mistake : mistakeList) {
                resBulider.append(mistake+"无法查找，请检查是否正确"+"\n");
            }
            return resBulider.toString();
        }else {
            return WXConstant.WX_FAIL_CONTENT;
        }
    }
}
