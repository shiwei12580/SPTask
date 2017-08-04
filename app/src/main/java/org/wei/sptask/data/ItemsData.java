package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class ItemsData {
    @SerializedName("timestamp")
    private String mTimestamp;
    @SerializedName("update_timestamp")
    private String mUpdatedTimestamp;
    @SerializedName("readings")
    private DataReadings mData;


    public String getRequestTimeStamp() {
        return mTimestamp;
    }


    public String getUpdateTimeStamp() {
        return mUpdatedTimestamp;
    }

    public DataReadings getDataReading() {
        return mData;
    }
}
