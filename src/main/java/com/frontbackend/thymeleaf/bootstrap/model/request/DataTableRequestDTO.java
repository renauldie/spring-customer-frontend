package com.frontbackend.thymeleaf.bootstrap.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTableRequestDTO {

    private int draw;

    private List<HashMap<String, Object>> columns;

    private List<HashMap<String, Object>> order;

    private Map<String, Object> search;

    private int start;

    private int length;

    private Map<String, Object> otherParameter;

}
