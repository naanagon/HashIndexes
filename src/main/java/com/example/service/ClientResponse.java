package com.example.service;

import lombok.Data;

import java.util.List;

@Data
public class ClientResponse<T> {
    private T data;
    private String errorMsg;
    private List<String> validationErrors;
}
