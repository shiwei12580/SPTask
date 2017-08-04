package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class DataReadings {
    @SerializedName("o3_sub_index")
    private PSIData o3_sub_index;
    @SerializedName("pm10_twenty_four_hourly")
    private PSIData pm10_twenty_four_hourly;
    @SerializedName("pm10_sub_index")
    private PSIData pm10_sub_index;
    @SerializedName("co_sub_index")
    private PSIData co_sub_index;
    @SerializedName("pm25_twenty_four_hourly")
    private PSIData pm25_twenty_four_hourly;
    @SerializedName("so2_sub_index")
    private PSIData so2_sub_index;
    @SerializedName("co_eight_hour_max")
    private PSIData co_eight_hour_max;
    @SerializedName("no2_one_hour_max")
    private PSIData no2_one_hour_max;
    @SerializedName("so2_twenty_four_hourly")
    private PSIData so2_twenty_four_hourly;
    @SerializedName("pm25_sub_index")
    private PSIData pm25_sub_index;
    @SerializedName("psi_twenty_four_hourly")
    private PSIData psi_twenty_four_hourly;
    @SerializedName("o3_eight_hour_max")
    private PSIData o3_eight_hour_max;


    public PSIData getPSI24HourlyData() {
        return psi_twenty_four_hourly;

    }
}
