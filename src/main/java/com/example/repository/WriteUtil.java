package com.example.repository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

import static com.example.common.Constants.S_PATH;

public class WriteUtil implements Callable<Long> {

    byte[] data;
    WriteUtil(byte[] data){
        this.data=data;
    }

    @Override
    public Long call() throws Exception {
        try {
            try(RandomAccessFile randomAccessFile=new RandomAccessFile(S_PATH, "rw")) {
                randomAccessFile.seek(randomAccessFile.length());
                randomAccessFile.write(data);
                Long pointer = randomAccessFile.getFilePointer()- data.length;
                randomAccessFile.close();
                return pointer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1l;
    }
}
