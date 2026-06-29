package org.example.demo01.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 面试状态枚举
 */
@Getter
@AllArgsConstructor
public enum InterviewStatusEnum {
    PENDING("pending", "待面试"),
    SCHEDULED("scheduled", "待面试"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String text;

    public static String getText(String code) {
        if (code == null) return "未知";
        for (InterviewStatusEnum e : values()) {
            if (e.code.equals(code)) return e.text;
        }
        return "未知";
    }
}
