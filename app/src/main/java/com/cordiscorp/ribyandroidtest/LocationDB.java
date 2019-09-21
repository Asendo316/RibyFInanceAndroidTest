package com.cordiscorp.ribyandroidtest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
@Database(entities = {Location.class}, version = 1)
public abstract class LocationDB extends RoomDatabase {
    public abstract LocationDAO locationDAO();
}

