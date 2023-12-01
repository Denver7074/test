package com.example.test.api;

import com.example.test.api.model.ResponseEntity;
import com.example.test.api.response.PositiveResponse;
import com.example.test.api.response.ResponseApi;


import com.example.test.model.TextEntity;
import com.example.test.model.Result;
import com.example.test.rep.ResponseRep;
import com.example.test.rep.ResultRep;
import com.example.test.service.CountService;
import com.example.test.utils.Errors;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
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
    public PositiveResponse<ResponseEntity> countFrequencyConcurrent(@Valid @NotBlank @RequestParam String text, @RequestParam(defaultValue = "1") Integer number) {
        TextEntity entity = null;
        if (responseRep.existsByText(text)){
            entity = responseRep.findByText(text);
            List<Result> resultList = resultRep.findAllByTextId(entity.getId());
            return ResponseApi.positiveResponse(ResponseEntity.toMap(entity, resultList));
        }
        entity = new TextEntity();
        entity.setText(text);
        entity = responseRep.save(entity);
        return switch (number) {
            case 1 -> ResponseApi.positiveResponse(countService.countFrequency(entity));
            default -> ResponseApi.positiveResponse(countService.countFrequencyConcurrent(entity));
        };
    }

    @GetMapping
    public List<ResponseEntity> getAll() {
        List<ResponseEntity> responseEntities = new ArrayList<>();
        for (TextEntity e : responseRep.findAll()) {
            responseEntities.add(ResponseEntity.toMap(e, resultRep.findAllByTextId(e.getId())));
        }
        return responseEntities;
    }

}
