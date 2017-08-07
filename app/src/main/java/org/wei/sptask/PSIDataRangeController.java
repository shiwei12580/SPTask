package org.wei.sptask;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 6/8/2017.
 */
public class PSIDataRangeController {

    public static final int Good = 0;
    public static final int Moderate = 0;
    public static final int Unhealthy = 0;
    public static final int VeryUnhealthy = 0;
    public static final int Hazardous = 0;


    public List<Double> gaps;

    public PSIDataRangeController() {
        gaps  =  Arrays.asList(
                (double) 51,
                (double) 101,
                (double) 201,
                (double) 301
        );
    }

    public int getPSILevel(double num) {
        int level = -1;
        for (int i = 0; i < gaps.size(); i++) {
            if (num < gaps.get(i)) {
                level = i;
                break;
            } else if (i + 1 < gaps.size() && num < gaps.get(i + 1)) {
                level = i + 1;
                break;
            } else if ( i == gaps.size() -1){
                level = i + 1;
                break;
            } else {
                continue;
            }
        }
        return level;
    }
}
