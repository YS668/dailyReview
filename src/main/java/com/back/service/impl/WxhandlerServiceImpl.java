package com.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.back.common.Result;
import com.back.common.utils.Wxutils.TextMessageUtil;
import com.back.entity.pojo.Wxhandler;
import com.back.mapper.WxhandlerMapper;
import com.back.service.WxhandlerService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 微信回复消息操作表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
@Service("xhandlerService")
@Slf4j
public class WxhandlerServiceImpl extends ServiceImpl<WxhandlerMapper, Wxhandler> implements WxhandlerService {

    @Resource
    private WxhandlerMapper wxhandlerMapper;

    /**
     * 添加微信操作
     * @param wxhandler
     * @return
     */
    @Override
    public Result add(Wxhandler wxhandler) {
        log.info("添加微信操作类：", JSON.toJSON(wxhandler));
        int insert = wxhandlerMapper.insert(wxhandler);
        TextMessageUtil.handMap.put(wxhandler.getKeywords(),wxhandler);
        log.info("微信指令：{}",JSON.toJSON(TextMessageUtil.handMap));
        return Result.suc(insert);
    }

    /**
     * 更新微信操作
     * @param wxhandler
     * @return
     */
    @Override
    public Result updateByKeyWords(Wxhandler wxhandler) {
        log.info("添加微信操作类：", JSON.toJSON(wxhandler));
        LambdaQueryWrapper<Wxhandler> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Wxhandler::getKeywords,wxhandler.getKeywords());
        int update = wxhandlerMapper.update(wxhandler, queryWrapper);
        TextMessageUtil.handMap.put(wxhandler.getKeywords(),wxhandler);
        log.info("微信指令：{}",JSON.toJSON(TextMessageUtil.handMap));
        return Result.suc(update);
    }
}
