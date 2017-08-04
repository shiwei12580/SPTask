package org.wei.sptask;

import org.junit.Before;
import org.junit.Test;
import org.wei.sptask.data.LocationData;
import org.wei.sptask.data.PSIData;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sli on 4/8/2017.
 */

public class LocationDataControllerTest {
    private LocationDataController mControllerUnderTest;

    private static final double epsilon = 0.00001;

    @Before
    public void init() {
        mControllerUnderTest = new LocationDataController();
        LocationData west = new LocationData(1.35735, 103.7);
        LocationData east = new LocationData(1.35735, 103.94);
        LocationData central = new LocationData(1.35735, 103.82);
        LocationData south = new LocationData(1.29587, 103.82);
        LocationData north = new LocationData(1.41803, 103.82);

        mControllerUnderTest.setupRegions(west, east, central, south, north);
    }

    @Test
    public void testGetBoarder() {
        List<Double> targetWestToEastBoarder = Arrays.asList(
                103.76,
                103.88
        );

        List<Double> targetSouthToNorthBoarder = Arrays.asList(
                1.32661,
                1.38769
        );

        List<Double> westToEastBoarder = mControllerUnderTest.getWestToEastBoarder();
        List<Double> southToNorthBoarder = mControllerUnderTest.getSouthToNorthBoarder();

        for (int i = 0; i < 2; i++) {
            String westToEastAssert = String.format("westToEastBoarder[%d]", i);
            assertEquals(westToEastAssert, targetWestToEastBoarder.get(i), westToEastBoarder.get(i), epsilon);
            String southToNorthAssert = String.format("southToNorthBoarder[%d]", i);
            assertEquals(southToNorthAssert, targetSouthToNorthBoarder.get(i), southToNorthBoarder.get(i), epsilon);
        }
    }

    @Test
    public void testFindMyRegion() {
        List<LocationData> testCurrentLocations = Arrays.asList(
                new LocationData(1.3250, 103.70),  // region 0
                new LocationData(1.3250, 103.80),  // region 1
                new LocationData(1.3250, 103.90),  // region 2
                new LocationData(1.350, 103.70),    // region 3
                new LocationData(1.350, 103.80),    // region 4
                new LocationData(1.350, 103.90),    // region 5
                new LocationData(1.410, 103.70),    // region 6
                new LocationData(1.410, 103.80),    // region 7
                new LocationData(1.410, 103.90)     // region 8
        );

        for (int i = 0; i < 9; i++) {
            int result = mControllerUnderTest.findMyRegion(testCurrentLocations.get(i));
            String findMyRegionAssert = String.format("findMyRegion[%d]", i);
            assertEquals(findMyRegionAssert, i, result);
        }
    }

    @Test
    public void getRegionReadings() {

        double[] targetReading = {
                53.0,
                60.0,
                59.0,
                46.0,
                57.0,
                58.0,
                52.5,
                59.0,
                58.5
        };
        PSIData data = new PSIData(46, 58, 57, 60, 59);
        for (int i = 0; i < 9; i++) {
            double result = mControllerUnderTest.getRegionReadings(i, data);
            String RegionReadingsAssert = String.format("RegionReadingsAssert[%d]", i);
            assertEquals(RegionReadingsAssert, targetReading[i], result, epsilon);
        }
    }

}
