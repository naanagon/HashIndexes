package com.example.controller;

import com.example.entity.StoreData;
import com.example.service.ClientResponse;
import com.example.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("")
public class ApiController {

    @Autowired
    KeyValueService keyValueService;

    @GetMapping("/get")
    public ResponseEntity retrieve(@RequestParam Optional<String> key){
        System.out.println("name is :"+key.get());
        ClientResponse response = new ClientResponse();
        response.setData(keyValueService.getValue(key.get()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity retrieve(){
        ClientResponse response = new ClientResponse();
        response.setData(keyValueService.getAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity store(@RequestBody StoreData storeData){
        System.out.println("Method is: post");
        ClientResponse response = new ClientResponse();
        keyValueService.persistKeyValue(storeData);
        response.setData(storeData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
