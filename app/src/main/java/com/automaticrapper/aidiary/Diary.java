package com.automaticrapper.aidiary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Diary implements Serializable
{
    public String title;
    public String context;
    public String postTime;
    public float emotion;
    public ArrayList<String> tags;
    public String imageUri;

    public Diary(String title, String context,float emotion){
        this.title = title;
        this.context = context;
        this.emotion = emotion;
        this.imageUri = "";
        postTime = new String();
    }
}
