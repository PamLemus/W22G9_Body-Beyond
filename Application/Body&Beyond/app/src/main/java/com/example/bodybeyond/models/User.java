package com.example.bodybeyond.models;

public class User {
    String userName;
    int userAge;
    char userGender;
    double userHeight;
    double userWeight;
    String activity;

    public User(String userName, int userAge, char userGender, double userHeight, double userWeight, String activity) {
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.activity = activity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public char getUserGender() {
        return userGender;
    }

    public void setUserGender(char userGender) {
        this.userGender = userGender;
    }

    public double getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(double userHeight) {
        this.userHeight = userHeight;
    }

    public double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(double userWeight) {
        this.userWeight = userWeight;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
