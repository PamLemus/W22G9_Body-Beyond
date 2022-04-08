package com.example.bodybeyond.viewmodel;

import android.graphics.drawable.Drawable;

public class Exercises {
    public String Description;
    public int ImageItem;
    public String ExerciseName;


    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImageItem() {
        return ImageItem;
    }

    public void setImageItem(int imageItem) {
        ImageItem = imageItem;
    }

    public Exercises(String exerciseName, int imageItem,String description ) {
        Description = description;
        ImageItem = imageItem;
        ExerciseName = exerciseName;
    }

    public Exercises(String description, int imageItem) {
        Description = description;
        ImageItem = imageItem;
    }
}
