package org.cetide.hibiscus.common.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.cetide.hibiscus.common.anno.Loggable;
import org.cetide.hibiscus.common.context.RequestContext;
import org.cetide.hibiscus.domain.model.aggregate.OperationLog;
import org.cetide.hibiscus.domain.model.valueobject.Description;
import org.cetide.hibiscus.domain.model.valueobject.Operator;
import org.cetide.hibiscus.domain.service.OperationLogService;
import org.cetide.hibiscus.common.utils.JsonUtils;
import org.springframework.stereotype.Component;

import static org.cetide.hibiscus.common.constants.UserConstants.*;

/**
 * 操作日志切面
 * 用于拦截被 @Loggable 注解的方法，在方法执行成功或抛出异常后记录操作日志。
 * 记录的信息包括：操作类型、描述、操作人、是否成功、请求参数和返回结果。
 *
 * @author heathcetide
 */
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService logService;

    public OperationLogAspect(OperationLogService logService) {
        this.logService = logService;
    }

    /**
     * 成功返回后执行日志记录
     *
     * @param jp        切点（被拦截的方法信息）
     * @param loggable  注解中的日志配置
     * @param result    方法返回结果
     */
    @AfterReturning(pointcut = "@annotation(loggable)", returning = "result")
    public void logSuccess(JoinPoint jp, Loggable loggable, Object result) {
        saveLog(jp, loggable, result, null);
    }

    /**
     * 方法抛出异常后执行日志记录
     *
     * @param jp        切点
     * @param loggable  注解中的日志配置
     * @param ex        抛出的异常
     */
    @AfterThrowing(pointcut = "@annotation(loggable)", throwing = "ex")
    public void logError(JoinPoint jp, Loggable loggable, Exception ex) {
        saveLog(jp, loggable, null, ex);
    }

    /**
     * 核心日志保存方法
     *
     * @param jp        切点
     * @param loggable  注解中的元信息
     * @param result    方法返回值
     * @param ex        异常对象（如果有）
     */
    private void saveLog(JoinPoint jp, Loggable loggable, Object result, Exception ex) {
        OperationLog log = new OperationLog();
        log.setType(loggable.type());
        log.setDescription(new Description(loggable.value()));
        log.setOperator(new Operator(RequestContext.get(CURRENT_USERNAME, String.class, DEFAULT_USERNAME)));
        log.setSuccess(ex == null);
        log.setParams(JsonUtils.toJson(jp.getArgs()));
        log.setResult(JsonUtils.toJson(result));
        logService.save(log);
    }
} 