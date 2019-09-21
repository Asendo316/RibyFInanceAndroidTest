package com.cordiscorp.ribyandroidtest.connectivity.models;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
public class UserLocation {
    private Double lat,lng;

    public UserLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
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
