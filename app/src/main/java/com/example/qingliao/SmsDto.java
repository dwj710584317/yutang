package com.example.qingliao;

class SmsDto {
    private String address;
    private String body;
    private String date;
    private String type;

    public SmsDto(String address, String body, String date, String type) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
