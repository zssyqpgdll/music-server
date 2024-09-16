package com.zhang.recommendation_system.util;

/**
 * @Author: https://www.jianshu.com/p/e25b3c542553
 * @Description: 另外再创建一个上传文件成功之后的Response响应实体类UploadFileResponse.java
 *               及异常类FileException.java来处理异常信息。
 */
public class FileException extends RuntimeException{
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}