package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import org.cetide.hibiscus.domain.model.aggregate.OrgInvite;
import org.cetide.hibiscus.domain.service.OrgInviteService;
import org.cetide.hibiscus.common.request.PageRequest;
import org.cetide.hibiscus.common.response.ApiResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrgInvite 控制器，提供基础增删改查接口
 * @author Hibiscus-code-generate
 */
@Api(tags = "OrgInvite 控制器")
@RestController
@RequestMapping("/api/org_invite")
public class OrgInviteController {

    private final OrgInviteService orgInviteService;

    public OrgInviteController(OrgInviteService orgInviteService) {
        this.orgInviteService = orgInviteService;
    }

    /**
     * 新增 OrgInvite 记录
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody OrgInvite entity) {
        return ApiResponse.success(orgInviteService.save(entity));
    }

    /**
     * 更新 OrgInvite 记录
     * @param entity 实体对象（必须包含主键 ID）
     * @return 是否更新成功
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody OrgInvite entity) {
        return ApiResponse.success(orgInviteService.updateById(entity));
    }

    /**
     * 删除指定 ID 的 OrgInvite 记录
     * @param id 主键 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Integer id) {
        return ApiResponse.success(orgInviteService.removeById(id));
    }

    /**
     * 根据 ID 获取 OrgInvite 详情
     * @param id 主键 ID
     * @return 匹配的实体对象
     */
    @GetMapping("/{id}")
    public ApiResponse<OrgInvite> getById(@PathVariable("id") Integer id) {
        return ApiResponse.success(orgInviteService.getById(id));
    }

    /**
     * 获取所有 OrgInvite 列表（不分页）
     * @return 实体列表
     */
    @GetMapping
    public ApiResponse<List<OrgInvite>> list() {
        return ApiResponse.success(orgInviteService.list());
    }

    /**
     * 分页查询 OrgInvite 列表
     * 支持关键字模糊搜索与排序
     * @param pageRequest 分页与筛选请求参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ApiResponse<Page<OrgInvite>> getPage(@RequestBody PageRequest pageRequest) {
        Page<OrgInvite> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        QueryWrapper<OrgInvite> wrapper = new QueryWrapper<>();

        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            wrapper.like("name", pageRequest.getKeyword()); // 可自定义字段
        }

        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(pageRequest.getSortOrder()), pageRequest.getSortBy());
        }

        return ApiResponse.success(orgInviteService.page(page, wrapper));
    }

}
