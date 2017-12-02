package com.shit.beerburn;

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
    private BeerSharedPreferenceHelper bspHelper;

    @Mock
    private StravaManager mStravaManager;
    private  DashBoardPresenter dashBoardPresenter;

    @org.junit.Before
    public void setUp() throws Exception {
        when(bspHelper.getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter = new DashBoardPresenter(view, bspHelper);
        dashBoardPresenter.setTrackingManager(mStravaManager);
    }

    @Test
    public void testOnRefreshShouldLaunchSignInOnEmptyToken() throws Exception {
        when(bspHelper.getTrackerToken()).thenReturn("");
        when(bspHelper.getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter.onRefresh();

        verify(view).startSignInActivity();

    }

    @Test
    public void testOnRefreshShouldUpdate() throws Exception {
        when(bspHelper.getTrackerToken()).thenReturn("1234");
        when(bspHelper.getTrackerType()).thenReturn(Const.TRACKER_TYPE.STRAVA);
        dashBoardPresenter.onRefresh();

        verify(mStravaManager).initialize();
    }
}