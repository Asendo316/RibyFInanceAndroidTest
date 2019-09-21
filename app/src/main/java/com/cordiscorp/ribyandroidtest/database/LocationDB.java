package com.cordiscorp.ribyandroidtest.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
@Database(entities = {UserLocationModel.class}, version = 1, exportSchema = false)
public abstract class LocationDB extends RoomDatabase {
    public abstract LocationDAO userLocationDao();

    private static LocationDB INSTANCE;

    public static LocationDB getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LocationDB.class, "location-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
