package org.example.demo01.common;

public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
