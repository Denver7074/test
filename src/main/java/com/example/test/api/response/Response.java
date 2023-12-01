package com.example.test.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Response<T> implements Serializable {


    Boolean isFinish;

    public Response(Boolean isFinish) {
        this.isFinish = isFinish;
    }
}
