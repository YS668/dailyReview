package com.back.service.impl;

import java.util.List;
import java.util.Map;

import com.back.common.Result;
import com.back.entity.pojo.Mystock;
import com.back.entity.req.MyStockReq;
import com.back.entity.vo.MyStockVo;
import com.back.mapper.MystockMapper;
import com.back.service.MystockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自选股表 服务实现类
 * </p>
 *
 * @author DR-back
 * @since 2023-02-16
 */
@Service
public class MystockServiceImpl extends ServiceImpl<MystockMapper, Mystock> implements MystockService {

	@Autowired
	private MystockMapper mystockMapper;

	/**
	 *  批量删除
	 * @param param
	 *        <股票名称，分组>
	 * @return
	 */
	@Override
	public Result removeBatch(List<MyStockReq> param) {
		int row = mystockMapper.removeBatch(param);
		if (row > 0 ){
			return Result.suc();
		}else {
			return Result.fail();
		}
	}

	/**
	 * 换组
	 * @param param
	 * @return
	 */
	@Override
	public Result changeGroup(List<MyStockReq> param) {
		int row = mystockMapper.changeGroup(param);
		if (row > 0 ){
			return Result.suc();
		}else {
			return Result.fail();
		}
	}


	/**
	 * 修改备注
	 * @param param
	 * @return
	 */
	@Override
	public Result updateNote(MyStockReq param) {
		int row = mystockMapper.updateNote(param);
		if (row > 0 ){
			return Result.suc();
		}else {
			return Result.fail();
		}
	}

	@Override
	public Map<String,String> getOneByUidStockNameGroupName(Long uid,String stockName,String groupName) {
		return mystockMapper.getOneByUidStockNameGroupName(uid,stockName,groupName);
	}
}
