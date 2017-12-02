package com.shit.beerburn.apps;

import com.shit.beerburn.DashBoardView;
import com.shit.beerburn.utils.BeerSharedPreferenceHelper;
import com.shit.beerburn.utils.Const;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rehan on 21/2/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class StravaManagerTest {
    private static final String JAN_1_DATE = "2016-01-01T00:00:00Z";
    @Mock
    private DashBoardView view;

    @Mock
    private BeerSharedPreferenceHelper fakBsp;

    @Before
    public void setUp() throws Exception {
        BeerSharedPreferenceHelper.setFake(fakBsp);
    }

    @Test
    public void testConstructorInitialize() throws Exception {

        String dateToday = "2016-02-21T00:00:01Z";

        when(BeerSharedPreferenceHelper.getInstance().getBeerAllTime()).thenReturn(Long.valueOf(0));
        when(BeerSharedPreferenceHelper.getInstance().getBeerToday()).thenReturn(Long.valueOf(0));
        when(BeerSharedPreferenceHelper.getInstance().getLastActivityDate()).thenReturn(dateToday);

        StravaManager myStravaManager = new StravaManager(view);

        verify(fakBsp).getBeerToday();
    }
}