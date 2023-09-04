package com.shaonian.project.service.impl;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.shaonian.project.config.AliPayConfig;
import com.shaonian.project.model.dto.pay.AlipayOrder;
import com.shaonian.project.model.dto.pay.PayRequest;
import com.shaonian.project.model.entity.PayOrder;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.model.enums.PayOrderStatusEnum;
import com.shaonian.project.model.enums.RechargeTypeEnum;
import com.shaonian.project.model.enums.UserRoleEnum;
import com.shaonian.project.service.AlipayService;
import com.shaonian.project.service.PayOrderService;
import com.shaonian.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.shaonian.project.model.enums.RechargeTypeEnum.*;

/**
 * @author 少年
 */
@Service
public class AlipayServiceImpl implements AlipayService {

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private UserService userService;

    @Resource
    private PayOrderService payOrderService;

    /**
     * 支付接口
     *
     * @author cc
     * @date 2021-12-01 15:54
     */
    @Override
    public String pay(PayRequest payRequest,HttpServletRequest request) throws AlipayApiException {
        // 支付宝网关
        String serverUrl = aliPayConfig.getGatewayUrl();
        // APPID
        String appId = aliPayConfig.getAppId();
        // 私钥
        String privateKey = aliPayConfig.getPrivateKey();
        // 格式化为 json 格式
        String format = "json";
        // 字符编码格式
        String charset = aliPayConfig.getCharset();
        // 公钥，对应APPID的那个
        String alipayPublicKey = aliPayConfig.getPublicKey();
        // 签名方式
        String signType = aliPayConfig.getSignType();
        //页面跳转同步通知页面路径
        String returnUrl = aliPayConfig.getReturnUrl();
        // 服务器异步通知页面路径
        String notifyUrl = aliPayConfig.getNotifyUrl();

        // 1. 初始化client
        AlipayClient client = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);

        // 2. 设置请求参数
        AlipayTradePagePayRequest tradeRequest = new AlipayTradePagePayRequest();
        tradeRequest.setReturnUrl(returnUrl);
        tradeRequest.setNotifyUrl(notifyUrl);


        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        AlipayOrder alipayOrder = new AlipayOrder();
        alipayOrder.setSubject(payRequest.getType()).setTotal_amount(BigDecimal.valueOf(payRequest.getMoney()))
                .setOut_trade_no(String.valueOf(id));
        System.out.println("JSON.toJSONString(order):" + JSON.toJSONString(alipayOrder));
        tradeRequest.setBizContent(JSON.toJSONString(alipayOrder));

        // 3. 调用支付并获取支付结果
        String result = client.pageExecute(tradeRequest).getBody();
        //生成订单
        PayOrder payOrder = new PayOrder();
        User loginUser = userService.getLoginUser(request);
        payOrder.setId(id).setUserId(loginUser.getId()).setPayMoney(BigDecimal.valueOf(payRequest.getMoney())).setType(payRequest.getType())
        .setStatus(PayOrderStatusEnum.UNPAID.getStatus());
        payOrderService.save(payOrder);
        return result;
    }

    /**
     * 支付同步通知回调校验
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @Override
    public boolean syncNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        return signVerified(request);
    }

    /**
     * 支付异步通知回调处理
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String asyncNotify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        boolean signVerified = signVerified(request);
        if (signVerified) { //验证成功
            // 商户订单号
            String id = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 支付宝交易号
            String tradeId = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 交易状态
            String status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            String endTime = new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"), "UTF-8");
            //异步通知，校验订单信息
            if (status.equals("TRADE_SUCCESS")) {
                // 付款完成后，支付宝系统发送该交易状态通知,更新订单信息
                PayOrder payOrder = new PayOrder();
                payOrder.setId(Long.parseLong(id)).setTradeId(tradeId)
                        .setStatus(PayOrderStatusEnum.PAID.getStatus())
                        .setEndTime(DateUtil.parse(endTime));
                payOrderService.updateById(payOrder);
                //根据充值类型 处理业务
                PayOrder order = payOrderService.getById(id);
                RechargeTypeEnum enumByType = getEnumByType(order.getType());

                User user = userService.getById(order.getUserId());
                user = getUserByType(enumByType,user);

                userService.updateById(user);
            }
            System.out.println("==========");
            System.out.println("out_trade_no: " + id);
            System.out.println("trade_no: " + tradeId);
            System.out.println("trade_status: " + status);
            return "success";
        }
        return "fail";
    }


    /**
     * SDK验证签名
     *
     * @param request
     * @return
     * @throws AlipayApiException
     */
    private boolean signVerified(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();

        System.out.println(requestParams);

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        System.out.println(params.toString());
        System.out.println(aliPayConfig.toString());
        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType());
        System.out.println("SDK验证签名结果1：" + signVerified);
        return signVerified;
    }

    private User getUserByType(RechargeTypeEnum type,User user){
        switch (type){
            case THREE_DAYS_MEMBER:
                user.setUserRole(UserRoleEnum.VIP_USER.getValue())
                        .setVipType(THREE_DAYS_MEMBER.getType())
                        .setExpireTime(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_MONTH,3));
                break;
            case MONTHLY_MEMBER:
                user.setUserRole(UserRoleEnum.VIP_USER.getValue())
                        .setVipType(MONTHLY_MEMBER.getType())
                        .setExpireTime(DateUtil.offset(DateUtil.date(), DateField.MONTH,1));
                break;
            case YEARLY_MEMBER:
                user.setUserRole(UserRoleEnum.VIP_USER.getValue())
                        .setVipType(YEARLY_MEMBER.getType())
                        .setExpireTime(DateUtil.offset(DateUtil.date(), DateField.YEAR,1));
                break;
            case ONE_HUNDRED_COUNT_RECHARGE:
                user.setCallNum(user.getCallNum()+100);
                break;
            case FIVE_HUNDRED_COUNT_RECHARGE:
                user.setCallNum(user.getCallNum()+500);
                break;
            case ONE_THOUSAND_COUNT_RECHARGE:
                user.setCallNum(user.getCallNum()+1000);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return user;
    }
}
