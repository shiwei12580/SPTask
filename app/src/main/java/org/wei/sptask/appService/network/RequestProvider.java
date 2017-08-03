package org.wei.sptask.appService.network;

import java.util.List;
import java.util.Map;

/**
 * Defines the interface used by the RequestManager to query for
 * details needed to issue a request and process a response
 *
 * @param <T> Response type
 * @param <E> Body type
 */
public interface RequestProvider<T, E> {
    MethodType getMethodType();

    RequestType getRequestType();

    String getURLString();

    Map<String, String> getHeaders();

    String getBody();

    void setBody(E body);

    Class<T> getResponseClass();

    List<? extends CustomTypeAdapter> getCustomTypeAdapters();

    String toString();

    /**
     * Decouples our method type
     * so we can use a different framework
     * if required
     */
    enum MethodType {
        GET,
        POST,
        PUT,
        DELETE
    }

    /**
     * Type of request
     */
    enum RequestType {
        JSON,
        MMAP_JSON,
        IMAGE
    }
}
