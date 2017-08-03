

package org.wei.sptask.appService.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;


public final class RequestManager {
    private RequestQueue mRequestQueue;

    private static RequestManager sInstance;
    //private static RequestManagerServiceConnection networkService;

    private Context mContext;

    public static RequestManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RequestManager(context);
        }
        return sInstance;
    }

    public RequestManager(Context context) {
        mContext = context;
        mRequestQueue = createAndStartRequestQueue();
    }

    /**
     * Registers the Service with a {@link android.content.Context}
     *
     * @param context Context with which to bind the service

     */
    public static RequestManagerServiceConnection register(Context context) {
        /*
        if(networkService == null){
            networkService = new RequestManagerServiceConnection(getInstance(McDonalds.getContext()));
        }

        return networkService;
        */
        return new RequestManagerServiceConnection(getInstance(context));
    }

    /**
     * Unbinds the ServiceConnection from the given Context
     *
     * @param context    Context with which to unbind the service

     */
    public static void unregister(Context context, RequestManagerServiceConnection connection) {
        // Not needed
    }

    /**
     * Add a query into a request dispatch queue.
     *
     * @param provider {@link RequestProvider} used by the RequestManager to query for
     *                 details needed to issue a request and process a response
     * @param listener {@link AsyncListener} a generic callback method signature that is used throughout the SDK
     *                 to return arguments on success or exception when they occur during asynchronous routines
     * @param <T>      Response type
     * @param <E>      Body type
     */
    public <T, E> void processRequest(RequestProvider<T, E> provider, AsyncListener listener) {
        // determine our method for volley from our supported method types
        int method;
        switch (provider.getMethodType()) {
            case GET:
                method = Request.Method.GET;
                break;
            case POST:
                method = Request.Method.POST;
                break;
            case PUT:
                method = Request.Method.PUT;
                break;
            case DELETE:
                method = Request.Method.DELETE;
                break;
            default:
                throw new IllegalArgumentException("You must specify an HTTP method");
        }

        switch (provider.getRequestType()) {
            case JSON:
                GsonRequest<T, E> gsonRequest = new GsonRequest<>(mContext, method, provider, listener);
                gsonRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 2, 1.0f));
                gsonRequest.setShouldCache(false);
                mRequestQueue.add(gsonRequest);
                break;

            case IMAGE:
                break;

            default:
                throw new IllegalArgumentException("You must specify a Request Type");
        }
    }

    private RequestQueue createAndStartRequestQueue() {


        return Volley.newRequestQueue(mContext);
    }

}
