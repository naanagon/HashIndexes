package com.example.service;

import com.example.entity.StoreData;
import com.example.repository.KeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyValueService {

    @Autowired
    KeyValueRepository keyValueRepository;

    public StoreData getValue(String key){
        return keyValueRepository.readFromFile(key);
    }

    public List<StoreData> getAll(){
        return keyValueRepository.readAll();
    }

    public void persistKeyValue(StoreData storeData){
        keyValueRepository.writeToFile(storeData);
    }
}
