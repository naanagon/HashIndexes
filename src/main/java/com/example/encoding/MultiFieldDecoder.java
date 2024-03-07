package com.example.encoding;

import java.nio.charset.StandardCharsets;

/**
 * Decodes a single byte[] into multiple field based on terminator elements.
 */
public class MultiFieldDecoder {
    private byte[] data;
    private int currentOffset;
    private int length;
    private int offset;

    private MultiFieldDecoder(){
        this.currentOffset=-1;
    }

    public static MultiFieldDecoder create(){
        return new MultiFieldDecoder();
    }

    public static MultiFieldDecoder wrap(byte[] row) {
        return wrap(row,0,row.length);
    }

    public static MultiFieldDecoder wrap(byte[] row, int offset, int length) {
        MultiFieldDecoder next = new MultiFieldDecoder();
        next.set(row,offset,length);
        return next;
    }

    public MultiFieldDecoder set(byte[] newData,int offset,int length){
        this.data = newData;
        currentOffset = offset;
        this.length = length;
        this.offset = offset;
        return this;
    }

    public String decodeNextString() {
        if (currentOffset >= offset &&
                (offset+length == currentOffset || data[currentOffset] == 0x00)) {
            currentOffset++;
            return null;
        }

        //determine the length of the string ahead of time
        int offset = currentOffset >= 0 ? currentOffset : 0;
        adjustOffset(-1);
        //the string length is the number of bytes that we encode
        return new String(data, offset,  currentOffset - offset - 1, StandardCharsets.UTF_8);
    }

    private void adjustOffset(int expectedLength){
        /*
         * if expectedLength <0, then we don't know where
         * the next terminator will be, so just keep looking until
         * we find one or we run out of data
         */
        if(expectedLength<0){
            expectedLength = offset+length-currentOffset;
        }
        if(expectedLength+currentOffset>=data.length) {
            expectedLength = data.length - currentOffset;
        }
        for(int i=1;i<expectedLength;i++){
            if(currentOffset+i>=offset+length){
                //we're out of bytes, so we must have been the end
                currentOffset=offset+length;
                return;
            }

            if(data[currentOffset+i] == 0x00){
                currentOffset+=i+1;
                return;
            }
        }
        currentOffset += expectedLength + 1; //not found before the end of the expectedLength
    }

}