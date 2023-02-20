package com.back.service;

import java.util.List;
import java.util.Map;

import com.back.common.Result;
import com.back.entity.pojo.Mystock;
import com.back.entity.req.MyStockReq;
import com.back.entity.vo.MyStockVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自选股表 服务类
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
public interface MystockService extends IService<Mystock> {

    Result removeBatch(List<MyStockReq> param);

    Result changeGroup(List<MyStockReq> param);

    Result updateNote(MyStockReq param);

    Map<String,String> getOneByUidStockNameGroupName(Long uid,String stockName,String groupName);

}
