package com.example.form;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private final String name;

    @SerializedName("email")
    private final String email;

    @SerializedName("mobile")
    private final String mobile;

    @SerializedName("birthdate")
    private final String birthdate;

    @SerializedName("gender")
    private final String gender;

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
