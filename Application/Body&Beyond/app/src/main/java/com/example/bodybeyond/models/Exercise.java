package com.example.bodybeyond.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise")
public class Exercise {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exerciseId")
    private int exerciseId;

    @NonNull
    @ColumnInfo(name = "exerciseActivity")
    private String exerciseActivity;

    @NonNull
    @ColumnInfo(name="exerciseType")
    private String exerciseType;

    @ColumnInfo(name="exerciseDesc")
    private String exerciseDesc;

    @ColumnInfo(name="exerciseImg")
    private String exerciseImg;

    public Exercise(int exerciseId, @NonNull String exerciseActivity, @NonNull String exerciseType, String exerciseDesc, String exerciseImg) {
        this.exerciseId = exerciseId;
        this.exerciseActivity = exerciseActivity;
        this.exerciseType = exerciseType;
        this.exerciseDesc = exerciseDesc;
        this.exerciseImg = exerciseImg;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    @NonNull
    public String getExerciseActivity() {
        return exerciseActivity;
    }

    public void setExerciseActivity(@NonNull String exerciseActivity) {
        this.exerciseActivity = exerciseActivity;
    }

    @NonNull
    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(@NonNull String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseDesc() {
        return exerciseDesc;
    }

    public void setExerciseDesc(String exerciseDesc) {
        this.exerciseDesc = exerciseDesc;
    }

    public String getExerciseImg() {
        return exerciseImg;
    }

    public void setExerciseImg(String exerciseImg) {
        this.exerciseImg = exerciseImg;
    }
}
