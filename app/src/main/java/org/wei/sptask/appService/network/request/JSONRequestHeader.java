package org.wei.sptask.appService.network.request;

import java.util.LinkedHashMap;

/**
 * Created by sli on 22/11/2016.
 */
public class JSONRequestHeader  extends LinkedHashMap<String, String> {

    private final String JSON_CONTENT_TYPE = "application/json";
    private final String API_KEY = "fCqNm4OJzzELQImm8pHAUKbl5wfr6ZSl";
    private final String mConfigBasePath;


    public JSONRequestHeader() {
        mConfigBasePath = "https://api.data.gov.sg/v1/environment/psi";
        putDefaults(null, null);
    }



    public JSONRequestHeader(final String token) {
        mConfigBasePath = "https://api.data.gov.sg/v1/environment/psi";
        putDefaults(token, null);
    }



    public JSONRequestHeader(final String ecpToken, final String nonce) {
        mConfigBasePath = "https://api.data.gov.sg/v1/environment/psi";
        putDefaults(ecpToken, nonce);
    }

    void putDefaults(final String token, final String nonce) {


        put("api-key", API_KEY);
        put("Content-Type", JSON_CONTENT_TYPE);

        if (token != null) {
            put("Token", token);
        }
        if (nonce != null) {
            put("Nonce", nonce);
        }
    }

}
