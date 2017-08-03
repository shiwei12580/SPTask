package org.wei.sptask.appService.network.request;

import com.google.gson.GsonBuilder;

import org.wei.sptask.appService.network.CustomTypeAdapter;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by sli on 22/11/2016.
 */
public class JSONRequestBody extends LinkedHashMap<String, Object> {
    private final String mConfigBasePath;
    private GsonBuilder mGsonBuilder;

    public String getConfigBasePath(){
        return mConfigBasePath;
    }

    public JSONRequestBody() {
        mGsonBuilder = new GsonBuilder().serializeNulls();
        mConfigBasePath = "https://api.data.gov.sg/v1/environment/psi";
        putDefaults();
    }


    private void putDefaults() {

    }

    public String toJson() {
        return toJson(null);
    }

    public String toJson(List<? extends CustomTypeAdapter> customTypeAdapters) {
        if (customTypeAdapters != null) {
            for (int i = 0, size = customTypeAdapters.size(); i < size; i++) {
                final CustomTypeAdapter customTypeAdapter = customTypeAdapters.get(i);
                if (customTypeAdapter.getSerializer() != null) {
                    mGsonBuilder = mGsonBuilder.registerTypeAdapter(
                            customTypeAdapter.getType(), customTypeAdapter.getSerializer()
                    );
                }
                if (customTypeAdapter.getDeserializer() != null) {
                    mGsonBuilder = mGsonBuilder.registerTypeAdapter(
                            customTypeAdapter.getType(), customTypeAdapter.getDeserializer()
                    );
                }
            }
        }
        return mGsonBuilder.create().toJson(this);
    }


    @Override
    public String toString() {
        return toJson();
    }
}