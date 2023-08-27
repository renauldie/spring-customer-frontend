package com.frontbackend.thymeleaf.bootstrap.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JsonOperation {
    private String op;
    private String path;
    private String value;
}
