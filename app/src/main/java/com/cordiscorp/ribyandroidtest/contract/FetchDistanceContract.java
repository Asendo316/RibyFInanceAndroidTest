package com.cordiscorp.ribyandroidtest.contract;


import com.cordiscorp.ribyandroidtest.connectivity.models.FetchDistanceResponse;

/**
 * Created by Ibkunle Adeoluwa on 9/20/2019.
 */

public class FetchDistanceContract {

    public interface Model {

        interface OnCompletedListener {
            void onGetDistanceResponse(FetchDistanceResponse fetchDistanceResponse);

            void onGetDistanceError(String message);

            void onGetDistanceFailed(Throwable t);
        }

        void fetchDistanceDetails(OnCompletedListener onCompletedListener, String origin, String destination,String mode, String key);

    }

    public interface View {
        void showGetDistanceProgress();

        void hideGetDistanceProgress();

        void showGetDistanceResults(String distance1, String s, String distance, String duration);

        void onFetchDIstanceFailed(Throwable throwable);

        void showFetchDistanceError(String message);
    }

    public interface Presenter {

        void onGetDistanceDestroy();
    }
}
