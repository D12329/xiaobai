package org.example.demo01.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success;
    private String message;
    private T data;
    private Integer code;
    private long timestamp;

    private Result(boolean success, String message, T data, Integer code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return new Result<>(true, "Operation successful", null, 200);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, "Operation successful", data, 200);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(false, message, null, 500);
    }

    public static <T> Result<T> successWithData(T data) {
        return new Result<>(true, "Operation successful", data, 200);
    }

    public static <T> Result<T> errorWithMsg(String message, Integer code) {
        return new Result<>(false, message, null, code);
    }

    public static <T> Result<T> errorWithMsg(String message) {
        return new Result<>(false, message, null, 500);
    }
}
