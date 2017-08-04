package org.wei.sptask.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 4/8/2017.
 */

public class ApiStatus {

    @SerializedName("status")
    private String mStatus;

    public static final String HEALTH = "healthy";
    public String getStatus() {
        return mStatus;
    }
}
