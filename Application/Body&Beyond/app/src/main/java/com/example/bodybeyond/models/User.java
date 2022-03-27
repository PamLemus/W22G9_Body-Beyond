package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "userEmail")
    private String userEmail;

    @NonNull
    @ColumnInfo(name = "userName")
    private String userName;

    @NonNull
    @ColumnInfo(name="userAge")
    private int userAge;

    @ColumnInfo(name="userGender")
    private String userGender;

    @NonNull
    @ColumnInfo(name="userHeight")
    private double userHeight;

    @NonNull
    @ColumnInfo(name="userWeight")
    private double userWeight;

    @ColumnInfo(name="activityType")
    private String activityType;

    @ColumnInfo(name="password")
    private String password;


    public User(){

    }

    public User(@NonNull String userEmail, @NonNull String userName, int userAge, String userGender,
                double userHeight, double userWeight, String activityType, String password) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.activityType = activityType;
        this.password = password;
    }

    @NonNull
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(@NonNull String userEmail) {
        this.userEmail = userEmail;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
