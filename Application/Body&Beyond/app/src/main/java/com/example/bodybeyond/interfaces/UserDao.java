package com.example.bodybeyond.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bodybeyond.models.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUsers(User users);

    @Query("Select * FROM users WHERE useremail = :email AND password = :password")
    User authenticateUser(String email, String password);

    @Query("UPDATE users SET password = :password WHERE userEmail = :email")
    int updateUserPassword(String email, String password);

    @Query("UPDATE users SET userHeight = :height AND userWeight = :weight WHERE userEmail = :email")
    int updateUserInfo(double height, double weight, String email);

    @Query("SELECT * FROM users WHERE userEmail = :email")
    User getUserInfo(String email);
}
