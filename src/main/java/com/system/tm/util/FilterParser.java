package com.system.tm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.tm.service.filter.CriteriaModel;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class FilterParser {

    public List<CriteriaModel> parseCriteriaJson(String criteriaJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(criteriaJson, CriteriaModel[].class));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Не удалось проанализировать условия", ex);
        }
    }
}