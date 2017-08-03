package org.wei.sptask.appService.network;

import com.android.volley.toolbox.NetworkImageView;

import java.util.LinkedHashMap;


/**
 * ServiceConnection used for Volley networking
 */
public class RequestManagerServiceConnection {

    private static final String TAG = RequestManagerServiceConnection.class.getSimpleName();
    private static final String CONNECTOR_KEY_MIDDLEWARE = "Middleware";

    private RequestManager mManager;
    private LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>> mRequestQueue = new LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>>();
    private LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>> mTokenRequestQueue = new LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>>();
    private LinkedHashMap<String, ImageRequest> mImageRequestQueue = new LinkedHashMap<String, ImageRequest>();
    private boolean mWaitingForToken;

    /**
     * A constructor
     *
     * @param manager {@link RequestManager}
     */
    public RequestManagerServiceConnection(RequestManager manager) {
        mManager = manager;
        flushRequestQueue();
        flushImageQueue();
    }

    private void flushImageQueue() {

        LinkedHashMap<String, ImageRequest> tmpImageRequestQueue =
                (LinkedHashMap<String, ImageRequest>) mImageRequestQueue.clone();

        mImageRequestQueue.clear();
    }

    private void flushRequestQueue() {

        // if we have any pending requests, prior to binding, process them now
        LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>> temp =
                (LinkedHashMap<RequestProvider<?, ?>, AsyncListener<?>>) mRequestQueue.clone();

        mRequestQueue.clear();

        for (RequestProvider provider : temp.keySet()) {
            processRequest(provider, temp.get(provider));
        }

        mWaitingForToken = false;
    }

    /**
     * Add a query into a request dispatch queue.
     *
     * @param provider {@link RequestProvider} used by the RequestManager to query for
     *                 details needed to issue a request and process a response
     * @param listener {@link AsyncListener} provides a generic callback method signature that is used throughout the SDK
     *                 to return arguments on success or exception when they occur during asynchronous routines
     */
    public void processRequest(RequestProvider provider, AsyncListener listener) {

        if (mManager != null) {


            mManager.processRequest(provider, listener);


        } else {
            mRequestQueue.put(provider, listener);
        }
    }


    class ImageRequest {
        String url;
        NetworkImageView networkImageView;
        int defaultImageResId;
        int errorImageResId;

        public ImageRequest(String url, NetworkImageView networkImageView, int defaultImageResId, int errorImageResId) {
            this.url = url;
            this.networkImageView = networkImageView;
            this.defaultImageResId = defaultImageResId;
            this.errorImageResId = errorImageResId;
        }
    }

    class AsyncListenerWrapper<T> implements AsyncListener<T> {

        private AsyncListener<T> mOrigin;
        private AsyncCounter<Void> mCounter;

        public AsyncListenerWrapper(AsyncListener<T> origin, AsyncCounter<Void> counter) {
            mOrigin = origin;
            mCounter = counter;
        }

        @Override
        public void onResponse(T response, AsyncToken token, AsyncException exception) {

            mCounter.success(null);
            if (mOrigin != null) {
                mOrigin.onResponse(response, token, exception);
            }
        }
    }
}