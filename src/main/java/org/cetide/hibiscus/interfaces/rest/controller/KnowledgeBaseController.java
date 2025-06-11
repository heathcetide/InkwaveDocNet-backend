package org.cetide.hibiscus.interfaces.rest.controller;

import org.cetide.hibiscus.domain.model.aggregate.KnowledgeBase;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.domain.service.KnowledgeBaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KnowledgeBase 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/knowledgebase")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    /**
     * 新增 KnowledgeBase 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody KnowledgeBase entity) {
        return ApiResponse.success(knowledgeBaseService.save(entity));
    }

    /**
     * 更新 KnowledgeBase 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody KnowledgeBase entity) {
        return ApiResponse.success(knowledgeBaseService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 KnowledgeBase 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseService.removeById(id));
    }

    /**
     * 根据 ID 获取 KnowledgeBase 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<KnowledgeBase> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(knowledgeBaseService.getById(id));
    }

    /**
     * 获取所有 KnowledgeBase 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<KnowledgeBase>> list() {
        return ApiResponse.success(knowledgeBaseService.list());
    }

    /**
     * 分页查询 KnowledgeBase 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<KnowledgeBase>> getPage(@RequestBody PageRequest pageRequest) {
        Page<KnowledgeBase> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<KnowledgeBase> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(knowledgeBaseService.page(page, wrapper));
    }
}
