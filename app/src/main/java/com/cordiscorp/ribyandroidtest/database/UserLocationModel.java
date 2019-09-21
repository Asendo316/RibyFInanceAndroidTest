package com.cordiscorp.ribyandroidtest.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
@Entity(tableName = "location")
public class UserLocationModel {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "lat")
    private Double lat;

    @ColumnInfo(name = "lng")
    private Double lng;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
