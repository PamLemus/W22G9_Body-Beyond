package com.example.bodybeyond.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bodybeyond.interfaces.DietDao;
import com.example.bodybeyond.interfaces.ExerciseDao;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.Diet;
import com.example.bodybeyond.models.Exercise;
import com.example.bodybeyond.models.User;

@Database(entities = {User.class, Diet.class, Exercise.class}, version = 2, exportSchema = false)
public abstract class BodyAndBeyondDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract DietDao dietDao();
    public abstract ExerciseDao exerciseDao();
}
