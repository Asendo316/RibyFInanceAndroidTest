package com.cordiscorp.ribyandroidtest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */

@Entity
public class Location {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "lat")
    public Double lat;

    @ColumnInfo(name = "lng")
    public Double lng;
}
