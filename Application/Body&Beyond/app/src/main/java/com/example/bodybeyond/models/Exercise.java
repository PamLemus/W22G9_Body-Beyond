package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "exercise")
public class Exercise {

    @NonNull
    @ColumnInfo(name = "exerciseActivity")
    private String exerciseActivity;

    @NonNull
    @ColumnInfo(name="exerciseType")
    private int exerciseType;

    @ColumnInfo(name="exerciseDesc")
    private String exerciseDesc;

    @ColumnInfo(name="exerciseImg")
    private String exerciseImg;
}
