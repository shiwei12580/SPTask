package org.wei.sptask.appService.network;

/**
 * Interface for processing non-standard Json objects
 * @param <T> Type for Gson
 */
public interface CustomTypeAdapter<T> {
    public Class<T> getType();

    public Object getDeserializer();

    public Object getSerializer();
}
