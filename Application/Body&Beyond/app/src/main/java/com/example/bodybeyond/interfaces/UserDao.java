package com.example.bodybeyond.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bodybeyond.models.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUsers(User users);

    @Query("Select * FROM users WHERE useremail = :email AND password = :password")
    User authenticateUser(String email, String password);

    @Query("UPDATE users SET password = :password WHERE userEmail = :email")
    int updateUserPassword(String email, String password);

    @Query("UPDATE users SET userHeight = :height, userWeight = :weight WHERE userEmail = :email")
    int updateUserInfo(double height, double weight, String email);

    @Query("SELECT * FROM users WHERE userEmail = :email")
    User getUserInfo(String email);

    @Query("UPDATE users SET userName = :name, userAge = :age, userGender = :gender, userHeight = :height, userWeight = :weight, activityType = :activity WHERE userEmail = :email")
    int updateAllProfileInfo(String name, int age, String gender, double height, double weight, String activity, String email);



}
