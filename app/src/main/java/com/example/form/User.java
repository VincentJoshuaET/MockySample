package com.example.form;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("gender")
    private String gender;

    public User(String name, String email, String mobile, String birthdate, String gender) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }
}
