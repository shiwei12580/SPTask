package org.wei.sptask.appService.network;

/**
 * Created by admin on 22/11/2016.
 */
public class AsyncException extends RuntimeException {

    public static final String NOTIFICATION_ASYNC_ERROR = "NOTIFICATION_ASYNC_ERROR";
    public static final int DEFAULT_ERROR_CODE = 0;

    protected int mErrorCode;

    public AsyncException() {
    }

    public AsyncException(String detailMessage) {
        super(detailMessage);
    }

    public AsyncException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * Provides a mechanism to report an AsyncException on a new thread, allowing
     * the current thread to continue and giving the default uncaught exception
     * handler an opportunity to take action off of the main thread
     *
     * @param detailMessage message to report
     */


    /**
     * Provides a mechanism to report an AsyncException on a new thread, allowing
     * the current thread to continue and giving the default uncaught exception
     * handler an opportunity to take action off of the main thread
     *
     * @param exception exception to report
     */
}
