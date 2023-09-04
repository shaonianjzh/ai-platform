package com.shaonian.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.model.entity.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shaonian.project.model.vo.PayOrderVO;

import java.util.List;

/**
* @author 少年
* @description 针对表【pay_order】的数据库操作Mapper
* @createDate 2023-09-03 19:33:02
* @Entity com.shaonian.project.model.entity.PayOrder
*/
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    List<PayOrderVO> pagePayOrderVO(Page<PayOrderVO> page, String userAccount);

}




