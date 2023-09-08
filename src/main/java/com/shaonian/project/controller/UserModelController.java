package com.shaonian.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.annotation.AuthCheck;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.DeleteRequest;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.constant.UserConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.es.UserChat;
import com.shaonian.project.model.vo.UserChatVO;
import com.shaonian.project.model.vo.UserModelVO;
import com.shaonian.project.service.UserModelService;
import com.shaonian.project.service.UserService;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/userModel")
public class UserModelController {

    @Resource
    private UserModelService userModelService;

    @Resource
    private UserService userService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteChatModel(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserModel oldUserModel = userModelService.getById(id);
        if (oldUserModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserModel.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userModelService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 分页获取用户对话记录
     *
     * @param userModelQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserModelVO>> listUserModelVOByPage(@RequestBody UserModelQueryRequest userModelQueryRequest) {
        if (userModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userModelQueryRequest.getCurrent();
        long pageSize = userModelQueryRequest.getPageSize();
//         限制爬虫
        if (pageSize > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<UserModelVO> userModelVoPage = userModelService.pageUserModelVO(new Page<>(current, pageSize), userModelQueryRequest);
        return ResultUtils.success(userModelVoPage);
    }

    /**
     * 获取用户对话记录
     *
     * @param userModelQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<UserModel>> listUserModelByPage(@RequestBody UserModelQueryRequest userModelQueryRequest) {
        if (userModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userModelQueryRequest.getCurrent();
        long pageSize = userModelQueryRequest.getPageSize();
        Long userId = userModelQueryRequest.getUserId();
        Long modelId = userModelQueryRequest.getModelId();
//         限制爬虫
        if (pageSize > 1000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<UserModel> queryWrapper = new QueryWrapper<>();
        if (userId == null || modelId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("modelId", modelId);
        queryWrapper.isNotNull("genResult");
        queryWrapper.orderByDesc("createTime");
        Page<UserModel> userModelVoPage = userModelService.page(new Page<>(current, pageSize), queryWrapper);
        List<UserModel> collect = userModelVoPage.getRecords()
                .stream()
                .sorted(Comparator.comparing(UserModel::getCreateTime)).collect(Collectors.toList());
        userModelVoPage.setRecords(collect);
        return ResultUtils.success(userModelVoPage);
    }

    /**
     * 词频统计
     * @return
     */
    @GetMapping("/wordCount")
    public BaseResponse wordCount(){
        //根据用户对话聚合
        TermsAggregationBuilder chatDataAgg = AggregationBuilders.terms("chatDataAgg")
                .field("chatData")
                .size(2000);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withAggregations(chatDataAgg)
                .build();

        SearchHits<UserChat> search = elasticsearchRestTemplate.search(build, UserChat.class);
        Aggregations aggregations = (Aggregations) search.getAggregations().aggregations();
        Terms chatDataAgg1 = aggregations.get("chatDataAgg");
        List<? extends Terms.Bucket> buckets = chatDataAgg1.getBuckets();
        List<UserChatVO> collect = buckets.stream().map((bucket) -> {
            String keyAsString = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            return new UserChatVO(keyAsString, docCount);
        }).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }
}
