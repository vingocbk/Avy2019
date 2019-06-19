package com.pic.libphotocollage.core.model;

/**
 * Created by Ducng on 10/20/2016.
 */

public class ItemData {
    private String title = null ;
    private String data = null ;
    private int size = 0;
    private long date = 0;
    public boolean isHeader = false;

    public String dateString = "1";
    public int sectionCounter = 0;




    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    private boolean check = false;

    public ItemData(String dateString){
        this.dateString = dateString ;
    }

    public ItemData(String title,String data,int size,long date){
        this.title = title ;
        this.data = data ;
        this.size = size ;
        this.date = date ;
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
