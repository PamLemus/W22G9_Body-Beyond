package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "useremail")
    private String userEmail;

    @NonNull
    @ColumnInfo(name = "username")
    private String userName;

    @NonNull
    @ColumnInfo(name="userAage")
    private int userAge;

    @ColumnInfo(name="usergender")
    private String userGender;

    @NonNull
    @ColumnInfo(name="userheight")
    private double userHeight;

    @NonNull
    @ColumnInfo(name="userweight")
    private double userWeight;

    @ColumnInfo(name="activity")
    private String activity;

    public User(){

    }

    public User(@NonNull String userEmail, @NonNull String userName, int userAge, String userGender,
                double userHeight, double userWeight, String activity) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.activity = activity;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
