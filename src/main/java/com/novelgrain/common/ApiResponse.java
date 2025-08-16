// src/main/java/com/novelgrain/common/ApiResponse.java
package com.novelgrain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;

    private String message;

    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, "OK", data);
    }

    public static <T> ApiResponse<T> err(int c, String m) {
        return new ApiResponse<>(c, m, null);
    }

    // 新增：别名
    public static <T> ApiResponse<T> error(int c, String m) {
        return err(c, m);
    }

}
