package com.example.test.api.model;

import com.example.test.model.Result;
import com.example.test.model.TextEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseEntity {

    String text;
    List<Map<String, Integer>> results = new LinkedList<>();

    public static ResponseEntity toMap(TextEntity entity, List<Result> resultList) {
        ResponseEntity en = new ResponseEntity();
        en.setText(entity.getText());
        for (Result r : resultList) {
            en.results.add(Map.of(r.getKey(), r.getValue()));
        }
        return en;
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @FieldDefaults(level = AccessLevel.PRIVATE)
//    public static class ResponseResult {
//        String key;
//        Integer value;
//
//        public static ResponseResult toModel(Result r) {
//            return ResponseResult.builder()
//                    .key(r.getKey())
//                    .value(r.getValue())
//                    .build();
//        }
//    }


}
