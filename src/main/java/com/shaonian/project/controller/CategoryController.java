package com.shaonian.project.controller;

import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.model.entity.Category;
import com.shaonian.project.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    /**
     * 获取模型分类列表
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Category>> getCategory(){
        List<Category> list = categoryService.list();
        return ResultUtils.success(list);
    }

}
