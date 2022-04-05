package com.example.bodybeyond.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bodybeyond.models.Exercise;
import com.example.bodybeyond.models.User;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertExercises(List<Exercise> exercise);

    @Query("SELECT * FROM exercise WHERE exerciseType = :exerciseType AND exerciseActivity = :exerciseActivity")
    List<Exercise> getExercises(String exerciseType, String exerciseActivity);

    @Query("SELECT * FROM exercise")
    List<Exercise> getAllExercises();
}
