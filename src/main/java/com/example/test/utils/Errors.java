package com.example.test.utils;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.ObjectUtils.anyNotNull;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Errors {

    E001("Текстовая строка не введена");

    String description;

    public void thr(Boolean value, Object... args) {
        if (isTrue(value)) {
             throw new CustomException(this, anyNotNull(args)
                    ? String.format(this.description, args)
                    : this.description);
        }
    }

    @Data
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class CustomException extends RuntimeException {
        Errors error;
        String message;

        public String getMsg() {
            return StringUtils.defaultString(message, error.getDescription());
        }

        @Override
        public String getMessage() {
            return getMsg();
        }
    }

}
