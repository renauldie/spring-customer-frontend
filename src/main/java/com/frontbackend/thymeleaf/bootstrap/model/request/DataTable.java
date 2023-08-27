package com.frontbackend.thymeleaf.bootstrap.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTable<T> {

    private int draw;

    private int start;

    private int totalRecords;

    private int filteredRecords;

    private List<T> data;
}
