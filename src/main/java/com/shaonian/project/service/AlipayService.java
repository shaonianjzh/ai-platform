package com.shaonian.project.service;

import com.alipay.api.AlipayApiException;
import com.shaonian.project.model.dto.pay.PayRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author 少年
 */
public interface AlipayService {


    /**
     * 支付宝支付
     */
    String pay(PayRequest payRequest,HttpServletRequest request) throws AlipayApiException;


    /**
     * 支付同步回调
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    boolean syncNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException;

    /**
     * 支付异步回调
     * @param request
     * @return
     */
    String asyncNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException;
}
