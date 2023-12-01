package com.example.test.utils;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class Utils {

    @FunctionalInterface
    public interface Supplier<T, E extends Throwable> {
        T get() throws E;
    }
    @Nullable
    public static <V> V safetyGet(Supplier<V, Exception> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("smth went wrong, ", e);
            return null;
        }
    }

    public static final Map<Class<?>, Function<String, Object>> converter = ImmutableMap.of(
            String.class, Object::toString,
            Long.class, NumberUtils::createLong,
            Integer.class, NumberUtils::createInteger
    );
}
