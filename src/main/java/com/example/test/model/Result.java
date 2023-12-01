package com.example.test.model;

import com.example.test.model.common.IdentityEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result extends IdentityEntity {

    @Schema(description = "ид набора символов")
    Long textId;
    @Column(name = "key_column")
    @Schema(description = "Символ")
    String key;
    @Column(name = "value_column")
    @Schema(description = "Сколько раз повторяется символ")
    Integer value;

    public static Result toMap(Long textId, String key, Integer value) {
       return Result.builder()
                .textId(textId)
                .key(key)
                .value(value)
                .build();
    }
}
