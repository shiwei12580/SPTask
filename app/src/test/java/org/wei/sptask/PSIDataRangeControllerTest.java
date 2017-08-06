package org.wei.sptask;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by admin on 6/8/2017.
 */
public class PSIDataRangeControllerTest {
    private PSIDataRangeController mControllerUnderTest;
    private static final double epsilon = 0.00001;
    @Before
    public void init() {
        mControllerUnderTest = new PSIDataRangeController();
    }

    @Test
    public void testGetPSILevel() {
        List<Double> testData = Arrays.asList(
                25.0,
                75.0,
                150.0,
                250.0,
                350.0
        );

        for (int i = 0; i < 5; i++) {
            int result = mControllerUnderTest.getPSILevel(testData.get(i));
            String getPSILevelAssert = String.format("getPSILevel[%d]", i);
            assertEquals(getPSILevelAssert, i, result);
        }
    }
}
