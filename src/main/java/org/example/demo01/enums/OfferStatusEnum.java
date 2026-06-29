package org.example.demo01.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Offer状态枚举
 */
@Getter
@AllArgsConstructor
public enum OfferStatusEnum {
    DRAFT("draft", "草稿"),
    PENDING("pending", "待确认"),
    SENT("sent", "待确认"),
    ACCEPTED("accepted", "已接受"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String text;

    public static String getText(String code) {
        if (code == null) return "未知";
        for (OfferStatusEnum e : values()) {
            if (e.code.equals(code)) return e.text;
        }
        return "未知";
    }
}
