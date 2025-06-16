package org.cetide.hibiscus.interfaces.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cetide.hibiscus.common.response.ApiResponse;
import org.cetide.hibiscus.infrastructure.storage.FileStorageAdapter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器
 */
@Api(tags = "File 控制器")
@RestController
@RequestMapping("/api/files")
public class FileController {

    /**
     * 文件存储服务
     */
    private final FileStorageAdapter fileStorageAdapter;

    public FileController(FileStorageAdapter fileStorageAdapter) {
        this.fileStorageAdapter = fileStorageAdapter;
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageAdapter.upload(file);
        return ApiResponse.success(filename);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    @ApiOperation("下载文件")
    public ResponseEntity<byte[]> download(@RequestParam("path") String path) {
        byte[] content = fileStorageAdapter.download(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path)
                .body(content);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除文件")
    public ApiResponse<Void> delete(@RequestParam("path") String path) {
        fileStorageAdapter.delete(path);
        return ApiResponse.success();
    }

    /**
     * 文件是否存在
     */
    @GetMapping("/exists")
    @ApiOperation("文件是否存在")
    public ApiResponse<Boolean> exists(@RequestParam("path") String path) {
        return ApiResponse.success(fileStorageAdapter.exists(path));
    }

    /**
     * 获取文件访问地址
     */
    @GetMapping("/url")
    @ApiOperation("获取文件访问地址")
    public ApiResponse<String> getUrl(@RequestParam("path") String path) {
        return ApiResponse.success(fileStorageAdapter.getUrl(path));
    }

    /**
     * 获取文件大小
     */
    @GetMapping("/size")
    @ApiOperation("获取文件大小")
    public ApiResponse<Long> getSize(@RequestParam("path") String path) {
        return ApiResponse.success(fileStorageAdapter.getFileSize(path));
    }

    /**
     * 获取文件类型
     */
    @GetMapping("/type")
    @ApiOperation("获取文件类型")
    public ApiResponse<String> getContentType(@RequestParam("path") String path) {
        return ApiResponse.success(fileStorageAdapter.getContentType(path));
    }

    /**
     * 重命名文件
     */
    @PostMapping("/rename")
    @ApiOperation("重命名文件")
    public ApiResponse<String> rename(@RequestParam String oldPath, @RequestParam String newPath) {
        fileStorageAdapter.rename(oldPath, newPath);
        return ApiResponse.success("重命名成功");
    }

    /**
     * 复制文件
     */
    @PostMapping("/copy")
    @ApiOperation("复制文件")
    public ApiResponse<Void> copy(@RequestParam String sourcePath, @RequestParam String targetPath) {
        fileStorageAdapter.copy(sourcePath, targetPath);
        return ApiResponse.success();
    }

    /**
     * 移动文件
     */
    @PostMapping("/move")
    @ApiOperation("移动文件")
    public ApiResponse<Void> move(@RequestParam String sourcePath, @RequestParam String targetPath) {
        fileStorageAdapter.move(sourcePath, targetPath);
        return ApiResponse.success();
    }
}
