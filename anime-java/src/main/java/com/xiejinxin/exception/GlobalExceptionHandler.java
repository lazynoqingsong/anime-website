package com.xiejinxin.exception;

import com.xiejinxin.common.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理客户端中断连接异常
     */
    @ExceptionHandler({AsyncRequestNotUsableException.class, ClientAbortException.class})
    public ResponseEntity<Void> handleClientAbort(Exception e) {
        logger.warn("客户端中断连接: {}", e.getMessage());
        return ResponseEntity.noContent().build();
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result handleCustomException(CustomException e) {
        logger.error("自定义异常: {}", e.getMessage());
        return Result.error(e.getMsg());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        logger.error("服务器异常: ", e);
        return Result.error("服务器内部错误: " + e.getMessage());
    }
}
