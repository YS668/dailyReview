package com.back.controller.WX;

import com.back.common.utils.Wxutils.EchostrCheckUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WxController {

    /**
     * 微信验证接口
     * @param request
     * @return
     */
    @GetMapping("/wx")
    public String verifyToken(HttpServletRequest request){
        return EchostrCheckUtil.checkSignature(request);
    }

}
