package org.wei.sptask.google;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.wei.sptask.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by admin on 12/9/2016.
 */
public class GeocodingAsyncTask extends AsyncTask<Object, Void, Address> {
    private GeocodingAsyncTaskListener mListener;
    private Context mContext;

    @Override
    /**
     * params:
     * String address
     * Double radius
     * AsyncListener<List<Store>> listener
     * Object token
     */
    protected Address doInBackground(Object... params) {
        if (params.length != 3) {
            return null;
        }

        String address = (String) params[0];
        mContext = (Context) params[1];
        mListener = (GeocodingAsyncTaskListener) params[2];

        Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);

        try {
            List<Address> results = geocoder.getFromLocationName(address, 1);
            return (results.size() > 0) ? results.get(0) : null;
        } catch (IOException ioe) {
            // IOException when network operation could not be completed
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Address address) {
        if (address != null) {
            mListener.onResponse(address.getLatitude(), address.getLongitude(), null);
        } else {
            mListener.onResponse(null, null, mContext.getString(R.string.error_could_not_determine_address));
        }
    }

}
