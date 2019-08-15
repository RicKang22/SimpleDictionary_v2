package com.android2016ncu.simpledictionary_v2.util;


import com.android2016ncu.simpledictionary_v2.model.DailySent;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ParseJSON {

    private DailySent dailySent;


    public ParseJSON(){
        this.dailySent = new DailySent();
    }


    public DailySent getDailySent(){
        return dailySent;
    }

    public void parseJSON(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            dailySent.setContent(jsonObject.getString("content"));
            dailySent.setTts(jsonObject.getString("tts"));
            dailySent.setNote(jsonObject.getString("note"));
            dailySent.setPictuteAddress(jsonObject.getString("picture"));
            dailySent.setToDate(jsonObject.getString("dateline"));
            dailySent.setTrans(jsonObject.getString("translation"));
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    //将输入流转换成字符串
    public String stranToSting(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bytes = new byte[4096];
        for (int i; (i = inputStream.read(bytes)) != -1;){
            stringBuffer.append(new String(bytes,0,i));
        }
        return stringBuffer.toString();
    }



}
