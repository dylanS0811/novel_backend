package com.novelgrain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.novelgrain.common.Messages;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;

    private String message;

    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, Messages.tr("ok"), data);
    }

    public static <T> ApiResponse<T> err(int c, String m) {
        return new ApiResponse<>(c, Messages.tr(m), null);
    }

    public static <T> ApiResponse<T> error(int c, String m) {
        return err(c, m);
    }

}
