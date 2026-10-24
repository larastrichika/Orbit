package com.shreebrahmanitravels.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Update
    void updateUser(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE phoneNo = :phoneNo LIMIT 1")
    UserEntity getUserByPhone(String phoneNo);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity login(String username, String password);

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);

    @Query("UPDATE users SET lastbooked_date = :date WHERE username = :username")
    void updateLastBookedDate(String username, String date);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();

    @Query("DELETE FROM users WHERE username = :username")
    void deleteUser(String username);
}
