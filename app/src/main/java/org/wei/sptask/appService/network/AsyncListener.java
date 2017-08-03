package org.wei.sptask.appService.network;

/**
 * Created by admin on 22/11/2016.
 */
public interface AsyncListener<T> {
    public void onResponse(final T response, final AsyncToken token, AsyncException exception);
}
