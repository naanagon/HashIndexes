package com.example.repository;

import com.example.encoding.EnDeUtil;
import com.example.entity.StoreData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.example.common.Constants.READ_TIMEOUT;
import static com.example.common.Constants.WRITE_TIMEOUT;

@Repository
public class KeyValueRepository {
    @Autowired
    InMemoryRepository inMemoryRepository;

    @Autowired
    EnDeUtil enDeUtil;

    ExecutorService readerService = Executors.newFixedThreadPool(10);
    ExecutorService writerService = Executors.newFixedThreadPool(10);

    public List<StoreData> readAll(){
        List<StoreData> list = new ArrayList<>();
        for(String key: inMemoryRepository.getKeys()){
            list.add(readFromFile(key));
        }
        return list;
    }

    public StoreData readFromFile(String key){
        Long offset = inMemoryRepository.get(key);
        Future<byte[]> future = readerService.submit(new ReadUtil(offset));
        byte[] data = new byte[0];
        try {
            data = future.get(READ_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return enDeUtil.decode(data);
    }

    public void writeToFile(StoreData storeData){
        byte[] data = enDeUtil.encode(storeData);
        Future<Long> future = writerService.submit(new WriteUtil(data));
        Long offset = null;
        try {
            offset = future.get(WRITE_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        inMemoryRepository.store(storeData.getKey(), offset);
    }

}
