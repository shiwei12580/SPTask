package org.wei.sptask.appService.network;

import android.os.Binder;


/**
 * Our service is not exported and therefore we can use a simple IBinder
 * that has one method for returning an instances of our Service within
 * the same process
 */
public final class RequestManagerBinder extends Binder {
    private final RequestManager mManager;

    /**
     * A constructor
     * @param manager an instance of {@link RequestManager}
     */
    public RequestManagerBinder(RequestManager manager) {
        mManager = manager;
    }

    /**
     * Return the an instance of {@link RequestManager}
     * @return an instance of {@link RequestManager}
     */
    public RequestManager getManager() {
        return mManager;
    }
}
