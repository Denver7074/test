package com.example.test.service;

import com.example.test.api.model.ResponseEntity;
import com.example.test.model.TextEntity;
import com.example.test.model.Result;
import com.example.test.rep.ResultRep;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CountService {

    ResultRep resultRep;

    //concurrentcy
    @SneakyThrows
    public ResponseEntity countFrequencyConcurrent(TextEntity entity) {
        Map<Character, Integer> map = new ConcurrentHashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(entity.getText().length());
        Semaphore semaphore = new Semaphore(5);
        for (Character c : entity.getText().toCharArray()) {
            try {
                semaphore.acquire();
                try {
                    map.merge(c, 1, Integer::sum);
                } catch (Exception ex) {
                    Thread.currentThread().interrupt();
                    log.error("An error occurred while save symbol", ex);
                } finally {
                    semaphore.release();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.error("An error occurred while save symbol", ex);
            } finally {
                countDownLatch.countDown();
            }
        }
        countDownLatch.await();
        return sortByValue(map, entity);
    }

    public ResponseEntity countFrequency(TextEntity entity) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character c : entity.getText().toCharArray()) {
            map.merge(c, 1, Integer::sum);
        }
        return sortByValue(map, entity);
    }

    public ResponseEntity sortByValue(Map<Character, Integer> map, TextEntity entity) {
        List<Map.Entry<Character, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        List<Result> results = new LinkedList<>();
        for (Map.Entry<Character, Integer> entry : list) {
            Result result = Result.toMap(entity.getId(), String.valueOf(entry.getKey()), entry.getValue());
            results.add(result);
            resultRep.save(result);
        }
        return ResponseEntity.toMap(entity, results);
    }
}
