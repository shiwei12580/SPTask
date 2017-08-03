package org.wei.sptask.appService.network;

import java.util.UUID;

/**
 * Created by admin on 22/11/2016.
 */
public class AsyncToken {
    private final String mPrefix;
    private final UUID mUUID;

    public AsyncToken(String prefix) {
        mPrefix = prefix;
        mUUID = UUID.randomUUID();
    }

    public String getPrefix() {
        return mPrefix;
    }

    public UUID getUUID() {
        return mUUID;
    }

    @Override
    public String toString() {
        return (mPrefix == null ? "" : mPrefix + ".") + mUUID.toString();
    }
}
