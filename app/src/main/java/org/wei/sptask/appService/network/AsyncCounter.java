package org.wei.sptask.appService.network;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22/11/2016.
 */
public class AsyncCounter<T> {
    private int mMax = -1;
    private List<T> mObjects;
    private AsyncListener<List<T>> mListener;

    private int mCount = 0;
    private boolean finished = false;

    public AsyncCounter(int finalCount, final AsyncListener<List<T>> listener) {
        if (finalCount < 0) {
            throw new RuntimeException("Max Count < 0!");
        } else if (finalCount == 0) {
            // 0 iterations, post notification to listener immediately
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.onResponse(new ArrayList<T>(), null, null);
                }
            });

            return;
        }

        mMax = finalCount;
        mObjects = new ArrayList<T>();
        mListener = listener;
    }

    public void success(final T obj) {
        if (finished) {
            return;
        }

        mCount++;

        if (mCount > mMax) {
            throw new RuntimeException("AsyncCounter has exceeded maximum iterations: " + mCount + " > " + mMax);
        }

        if (obj != null) {
            mObjects.add(obj);
        }

        if (mCount == mMax) {
            finished = true;
            // All done, call our listener
            mListener.onResponse(mObjects, null, null);
        }
    }

    public void error(final AsyncException exception) {
        if (finished) {
            return;
        }


        finished = true;

        mListener.onResponse(null, null, exception);
    }

    public void incrementMax() {
        ++mMax;
    }

    public boolean hasActiveProcesses() {
        return mCount > 0;
    }
}
