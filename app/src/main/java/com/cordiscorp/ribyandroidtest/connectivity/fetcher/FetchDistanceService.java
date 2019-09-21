package com.cordiscorp.ribyandroidtest.connectivity.fetcher;


import com.cordiscorp.ribyandroidtest.connectivity.models.FetchDistanceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ibkunle Adeoluwa on 5/22/2019.
 */
public interface FetchDistanceService {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("maps/api/directions/json")
    Call<FetchDistanceResponse> getDistanceDetails(@Query(value = "origin", encoded = false) String origin, @Query(value = "destination", encoded = false) String destination, @Query(value = "mode", encoded = false) String mode, @Query(value = "key", encoded = false) String key);

}
