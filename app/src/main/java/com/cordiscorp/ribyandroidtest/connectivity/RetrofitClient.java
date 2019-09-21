package com.cordiscorp.ribyandroidtest.connectivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ibikunle Adeoluwa on 12/21/2018.
 */

public class RetrofitClient {

    public static final String BASE_URL = "https://maps.googleapis.com/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
