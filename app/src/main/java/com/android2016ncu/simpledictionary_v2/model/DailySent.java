package com.android2016ncu.simpledictionary_v2.model;

public class DailySent {
    //英文内容
    private String content;

    //英文发音地址
    private String tts;

    //中文内容
    private String note;

    //图片地址
    private String pictuteAddress;

    //今日日期
    private String toDate;

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    //小编的话
    private String trans;

    public DailySent(){
        this.content = "";
        this.note = "";
        this.tts = "";
        this.pictuteAddress = "";
        this.toDate = "";
    }


    public DailySent(String content,String tts,String note,String pictuteAddress, String toDate){
        this.content = content;
        this.note = note;
        this.tts = tts;
        this.pictuteAddress = pictuteAddress;
        this.toDate = toDate;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPictuteAddress() {
        return pictuteAddress;
    }

    public void setPictuteAddress(String pictuteAddress) {
        this.pictuteAddress = pictuteAddress;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
