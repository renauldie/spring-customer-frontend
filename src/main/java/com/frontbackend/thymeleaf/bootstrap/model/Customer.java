package com.frontbackend.thymeleaf.bootstrap.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Customer {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String province;
    private LocalDateTime registerDate;
    private String status;

}
