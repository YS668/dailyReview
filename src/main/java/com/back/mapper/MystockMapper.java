package com.back.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.back.entity.pojo.Mystock;
import com.back.entity.req.MyStockReq;
import com.back.entity.vo.MyStockVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 自选股表 Mapper 接口
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Mapper
public interface MystockMapper extends BaseMapper<Mystock> {


    int removeBatch(@Param("param") List<MyStockReq> param);

    int changeGroup(List<MyStockReq> param);

    int updateNote(@Param("val") MyStockReq param);

    Map<String,String> getOneByUidStockNameGroupName(@Param("uid") Long uid
            ,@Param("stockName") String stockName,@Param("groupName") String groupName);
}
