package com.example.bodybeyond.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class BodyAndBeyondDB extends RoomDatabase {
    public abstract UserDao userDao();
}
