package com.example.test.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NegativeResponse<T> extends Response<T> {
    T data;
    String code;
    Object details;

    public NegativeResponse(String code, Object details, T data) {
        super(Boolean.FALSE);
        this.code = code;
        this.data = data;
        this.details = details;
    }

    public Boolean getResult() {
        return Boolean.FALSE;
    }
}
