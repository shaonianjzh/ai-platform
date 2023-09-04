package com.shaonian.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.annotation.AuthCheck;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.constant.UserConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.exception.ThrowUtil;
import com.shaonian.project.model.dto.payorder.PayOrderQueryRequest;
import com.shaonian.project.model.vo.PayOrderVO;
import com.shaonian.project.service.PayOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/payOrder")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;


    /**
     * 分页查询订单信息
     * @param payOrderQueryRequest
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page")
    public BaseResponse<Page<PayOrderVO>> listUserByPage(@RequestBody PayOrderQueryRequest payOrderQueryRequest, HttpServletRequest request) {
        if (payOrderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = payOrderQueryRequest.getCurrent();
        long size = payOrderQueryRequest.getPageSize();

        //防止爬虫
        ThrowUtil.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<PayOrderVO> payOrderVOPage = payOrderService.pagePayOrderVO(new Page<>(current,size),payOrderQueryRequest);
        return ResultUtils.success(payOrderVOPage);
    }
}
