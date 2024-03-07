package com.example.repository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

import static com.example.common.Constants.EMPTY_BYTE_ARRAY;
import static com.example.common.Constants.S_PATH;

public class ReadUtil implements Callable<byte[]> {

    Long offset;

    ReadUtil(Long offset){
        this.offset=offset;
    }

    @Override
    public byte[] call() {
        if(offset==-1)
            return null;
            try(RandomAccessFile randomAccessFile=new RandomAccessFile(S_PATH, "r")){
                randomAccessFile.seek(offset);
                int length;
                if((length=randomAccessFile.readByte())!=0) {
                    byte[] data = new byte[length];
                    randomAccessFile.read(data, 0, length);
                    return data;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return EMPTY_BYTE_ARRAY;
    }
}
