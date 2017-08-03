package org.wei.sptask.google;

/**
 * Created by admin on 12/9/2016.
 */
public interface GeocodingAsyncTaskListener {

    void onResponse(Double latitude, Double longitude, String errorMsg);
}