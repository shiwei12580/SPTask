package org.wei.sptask.appService.network.request;

import org.wei.sptask.appService.network.CustomTypeAdapter;
import org.wei.sptask.appService.network.RequestProvider;
import org.wei.sptask.appService.network.response.GetPSIDataResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 4/8/2017.
 */
public class PSIReadingRequest implements RequestProvider<GetPSIDataResponse, JSONRequestBody> {

    private static final String URL_PATH = "/environment/psi";
    private final JSONRequestHeader mHeaderMap;
    private final String mUrl;
    private GETQueryArgs mQueryArgs;

    public PSIReadingRequest(String date_time, String date){
        mHeaderMap = new JSONRequestHeader();
        if(date_time != null) {
            mQueryArgs.put("date_time", date_time);
        }
        if(date != null) {
            mQueryArgs.put("date", date_time);
        }
        mUrl = mHeaderMap.getConfigBasePath() + URL_PATH;

    }
    @Override
    public MethodType getMethodType() {
        return MethodType.GET;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.JSON;
    }

    @Override
    public String getURLString() {
        return mUrl + mQueryArgs.toString();
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public void setBody(JSONRequestBody body) {

    }

    @Override
    public Class<GetPSIDataResponse> getResponseClass() {
        return GetPSIDataResponse.class;
    }

    @Override
    public List<? extends CustomTypeAdapter> getCustomTypeAdapters() {
        return null;
    }

    @Override
    public String toString() {
        return "MWGetAddressBookRequest{" +
                "mHeaderMap=" + mHeaderMap +
                ", mQueryArgs=" + mQueryArgs +
                ", mUrl=\"" + mUrl + "\"" +
                "}";
    }

}
