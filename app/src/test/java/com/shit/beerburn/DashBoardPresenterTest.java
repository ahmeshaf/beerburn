package com.shit.beerburn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shit.beerburn.apps.StravaManager;
import com.shit.beerburn.apps.TrackingManager;
import com.shit.beerburn.utils.BeerSharedPreferenceHelper;
import com.shit.beerburn.utils.Const;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ahmeshaf on 2/6/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DashBoardPresenterTest {

    @Mock
    private DashBoardView view;

    @Mock
    private BeerSharedPreferenceHelper fakBsp;

    @Mock
    private StravaManager mStravaManager;

    @Mock
    Context someContext;
    private  DashBoardPresenter dashBoardPresenter;

    @org.junit.Before
    public void setUp() throws Exception {

        BeerSharedPreferenceHelper.setFake(fakBsp);
//        when(BeerSharedPreferenceHelper.getInstance()).thenReturn(new BeerSharedPreferenceHelper());
        when(BeerSharedPreferenceHelper.getInstance().getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter = new DashBoardPresenter(view);
        dashBoardPresenter.setTrackingManager(mStravaManager);
    }

    @Test
    public void testOnRefreshShouldLaunchSignInOnEmptyToken() throws Exception {
        when(BeerSharedPreferenceHelper.getInstance().getTrackerToken()).thenReturn("");
        when(BeerSharedPreferenceHelper.getInstance().getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter.onRefresh();

        verify(view).startSignInActivity();

    }

    @Test
    public void testOnRefreshShouldUpdate() throws Exception {
        when(BeerSharedPreferenceHelper.getInstance().getTrackerToken()).thenReturn("1234");
        when(BeerSharedPreferenceHelper.getInstance().getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter.onRefresh();

        verify(mStravaManager).initiate();
    }
}