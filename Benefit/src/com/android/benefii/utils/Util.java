package com.android.benefii.utils;
import android.util.Log;

/**
 * Created by ES on 2015-05-16.
 */

public class Util {
	
    public static final String CHARS = "0123456789ABCDEF";

    // 버퍼 데이터를 디코딩해서 String 으로 변환
    public static String byteDecoding(byte[] buf) {
        String strText="";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1,
                    buf.length - langCodeLen - 1, textEncoding);
        } catch(Exception e) {
            Log.d("tag1", e.toString());
        }
        return strText;
    }


    public static String toHexString(byte[] data){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<data.length; ++i){
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));

        }
        return sb.toString();
    }


}
