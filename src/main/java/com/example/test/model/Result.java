package com.example.test.model;

import com.example.test.model.common.IdentityEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result extends IdentityEntity {


    Long textId;
    @Column(name = "key_column")
    String key;
    @Column(name = "value_column")
    Integer value;

    public static Result toMap(Long textId, String key, Integer value) {
       return Result.builder()
                .textId(textId)
                .key(key)
                .value(value)
                .build();
    }
}
