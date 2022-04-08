package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diet")
public class Diet {

    @NonNull
    @ColumnInfo(name = "dietId")
    @PrimaryKey(autoGenerate = true)
    private int dietId;

    @NonNull
    @ColumnInfo(name = "dietType")
    private String dietType;

    @NonNull
    @ColumnInfo(name="dietRange")
    private String dietRange;

    @NonNull
    @ColumnInfo(name="dietDays")
    private String dietDays;

    @NonNull
    @ColumnInfo(name="dietName")
    private String dietName;

    @NonNull
    @ColumnInfo(name="dietDesc")
    private String dietDesc;

    @ColumnInfo(name="dietImg")
    private String dietImg;

    public Diet(int dietId, @NonNull String dietType, @NonNull String dietRange, @NonNull String dietDays, @NonNull String dietName, @NonNull String dietDesc, String dietImg) {
        this.dietId = dietId;
        this.dietType = dietType;
        this.dietRange = dietRange;
        this.dietDays = dietDays;
        this.dietName = dietName;
        this.dietDesc = dietDesc;
        this.dietImg = dietImg;
    }
    public  Diet(){

    }

    public int getDietId() {
        return dietId;
    }

    public void setDietId(int dietId) {
        this.dietId = dietId;
    }

    @NonNull
    public String getDietType() {
        return dietType;
    }

    public void setDietType(@NonNull String dietType) {
        this.dietType = dietType;
    }

    @NonNull
    public String getDietRange() {
        return dietRange;
    }

    public void setDietRange(@NonNull String dietRange) {
        this.dietRange = dietRange;
    }

    @NonNull
    public String getDietDays() {
        return dietDays;
    }

    public void setDietDays(@NonNull String dietDays) {
        this.dietDays = dietDays;
    }

    @NonNull
    public String getDietName() {
        return dietName;
    }

    public void setDietName(@NonNull String dietName) {
        this.dietName = dietName;
    }

    @NonNull
    public String getDietDesc() {
        return dietDesc;
    }

    public void setDietDesc(@NonNull String dietDesc) {
        this.dietDesc = dietDesc;
    }

    public String getDietImg() {
        return dietImg;
    }

    public void setDietImg(String dietImg) {
        this.dietImg = dietImg;
    }
}
