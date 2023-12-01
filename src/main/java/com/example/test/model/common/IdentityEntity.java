package com.example.test.model.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@FieldNameConstants
public class IdentityEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

}