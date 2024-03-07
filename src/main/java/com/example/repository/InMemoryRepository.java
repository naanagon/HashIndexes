package com.example.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Set;

@Repository
public class InMemoryRepository {

    HashMap<String, Long> inMemoryMap;
    InMemoryRepository() {
        inMemoryMap = new HashMap<>();
    }
    public Long get(String key){
        return inMemoryMap.getOrDefault(key, -1l);
    }

    public Set<String> getKeys(){
        return inMemoryMap.keySet();
    }
    public void store(String key, Long value){
        inMemoryMap.put(key, value);
        System.out.println("inMemory Data" + inMemoryMap);
    }
}
