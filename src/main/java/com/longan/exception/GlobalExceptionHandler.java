package com.longan.exception;

import com.longan.result.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 2. 数据库唯一索引异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicate(DuplicateKeyException e) {
        return Result.error("数据重复 / 已存在");
    }

    /**
     * 3. 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValid(MethodArgumentNotValidException e) {

        String msg = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return Result.error(msg);
    }

    /**
     * 4. 兜底异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器内部错误");
    }
}
