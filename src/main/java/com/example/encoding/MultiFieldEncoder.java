package com.example.encoding;

import com.example.common.Constants;

import java.nio.charset.StandardCharsets;

/**
 * Encode multiple fields into a single byte array.  Encode(X) methods delegate to our XEncoding classes.
 *
 * Spec:
 *
 * -- Fields are followed by a single byte, 0, field delimiter.
 * -- Last field does not get a delimiter.
 * -- Empty fields (encodeEmpty()) occupy zero bytes, but are followed by a delimiter.
 *
 */
public class MultiFieldEncoder {
    private final byte[][] fields;
    private final int numFields;
    private int currentPos;
    private int currentSize;

    private int markedPos = 0;
    private int markedSize = 0;

    private MultiFieldEncoder(int numFields) {
        fields = new byte[numFields][];
        this.numFields = numFields;
        //initialize ourselves
        reset();
    }

    public static MultiFieldEncoder create(int numFields){
        return new MultiFieldEncoder(numFields);
    }

    public MultiFieldEncoder encodeNext(String value){
        encodeNext(value,false);
        return this;
    }

    public MultiFieldEncoder encodeNext(String value,boolean desc){
//        assert currentPos<fields.length;
        byte[] next = value.getBytes(StandardCharsets.UTF_8);
        currentSize+=next.length;
        fields[currentPos] = next;
        currentPos++;
        return this;
    }



    public byte[] build(){
        //if you haven't tried to encode anything, return empty array
        if(currentPos==0) return Constants.EMPTY_BYTE_ARRAY;

        byte[] data = new byte[currentSize+currentPos];
        int destPos=0;
        data[destPos++]=(byte)(data.length-1);
        for(int srcPos=0;srcPos<currentPos;srcPos++){
            byte[] src = fields[srcPos];
            if(srcPos != 0){
                data[destPos] = 0x00; //we know that 0x00 is never allowed, so it's a safe terminator
                destPos++;
            }
            if(src==null || src.length==0) {
                /* Happens when encodeEmpty() is called, or if a field's encoding is an empty byte array (e.g. null String) */
                continue;
            }
            System.arraycopy(src,0,data,destPos,src.length);
            destPos+=src.length;
        }
        return data;
    }

    public void mark() {
        markedPos = currentPos;
        markedSize = currentSize;
    }

    public void reset(){
        /*
         * Rather than waste time manually clearing data, just write over the positions
         * as we need to. Any remaining garbage in the array will get destroyed along with this object
         * then. Just make sure we don't keep one of these around for forever without using it repeatedly.
         */
        currentPos=markedPos;
        currentSize= markedSize;
    }

    public static class MultiFieldDecoder {
    }
}