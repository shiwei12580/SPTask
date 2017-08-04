package org.wei.sptask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.wei.sptask.appService.network.response.GetPSIDataResponse;

import static junit.framework.Assert.fail;

/**
 * Created by sli on 4/8/2017.
 */

public class GetPSIDataResponseTest {

    private static String JSONSample;
    private GetPSIDataResponse response;
    private Gson mGson;

    @Test
    public void parse() {
        JSONSample = "{\"region_metadata\":[{\"name\":\"west\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.7}},{\"name\":\"national\",\"label_location\":{\"latitude\":0,\"longitude\":0}},{\"name\":\"east\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.94}},{\"name\":\"central\",\"label_location\":{\"latitude\":1.35735,\"longitude\":103.82}},{\"name\":\"south\",\"label_location\":{\"latitude\":1.29587,\"longitude\":103.82}},{\"name\":\"north\",\"label_location\":{\"latitude\":1.41803,\"longitude\":103.82}}],\"items\":[{\"timestamp\":\"2017-08-03T15:00:00+08:00\",\"update_timestamp\":\"2017-08-03T15:06:17+08:00\",\"readings\":{\"o3_sub_index\":{\"west\":22,\"national\":25,\"east\":23,\"central\":24,\"south\":25,\"north\":24},\"pm10_twenty_four_hourly\":{\"west\":33,\"national\":34,\"east\":34,\"central\":28,\"south\":33,\"north\":31},\"pm10_sub_index\":{\"west\":33,\"national\":34,\"east\":34,\"central\":28,\"south\":33,\"north\":31},\"co_sub_index\":{\"west\":3,\"national\":5,\"east\":3,\"central\":5,\"south\":4,\"north\":5},\"pm25_twenty_four_hourly\":{\"west\":11,\"national\":20,\"east\":19,\"central\":17,\"south\":20,\"north\":19},\"so2_sub_index\":{\"west\":17,\"national\":17,\"east\":5,\"central\":5,\"south\":3,\"north\":4},\"co_eight_hour_max\":{\"west\":0.29,\"national\":0.51,\"east\":0.3,\"central\":0.46,\"south\":0.41,\"north\":0.51},\"no2_one_hour_max\":{\"west\":6,\"national\":20,\"east\":10,\"central\":15,\"south\":6,\"north\":20},\"so2_twenty_four_hourly\":{\"west\":27,\"national\":27,\"east\":8,\"central\":9,\"south\":5,\"north\":6},\"pm25_sub_index\":{\"west\":46,\"national\":60,\"east\":58,\"central\":57,\"south\":60,\"north\":59},\"psi_twenty_four_hourly\":{\"west\":46,\"national\":60,\"east\":58,\"central\":57,\"south\":60,\"north\":59},\"o3_eight_hour_max\":{\"west\":51,\"national\":58,\"east\":54,\"central\":57,\"south\":58,\"north\":57}}}],\"api_info\":{\"status\":\"healthy\"}}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        mGson = gsonBuilder.create();
        response = mGson.fromJson(JSONSample ,GetPSIDataResponse.class);
        if(response == null) {

            fail("parse error");
        }
    }
}
