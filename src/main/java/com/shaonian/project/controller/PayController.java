package com.shaonian.project.controller;

import com.alipay.api.AlipayApiException;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.pay.PayRequest;
import com.shaonian.project.model.enums.RechargeTypeEnum;
import com.shaonian.project.service.AlipayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private AlipayService alipayService;

    /**
     * 支付宝支付
     * @param payRequest
     * @param response
     * @throws AlipayApiException
     * @throws IOException
     */

    @GetMapping("/alipay")
    public void alipay(PayRequest payRequest,HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        String type = payRequest.getType();
        if(StringUtils.isBlank(type)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"充值类型错误");
        }
        RechargeTypeEnum enumByType = RechargeTypeEnum.getEnumByType(type);
        if(!enumByType.getValue().equals(payRequest.getMoney())){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"充值金额错误");
        }
        String form = alipayService.pay(payRequest,request);
        response.setContentType("text/html;charset=utf-8");
        // 直接将完整的表单html输出到页面
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
//        response.sendRedirect(form);
    }

    /**
     * 支付异步回调通知
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/notify/async")
    public String asyncNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        return alipayService.asyncNotify(request);
    }


    /**
     * 支付同步回调通知，重定向到商户
     * @param request
     */
    @GetMapping("/notify/sync")
    public void syncNotify(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        //校验成功，返回商户
        if(alipayService.syncNotify(request)){
            response.sendRedirect("http://localhost:7529/api/doc.html");
        }
    }

}
