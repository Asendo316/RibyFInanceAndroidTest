package com.cordiscorp.ribyandroidtest.connectivity.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ibkunle Adeoluwa on 5/22/2019.
 */
public class EndLocation_ implements Serializable {

    @SerializedName("lat")
    @Expose
    private Float lat;
    @SerializedName("lng")
    @Expose
    private Float lng;
    private final static long serialVersionUID = -1857051357260249496L;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

}
