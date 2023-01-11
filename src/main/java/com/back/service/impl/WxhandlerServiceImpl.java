package com.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.back.common.Result;
import com.back.common.wx.Wxutils.TextMessageUtil;
import com.back.entity.pojo.Wxhandler;
import com.back.mapper.WxhandlerMapper;
import com.back.service.WxhandlerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        log.info("添加微信操作类：{}", JSON.toJSON(wxhandler));
        int insert = wxhandlerMapper.insert(wxhandler);
        TextMessageUtil.handMap.put(wxhandler.getKeywords(),wxhandler);
        log.info("增加指令后，微信指令：{}",JSON.toJSON(TextMessageUtil.handMap));
        return Result.suc(insert);
    }

    /**
     * 更新微信操作
     * @param wxhandler
     * @return
     */
    @Override
    public Result updateByKeyWords(Wxhandler wxhandler) {
        log.info("更新微信指令：{}", JSON.toJSON(wxhandler));
        LambdaQueryWrapper<Wxhandler> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Wxhandler::getKeywords,wxhandler.getKeywords());
        int update = wxhandlerMapper.update(wxhandler, queryWrapper);
        TextMessageUtil.handMap.put(wxhandler.getKeywords(),wxhandler);
        log.info("更新微信指令：{}",JSON.toJSON(TextMessageUtil.handMap));
        return Result.suc(update);
    }

    /**
     * 删除微信指令
     * @param keyWords
     * @return
     */
    @Override
    public Result del(String keyWords) {
        log.info("删除微信指令：{}", keyWords);
        LambdaQueryWrapper<Wxhandler> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Wxhandler::getKeywords,keyWords);
        int del = wxhandlerMapper.delete(queryWrapper);
        TextMessageUtil.handMap.remove(keyWords);
        log.info("删除后的微信指令：{}",JSON.toJSON(TextMessageUtil.handMap));
        return Result.suc(del);
    }

    /**
     *  分页查询
     * @param keywords
     * @param channel
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result getPage(String keywords, String channel,int pageNum, int pageSize) {
        log.info("分页查询微信指令，keywords：{}，channel：{}，pageNum：{}，pageSize：{}", keywords,channel,pageNum,pageSize);
        PageHelper.startPage(pageNum,pageSize);
        List<Wxhandler> list= wxhandlerMapper.page(keywords,channel);
        PageInfo<Wxhandler> pageInfo = new PageInfo<>(list);
        log.info("分页查询微信指令结果",JSON.toJSON(pageInfo));
        return Result.suc(pageInfo);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public Result getAll() {
        log.info("查询所有的微信指令");
        List<Wxhandler> all = wxhandlerMapper.getAll();
        log.info("查询所有的微信指令结果：{}",all);
        return Result.suc(all);
    }
}
