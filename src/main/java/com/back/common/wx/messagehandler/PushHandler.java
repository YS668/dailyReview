package com.back.common.wx.messagehandler;

import com.back.common.constant.CommonConstant;
import com.back.common.constant.WXConstant;
import com.back.common.craw.CrawUtil;
import com.back.entity.pojo.Mystock;
import com.back.entity.vo.StockPushVo;
import com.back.mapper.UserMapper;
import org.springframework.stereotype.Component;

import com.back.service.MystockService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import javax.annotation.Resource;
import java.util.List;

/**
 * 推送自选股
 */
@Component
public class PushHandler extends HandlerAdapter {

    @Resource
    private MystockService mystockService;
    @Resource
    private UserMapper userMapper;

    @Override
    public String handler(String openId,String content) {
        if (content.equals(WXConstant.HANDLER_PUSH)){
            //回复模板
            String resContent = WXConstant.PUSH_TEXT;
            //自选股
            LambdaQueryWrapper<Mystock> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Mystock::getOpenid,openId);
            List<Mystock> list = mystockService.list(wrapper);
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
