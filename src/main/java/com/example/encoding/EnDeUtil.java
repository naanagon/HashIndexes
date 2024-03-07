package com.example.encoding;

import com.example.entity.StoreData;
import org.springframework.stereotype.Service;

@Service
public class EnDeUtil {

    public byte[] encode(StoreData storeData){
        MultiFieldEncoder encoder = MultiFieldEncoder.create(2);
        encoder.encodeNext(storeData.getKey());
        encoder.encodeNext(storeData.getValue());
        return encoder.build();
    }

    public StoreData decode(byte[] data){
        if(data==null || data.length ==0)
            return new StoreData();
        MultiFieldDecoder decoder = MultiFieldDecoder.wrap(data);
        String key = decoder.decodeNextString();
        String value = decoder.decodeNextString();
        return new StoreData(key, value);

    }
}
