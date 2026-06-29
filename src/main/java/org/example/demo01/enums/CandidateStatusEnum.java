package org.example.demo01.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 候选人状态枚举
 */
@Getter
@AllArgsConstructor
public enum CandidateStatusEnum {
    PENDING(1, "待筛选"),
    SCREENING(2, "筛选中"),
    INTERVIEWING(3, "面试中"),
    OFFERED(4, "已录用"),
    ONBOARDED(5, "已入职"),
    REJECTED(6, "已拒绝");

    private final Integer code;
    private final String text;

    public static String getText(Integer code) {
        if (code == null) return "未知";
        for (CandidateStatusEnum e : values()) {
            if (e.code.equals(code)) return e.text;
        }
        return "未知";
    }
}
