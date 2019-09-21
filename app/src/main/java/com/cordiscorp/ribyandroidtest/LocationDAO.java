package com.cordiscorp.ribyandroidtest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM location")
    List<Location> getAll();

    @Query("SELECT * FROM location WHERE uid IN (:userIds)")
    List<Location> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM location WHERE lat LIKE :lat AND " +
            "lng LIKE :lng LIMIT 1")
    Location findByLocation(Double lat, Double lng);

    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);
}
