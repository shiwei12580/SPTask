package org.wei.sptask.appService.network.response;

import com.google.gson.annotations.SerializedName;

import org.wei.sptask.data.ApiStatus;
import org.wei.sptask.data.ItemsData;
import org.wei.sptask.data.RegionData;

import java.util.List;

/**
 * Created by admin on 4/8/2017.
 */
public class GetPSIDataResponse {
    @SerializedName("region_metadata")
    private List<RegionData> mRegionData;
    @SerializedName("items")
    private List<ItemsData> mItemsData;
    @SerializedName("api_info")
    private ApiStatus mApiStatus;

    public List<RegionData> getRegionData() {
        return mRegionData;
    }

    public List<ItemsData> getItemData() {
        return mItemsData;
    }

    public ApiStatus getApiStatus() {
        return mApiStatus;
    }

}
