package com.example.test.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Response<T> implements Serializable {

    String message;
    T data;
    Boolean isFinish;

    public Response(T data) {
        this.isFinish = Boolean.TRUE;
        this.message = StringUtils.EMPTY;
        this.data = data;
    }
}
