package org.cetide.hibiscus.interfaces.rest.controller;

import org.cetide.hibiscus.domain.model.aggregate.Organization;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cetide.hibiscus.domain.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Organization 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * 新增 Organization 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody Organization entity) {
        return ApiResponse.success(organizationService.save(entity));
    }

    /**
     * 更新 Organization 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Organization entity) {
        return ApiResponse.success(organizationService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 Organization 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationService.removeById(id));
    }

    /**
     * 根据 ID 获取 Organization 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<Organization> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(organizationService.getById(id));
    }

    /**
     * 获取所有 Organization 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<Organization>> list() {
        return ApiResponse.success(organizationService.list());
    }

    /**
     * 分页查询 Organization 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<Organization>> getPage(@RequestBody PageRequest pageRequest) {
        Page<Organization> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<Organization> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(organizationService.page(page, wrapper));
    }
}
