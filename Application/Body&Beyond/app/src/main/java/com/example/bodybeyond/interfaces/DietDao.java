package com.example.bodybeyond.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bodybeyond.models.Diet;
import com.example.bodybeyond.models.Exercise;

import java.util.List;

@Dao
public interface DietDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDiets(List<Diet> diets);

    @Query("SELECT * FROM diet WHERE dietType = :dietType AND dietRange = :dietRange AND dietDays = :dietDay")
    List<Diet> getDiets(String dietType, String dietRange, String dietDay);

    @Query("SELECT * FROM diet")
    List<Diet> getAllDiets();
}

