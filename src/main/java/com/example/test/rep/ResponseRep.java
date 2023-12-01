package com.example.test.rep;

import com.example.test.model.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRep extends JpaRepository<TextEntity, Long> {

    Boolean existsByText(String text);
    TextEntity findByText(String text);
}
