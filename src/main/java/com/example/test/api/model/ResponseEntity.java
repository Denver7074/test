package com.example.test.api.model;

import com.example.test.model.Result;
import com.example.test.model.TextEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseEntity {

    String text;
    List<ResponseResult> results = new ArrayList<>();

    public static ResponseEntity toMap(TextEntity entity, List<Result> resultList) {
        ResponseEntity en = new ResponseEntity();
        en.setText(entity.getText());
        for (Result r : resultList) {
            en.results.add(ResponseResult.toModel(r));
        }
        return en;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ResponseResult {
        String key;
        Integer value;

        public static ResponseResult toModel(Result r) {
            return ResponseResult.builder()
                    .key(r.getKey())
                    .value(r.getValue())
                    .build();
        }
    }


}
