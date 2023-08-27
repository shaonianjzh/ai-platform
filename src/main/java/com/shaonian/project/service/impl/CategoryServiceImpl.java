package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.model.entity.Category;
import com.shaonian.project.service.CategoryService;
import com.shaonian.project.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 少年
* @description 针对表【category】的数据库操作Service实现
* @createDate 2023-08-27 12:04:29
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




