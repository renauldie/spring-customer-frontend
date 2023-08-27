package com.frontbackend.thymeleaf.bootstrap.service;

import com.frontbackend.thymeleaf.bootstrap.model.Customer;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.request.DataTable;
import com.frontbackend.thymeleaf.bootstrap.model.request.DataTableRequestDTO;
import com.frontbackend.thymeleaf.bootstrap.model.request.JsonOperation;
import com.github.fge.jsonpatch.JsonPatch;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerService {

    @Value("${server.base.url}/api/customers/")
    private String url;

    @Autowired
    private RestTemplate restTemplate;



    public Page<Customer> get(DataTableRequestDTO request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataTableRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String strRes = response.getBody();
            JsonObject returnValue = new Gson().fromJson(strRes, JsonObject.class);
            JsonObject value = returnValue.getAsJsonObject("data");

            JsonArray newObject = value.getAsJsonArray("data");
            JsonArray arrayData = JsonParser.parseString(newObject.toString()).getAsJsonArray();
            List<Object> listData = new ArrayList<>();

            Gson gson = new Gson();
            for (int i = 0; i < arrayData.size(); i++) {
                listData.add(gson.fromJson(arrayData.get(i), Object.class));
            }

            DataTable dataTable = new DataTable();
            dataTable.setDraw(value.get("draw").getAsInt());
            dataTable.setStart(value.get("start").getAsInt());
            dataTable.setTotalRecords(value.get("totalRecords").getAsInt());
            dataTable.setFilteredRecords(value.get("filteredRecords").getAsInt());
            dataTable.setData(listData);
            return getPage(dataTable);
        }

        return new Page<>();
    }

    private Page<Customer> getPage(DataTable data) {
        List<Customer> filtererd = data.getData();

        Page<Customer> page = new Page<>(filtererd);
        page.setData(data.getData());
        page.setRecordsFiltered(data.getFilteredRecords());
        page.setRecordsTotal(data.getTotalRecords());
        page.setDraw(data.getStart());
        return page;
    }

    public Customer getById(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataTableRequestDTO> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url.concat("get/" + id), HttpMethod.GET, requestEntity, String.class);

        return resultCustomer(response);
    }

    public Object delete(Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataTableRequestDTO> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url.concat("/" + id), HttpMethod.DELETE, requestEntity, String.class);

        Map<String, Object> data = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            String strResponse = response.getBody();
            JsonObject valueResponse = new Gson().fromJson(strResponse, JsonObject.class);

            data.put("message", valueResponse.get("message").getAsString());
        }

        return data;
    }

    public Customer update(Integer id, JsonPatch request) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        headers.add("Access-Control-Allow-Methods", "PATCH");
//
//        HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);
//
//        String response = restTemplate.patchForObject(
//                url.concat("/" + id),
//                requestEntity,
//                String.class);
//
//        return resultCustomer(response);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);

        HttpEntity<JsonPatch> entity = new HttpEntity<>(request, headers);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        restTemplate.exchange(url.concat("/" + id), HttpMethod.PATCH, entity, Void.class);
        return null;
    }

    public Customer create(Customer request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url.concat("/create"), HttpMethod.POST, requestEntity, String.class);

        return resultCustomer(response);
    }

    private Customer resultCustomer(ResponseEntity<String> response) {
        Customer customer = new Customer();
        if (response.getStatusCode() == HttpStatus.OK) {
            String strResponse = response.getBody();
            JsonObject valueResponse = new Gson().fromJson(strResponse, JsonObject.class);
            JsonObject data = valueResponse.getAsJsonObject("data");

            customer.setId(data.get("id").getAsInt());
            customer.setName(data.get("name").getAsString());
            customer.setAddress(data.get("address").getAsString());
            customer.setCity(data.get("city").getAsString());
            customer.setProvince(data.get("province").getAsString());
            customer.setRegisterDate(LocalDateTime.parse(data.get("registerDate").getAsString()));
            customer.setStatus(data.get("status").getAsString());
        }

        return customer;
    }

}
