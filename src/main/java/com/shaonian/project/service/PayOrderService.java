package com.shaonian.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.model.dto.payorder.PayOrderQueryRequest;
import com.shaonian.project.model.entity.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.model.vo.PayOrderVO;

/**
* @author 少年
* @description 针对表【pay_order】的数据库操作Service
* @createDate 2023-09-03 19:33:02
*/
public interface PayOrderService extends IService<PayOrder> {

    /**
     * 分页获取订单信息
     * @param page
     * @param payOrderQueryRequest
     * @return
     */
    Page<PayOrderVO> pagePayOrderVO(Page<PayOrderVO> page, PayOrderQueryRequest payOrderQueryRequest);
}
