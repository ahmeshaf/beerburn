package com.shit.beerburn.utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ahmeshaf on 2/7/2016.
 */
public class DateTimeHelperTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetLocalFormattedDate() throws Exception {
        String isoTestDate = "2016-01-01T00:00:00Z";
        String expectedDate = "Jan 01 2016  00:00 AM";
        String actualDate = DateTimeHelper.getLocalFormattedDate(isoTestDate);

        Assert.assertEquals(expectedDate, actualDate);

        isoTestDate = "2016-1-1T12:00:00Z";
        expectedDate = "Jan 01 2016  12:00 PM";
        actualDate = DateTimeHelper.getLocalFormattedDate(isoTestDate);

        Assert.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetEpochSeconds() throws Exception {
        String isoTestDate = "2016-01-01T00:00:00Z";
        Long epochSecondsOfTestDate = DateTimeHelper.getEpochSecondsOf(isoTestDate);
        Long expectedValue = Long.valueOf(1451606400);
        Long actualValue = epochSecondsOfTestDate;
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDateIsToday() throws Exception {
        String isoDate = "2016-02-21T00:00:01Z";

        Assert.assertTrue(DateTimeHelper.isDayOfDateToday(isoDate));

    }

    @Test
    public void testDateIsNotToday() throws Exception {
        String isoDate = "2016-01-01T00:00:01Z";

        Assert.assertFalse(DateTimeHelper.isDayOfDateToday(isoDate));

    }
}