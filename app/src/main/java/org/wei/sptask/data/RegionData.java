package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class RegionData {
    @SerializedName("name")
    private String name;
    @SerializedName("label_location")
    private LocationData location;

    public String getName() {
        return name;
    }

    public LocationData getLocationData() {
        return location;
    }
}
