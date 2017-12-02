package com.shit.beerburn;

import com.shit.beerburn.apps.StravaManager;
import com.shit.beerburn.apps.TrackingManager;
import com.shit.beerburn.utils.BeerSharedPreferenceHelper;
import com.shit.beerburn.utils.Const;

import static com.shit.beerburn.utils.Const.TRACKER_TYPE.STRAVA;

/**
 * Created by ahmeshaf on 2/6/2016.
 */
public class DashBoardPresenter {
    private final DashBoardView dashBoardView;
    private TrackingManager mTrackingManager;

    public DashBoardPresenter(DashBoardView view){
        this.dashBoardView = view;
        setTrackingManager();
    }

    private void setTrackingManager() {
        switch(BeerSharedPreferenceHelper.getInstance().getTrackerType()){
            case STRAVA:
                mTrackingManager = new StravaManager(dashBoardView);
                break;
        }
    }

    public void setTrackingManager(TrackingManager trackingManager) {
        this.mTrackingManager = trackingManager;
    }

    public void onRefresh() {
        String token = BeerSharedPreferenceHelper.getInstance().getTrackerToken();
        String clientCode = BeerSharedPreferenceHelper.getInstance().getTrackerClientCode();
        if (token.isEmpty()&&clientCode.isEmpty()) {
            dashBoardView.startSignInActivity();
        }
        else  {
            mTrackingManager.initiate();
        }
    }
}
