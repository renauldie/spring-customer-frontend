package com.frontbackend.thymeleaf.bootstrap.controller;

import com.frontbackend.thymeleaf.bootstrap.model.Customer;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.request.DataTableRequestDTO;
import com.frontbackend.thymeleaf.bootstrap.model.request.JsonOperation;
import com.frontbackend.thymeleaf.bootstrap.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    @Autowired
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public Page<Customer> get(@RequestBody DataTableRequestDTO request) {
        return customerService.get(request);
    }

    @PostMapping("/create")
    public Customer create(@RequestBody Customer request) {
        return customerService.create(request);
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Integer id) {
        return customerService.getById(id);
    }

    @PatchMapping("/update/{id}")
    public Customer update(@PathVariable Integer id, @RequestBody JsonPatch request) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Object destroy(@PathVariable Integer id) {
        return customerService.delete(id);
    }

}
