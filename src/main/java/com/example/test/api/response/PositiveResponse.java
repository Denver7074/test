package com.example.test.api.response;

public class PositiveResponse<T> extends Response<T>{
    public PositiveResponse(T data) {
        super(data);
    }
}
