package com.cordiscorp.ribyandroidtest.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
@Dao
public interface LocationDAO {
    @Insert
    Long insert(UserLocationModel userLocationModel);

    @Query("SELECT * FROM `location` ORDER BY `uid` DESC")
    List<UserLocationModel> getAllUsers();

    @Query("SELECT * FROM `location` WHERE `uid` =:id")
    UserLocationModel getUserLocationById(int id);

    @Update
    void update(UserLocationModel userLocationModel);

    @Delete
    void delete(UserLocationModel userLocationModel);
}
