package com.back.service;

import com.back.common.Result;
import com.back.entity.pojo.Wxhandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 微信回复消息操作表 服务类
 * </p>
 *
 * @author DR-back
 * @since 2023-01-05
 */
public interface WxhandlerService extends IService<Wxhandler> {

    Result add(Wxhandler wxhandler);

    Result updateByKeyWords(Wxhandler wxhandler);
}
