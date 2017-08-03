package org.wei.sptask.appService.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sli on 22/11/2016.
 */
public class JSONResponse<T>{
    @SerializedName("data")
    private T mData;
    @SerializedName("returnCode")
    private int mReturnCode;

    public T getData() {
        return mData;
    }

    public void setData(final T data) {
        mData = data;
    }

    public int getResultCode() {
        return mReturnCode;
    }

    public void setResultCode(final int resultCode) {
        mReturnCode = resultCode;
    }


    @Override
    public String toString() {
        return "MWJSONResponse{" +
                "mData=" + mData +
                ", mResultCode=" + mReturnCode +
                "}";
    }
}
