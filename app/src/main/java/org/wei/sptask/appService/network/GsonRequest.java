package org.wei.sptask.appService.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Request object to be used with Volley
 *
 * @param <V> Request
 * @param <E> Body
 */
public final class GsonRequest<V, E> extends Request<V> {

    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE_KEY = "Content-Type";
    private static final String PROTOCOL_CONTENT_TYPE = "application/json";
    private static final String LANGUAGE_NAME_FORMAT = "\"languageName\":\"%s\"";
    private static final Pattern LANGUAGE_PATTERN = Pattern.compile("\"languageName\":\".*?\"");
    private static final int ERROR_LANGUAGE_NOT_SUPPORTED = -1030;
    private static final int ERROR_INVALID_TOKEN = -1037;
    public static final String HEADER_PARAM_TOKEN = "Token";

    private final Context mContext;
    private final RequestProvider<V, E> mRequestProvider;
    private final AsyncListener<V> mAsyncListener;
    private boolean mSkipListener;
    private final Gson mGson;
    private final Class<V> mClazz;
    private final Map<String, String> mHeaders;

    /**
     * Creates a new request with the given method
     * @param context {@link Context}
     * @param method {@link int} Supported request methods {@link com.android.volley.Request.Method}.
     * @param provider {@link RequestProvider}  Defines the interface used by the RequestManager to query for
     * details needed to issue a request and process a response
     * @param listener {@link AsyncListener}  This interface provides a generic callback method signature that is used throughout the SDK
     * to return arguments on success or exception when they occur during asynchronous routines
     */
    public GsonRequest(final Context context, final int method,
                       final RequestProvider<V, E> provider, final AsyncListener<V> listener) {

        super(method, provider.getURLString(),
                new GsonResponseErrorListenerWrapper(context, listener));


        mContext = context;
        mRequestProvider = provider;
        mAsyncListener = listener;

        GsonBuilder gsonBuilder = new GsonBuilder();
        List<? extends CustomTypeAdapter> customTypeAdapters = provider.getCustomTypeAdapters();
        if (customTypeAdapters != null) {
            for (CustomTypeAdapter customTypeAdapter : customTypeAdapters) {

                if (customTypeAdapter.getSerializer() != null) {

                    gsonBuilder = gsonBuilder.registerTypeAdapter(customTypeAdapter.getType(),
                            customTypeAdapter.getSerializer());
                }

                if (customTypeAdapter.getDeserializer() != null) {

                    gsonBuilder = gsonBuilder.registerTypeAdapter(customTypeAdapter.getType(),
                            customTypeAdapter.getDeserializer());
                }
            }
        }

        mGson = gsonBuilder.create();
        mClazz = provider.getResponseClass();
        mHeaders = provider.getHeaders() == null ?
                new HashMap<String, String>() :
                provider.getHeaders();

        // Hack to fix Middleware issue
        // Leave in until fixed. (See GMA-7293 & GMA-7294)
        if (provider.getBody() == null) {
            mHeaders.put(PROTOCOL_CONTENT_TYPE_KEY, PROTOCOL_CONTENT_TYPE);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<V> parseNetworkResponse(NetworkResponse response) {

        try {

            final ByteArrayInputStream bis = new ByteArrayInputStream(response.data);
            final InputStreamReader reader = new InputStreamReader(bis);
            final V data = mGson.fromJson(reader, mClazz);

            final Response<V> parsed = Response.success(data,
                    HttpHeaderParser.parseCacheHeaders(response));

            checkForErrors(parsed.result);
            return parsed;

        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (NullPointerException npe) {
            return Response.error(new ParseError(npe));
        }
    }

    @Override
    protected void deliverResponse(V response) {
        if (mAsyncListener != null && !mSkipListener) {
            mAsyncListener.onResponse(response, null, null);
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        try {
            return mRequestProvider.getBody() == null ? null : mRequestProvider
                    .getBody().getBytes(PROTOCOL_CHARSET);

        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }



    private void checkForErrors(V result) {

        final int resultCode = getResultCode(result);
        if (resultCode == ERROR_LANGUAGE_NOT_SUPPORTED) {
            retryWithDefaultLanguage();
        } else if (resultCode == ERROR_INVALID_TOKEN) {
            updateToken();
        }
    }

    private void updateToken(){
        //TODO update token
    }

    private static int getResultCode(Object response) {

        if (response instanceof AsyncException) {
            return ((AsyncException) response).getErrorCode();
        }

        return -1;
    }

    private void retryWithDefaultLanguage() {

        mSkipListener = true;

        final String originalBody = mRequestProvider.getBody();
        final String languageName = "EN";
                // Configuration.getSharedInstance().getLocalization().getDefaultLanguage();

        final Matcher matcher = LANGUAGE_PATTERN.matcher(originalBody);
        final String newLanguageName = String.format(LANGUAGE_NAME_FORMAT, languageName);
        final String newBody = matcher.replaceFirst(newLanguageName);

        RequestManager.register(mContext).processRequest(fromProvider(newBody), mAsyncListener);
    }





    @NonNull
    private RequestProvider<V, E> fromProvider(final String body) {

        return new RequestProvider<V, E>() {

            @Override
            public MethodType getMethodType() {
                return mRequestProvider.getMethodType();
            }

            @Override
            public RequestType getRequestType() {
                return mRequestProvider.getRequestType();
            }

            @Override
            public String getURLString() {
                return mRequestProvider.getURLString();
            }

            @Override
            public Map<String, String> getHeaders() {
                return mRequestProvider.getHeaders();
            }

            @Override
            public String getBody() {
                return body;
            }

            @Override
            public void setBody(E body) {

            }

            @Override
            public Class<V> getResponseClass() {
                return mRequestProvider.getResponseClass();
            }

            @Override
            public List<? extends CustomTypeAdapter> getCustomTypeAdapters() {
                return mRequestProvider.getCustomTypeAdapters();
            }
        };
    }


    /*
     * The GsonResponseErrorListenerWrapper proxies the Volley error responses back to
     * our own listener
     */
    private static class GsonResponseErrorListenerWrapper implements Response.ErrorListener {

        private static final String DEBUG_ERROR_FORMAT = "Network Error:\nStatus Code: %s\nCause: %s";

        private final AsyncListener mListener;
        private final Context mContext;

        public GsonResponseErrorListenerWrapper(final Context context, final AsyncListener listener) {

            mContext = context;
            mListener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (mListener != null) {
                mListener.onResponse(null, null, new AsyncException(error.getMessage()));
            }
        }



        private String trimMessage(String json, String key){
            String trimmedString = null;

            try{
                JSONObject obj = new JSONObject(json);
                trimmedString = obj.getString(key);
            } catch(JSONException e){
                e.printStackTrace();
                return null;
            }

            return trimmedString;
        }
    }
}