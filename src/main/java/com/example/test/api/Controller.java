package com.example.test.api;

import com.example.test.api.model.ResponseEntity;
import com.example.test.api.response.PositiveResponse;
import com.example.test.api.response.ResponseApi;


import com.example.test.model.TextEntity;
import com.example.test.model.Result;
import com.example.test.rep.ResponseRep;
import com.example.test.rep.ResultRep;
import com.example.test.service.CountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Controller {

    ResultRep resultRep;
    ResponseRep responseRep;
    CountService countService;

    @PostMapping
    @Transactional
    @Operation(description = "Обработка полученной строки с символами",
            summary = "Обработка полученной строки с символами",
            responses = {
                    @ApiResponse(responseCode = "001", description = "Параметр 'text' не может быть пустым"),
            })
    public PositiveResponse<ResponseEntity> countFrequencyConcurrent(@NotBlank @RequestParam String text, @RequestParam(defaultValue = "1") Integer number) {
        TextEntity entity;
        if (responseRep.existsByText(text)){
            entity = responseRep.findByText(text);
            List<Result> resultList = resultRep.findAllByTextId(entity.getId());
            return ResponseApi.positiveResponse(ResponseEntity.toMap(entity, resultList));
        }
        entity = responseRep.save(new TextEntity().setText(text));
        return switch (number) {
            case 1 -> ResponseApi.positiveResponse(countService.countFrequency(entity));
            default -> ResponseApi.positiveResponse(countService.countFrequencyConcurrent(entity));
        };
    }

    @GetMapping
    @Operation(description = "Получение всех строк из бд с учетом обработки",
            summary = "Получение всех строк из бд с учетом обработки")
    public List<ResponseEntity> getAll() {
        List<TextEntity> all = responseRep.findAll();
        List<ResponseEntity> responseEntities = new ArrayList<>(all.size());
        for (TextEntity e : all) {
            responseEntities.add(ResponseEntity.toMap(e, resultRep.findAllByTextId(e.getId())));
        }
        return responseEntities;
    }

}
