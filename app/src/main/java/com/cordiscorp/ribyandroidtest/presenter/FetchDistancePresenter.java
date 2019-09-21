package com.cordiscorp.ribyandroidtest.presenter;

import com.cordiscorp.ribyandroidtest.connectivity.models.FetchDistanceResponse;
import com.cordiscorp.ribyandroidtest.connectivity.models.Leg;
import com.cordiscorp.ribyandroidtest.connectivity.models.Route;
import com.cordiscorp.ribyandroidtest.connectivity.models.UserLocation;
import com.cordiscorp.ribyandroidtest.contract.FetchDistanceContract;
import com.cordiscorp.ribyandroidtest.model.FetchDistanceModel;

import java.util.List;

/**
 * Created by Ibkunle Adeoluwa on 9/21/2019.
 */
public class FetchDistancePresenter implements FetchDistanceContract.Presenter,
        FetchDistanceContract.Model.OnCompletedListener {

    private FetchDistanceContract.View fetchView;
    private FetchDistanceContract.Model fetchModel;
    private List<Route> routeList;
    private List<Leg> legList;


    public FetchDistancePresenter(FetchDistanceContract.View fetchView) {
        this.fetchView = fetchView;
        fetchModel = new FetchDistanceModel();
    }

    @Override
    public void onGetDistanceResponse(FetchDistanceResponse fetchDistanceResponse) {
        routeList = fetchDistanceResponse.getRoutes();
        legList = routeList.get(0).getLegs();

        fetchView.showGetDistanceResults(
                convertDistanceToKm(
                        legList.get(0).getDistance().getValue()) +" km",
                        legList.get(0).getDuration().toString(),
                        legList.get(0).getStartAddress(),
                        legList.get(0).getEndAddress());

        if (fetchView != null) {
            fetchView.hideGetDistanceProgress();
        }
    }

    private String convertDistanceToKm(int distanceValue) {
        double kmDistance = Math.round(distanceValue * 0.001);
        int finalDistance = (int) kmDistance;
        return String.valueOf(finalDistance);
    }

    @Override
    public void onGetDistanceError(String message) {
        fetchView.showFetchDistanceError(message);
        if (fetchView != null) {
            fetchView.hideGetDistanceProgress();
        }
    }

    @Override
    public void onGetDistanceFailed(Throwable t) {
        fetchView.onFetchDIstanceFailed(t);
        if (fetchView != null) {
            fetchView.hideGetDistanceProgress();
        }
    }

    @Override
    public void onGetDistanceDestroy() {
        this.fetchModel = null;
    }

    public void getDistance(UserLocation startLocation, UserLocation stopLocation, String key) {
        if (fetchModel != null) {
            fetchView.showGetDistanceProgress();
        }
        fetchModel.fetchDistanceDetails(this, startLocation.getLat() + "," + startLocation.getLng(),
                stopLocation.getLat() + "," + stopLocation.getLng(),
                "driving", key);
    }
}
