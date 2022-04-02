package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "diet")

public class Diet {

    @NonNull
    @ColumnInfo(name = "dietType")
    private String dietType;

    @NonNull
    @ColumnInfo(name="dietRange")
    private int dietRange;

    @ColumnInfo(name="dietDays")
    private String dietDays;

    @NonNull
    @ColumnInfo(name="dietName")
    private double dietName;

    @NonNull
    @ColumnInfo(name="dietDesc")
    private double dietDesc;

    @ColumnInfo(name="dietImg")
    private String dietImg;





}
