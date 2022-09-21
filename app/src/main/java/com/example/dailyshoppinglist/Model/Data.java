package com.example.dailyshoppinglist.Model;

import androidx.annotation.NonNull;

public class Data {
    private int amount;
    private String type;
    private String note;
    private String date;
    private String id;

    public Data(int amount, String type, String note, String date, String id) {
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = date;
        this.id = id;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Data{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                ", date='" + date + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
