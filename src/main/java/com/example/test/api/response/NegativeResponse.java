package com.example.test.api.response;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NegativeResponse<T> extends Response<T> {

    String code;

    public NegativeResponse(String code, String message, T data) {
        super(message, data, Boolean.FALSE);
        this.code = code;
    }
}
