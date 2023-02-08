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
@Component
public class PushHandler extends HandlerAdapter {

    @Resource
    private  MystockMapper mystockMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openId,String content) {
        if (content.equals(WXConstant.HANDLER_PUSH)){
            //回复模板
            String resContent = WXConstant.PUSH_TEXT;
            //自选股
            List<Mystock> list = mystockMapper.getByOpenId(openId);
            if (list == null || list.size() == CommonConstant.ZERO)
            //没有自选股
            if(list == null || list.size() == CommonConstant.ZERO){
                return WXConstant.WX_NO_MY_STOCK;
            }
            StringBuilder resBulider = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                StockPushVo crawRes = CrawUtil.getOneBySinA(list.get(i).getStockcode());
                if (crawRes == null){
                    return WXConstant.WX_FAIl_HANDLER;
                }
                resBulider.append(crawRes.show()+"\n");
            }
            return resBulider.toString();
        }else {
            return WXConstant.WX_FAIL_CONTENT;
        }
    }

}
