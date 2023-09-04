package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.payorder.PayOrderQueryRequest;
import com.shaonian.project.model.entity.PayOrder;
import com.shaonian.project.model.vo.PayOrderVO;
import com.shaonian.project.service.PayOrderService;
import com.shaonian.project.mapper.PayOrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 少年
* @description 针对表【pay_order】的数据库操作Service实现
* @createDate 2023-09-03 19:33:02
*/
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder>
    implements PayOrderService{

    @Resource
    private PayOrderMapper payOrderMapper;

    @Override
    public Page<PayOrderVO> pagePayOrderVO(Page<PayOrderVO> page, PayOrderQueryRequest payOrderQueryRequest) {
        if(payOrderQueryRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return page.setRecords(payOrderMapper.pagePayOrderVO(page,payOrderQueryRequest.getUserAccount()));
    }
}




