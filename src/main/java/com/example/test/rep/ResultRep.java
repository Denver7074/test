package com.example.test.rep;

import com.example.test.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRep extends JpaRepository<Result, Long> {

    List<Result> findAllByTextId(Long textId);
}
