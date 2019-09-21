package com.cordiscorp.ribyandroidtest.model;


import com.cordiscorp.ribyandroidtest.connectivity.RetrofitClient;
import com.cordiscorp.ribyandroidtest.connectivity.fetcher.FetchDistanceService;
import com.cordiscorp.ribyandroidtest.connectivity.models.FetchDistanceResponse;
import com.cordiscorp.ribyandroidtest.contract.FetchDistanceContract;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
public class FetchDistanceModel implements FetchDistanceContract.Model {
    RetrofitClient retrofitClient = new RetrofitClient();
    FetchDistanceService fetchDistanceService =
            retrofitClient.getClient().create(FetchDistanceService.class);

    @Override
    public void fetchDistanceDetails(OnCompletedListener onCompletedListener, String origin, String destination, String mode, String key) {
        Call<FetchDistanceResponse> call = fetchDistanceService
                .getDistanceDetails(origin, destination, mode, key);
        call.enqueue(new Callback<FetchDistanceResponse>() {
            @Override
            public void onResponse(Call<FetchDistanceResponse> call, retrofit2.Response<FetchDistanceResponse> response) {
                if (response != null) {

                    try {
                        onCompletedListener.onGetDistanceResponse(response.body());

                    } catch (Exception e) {
                        onCompletedListener.onGetDistanceError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<FetchDistanceResponse> call, Throwable t) {
                onCompletedListener.onGetDistanceFailed(t);
            }
        });
    }
}
