package com.example.camp_proj1;

import android.app.Application;

import java.util.ArrayList;

public class UserInfo {

    String name;
    String phoneNumber;
    String email;
   private int photo;

    public UserInfo(String name, String number, String email, int photo){
        this.name = name;
        this.phoneNumber = number;
        this.photo = photo;
        this.email = email;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){return email; }

    public String getPhoneNumber(){ return phoneNumber; }

    public int getPhoto(){ return photo;}

}

