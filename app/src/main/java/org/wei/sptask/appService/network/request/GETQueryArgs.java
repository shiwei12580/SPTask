package org.wei.sptask.appService.network.request;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by admin on 4/8/2017.
 */
public class GETQueryArgs extends LinkedHashMap<String, Object> {

    private static final int AVG_QUERY_ARGS_PAIR_LENGTH = 5;


    @Override
    public String toString() {
        final List<QueryArgsPair> queryArgs = new ArrayList<>();
        createQueryArgsPairs(new Stack<String>(), queryArgs, this);

        return queryArgs.isEmpty() ? "" : encode(queryArgs);
    }

    public List<String> getQueryPropertyNames() {
        return new ArrayList<>(keySet());
    }

    public Object getQueryProperty(String name) {
        return get(name);
    }


    private void createQueryArgsPairs(final Stack<String> pathComponents,
                                      final List<QueryArgsPair> queryArgs,
                                      final GETQueryArgs obj) {

        if (obj != this) {
            throw new RuntimeException("Complex Query Parameters not supported.");
        }

        final List<String> queryPropertyNames = obj.getQueryPropertyNames();
        for (int i = 0, size = queryPropertyNames.size(); i < size; i++) {
            final String key = queryPropertyNames.get(i);
            pathComponents.push(key);
            final Object value = obj.getQueryProperty(key);

            if (value instanceof List) {
                createQueryArgsPairs(pathComponents, queryArgs, (List) value);
            } else if (value instanceof GETQueryArgs) {
                createQueryArgsPairs(pathComponents, queryArgs, (GETQueryArgs) value);
            } else {
                // everything else falls under toString()
                final String name = createArgName(pathComponents);
                final String valueStr = (value == null ? "" : value.toString());

                queryArgs.add(new QueryArgsPair(name, valueStr));
            }

            pathComponents.pop();
        }
    }

    private void createQueryArgsPairs(final Stack<String> pathComponents,
                                      final List<QueryArgsPair> queryArgs,
                                      final List list) {
        if (list.isEmpty()) {
            return;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append('[');

        for (int i = 0, size = list.size(); i < size; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(list.get(i).toString());
        }

        builder.append(']');

        final String name = createArgName(pathComponents);
        final String valueStr = builder.toString();

        queryArgs.add(new QueryArgsPair(name, valueStr));
    }


    private String createArgName(final Stack<String> pathComponents) {
        if (pathComponents.isEmpty()) {
            return null;
        }

        final StringBuilder builder = new StringBuilder(pathComponents.get(0));
        for (int i = 1, size = pathComponents.size(); i < size; i++) {
            builder.append('[');
            builder.append(pathComponents.get(i));
            builder.append(']');
        }

        return builder.toString();
    }

    private String encode(List<QueryArgsPair> queryArgs) {
        int size = queryArgs.size();

        StringBuilder query = new StringBuilder(size * AVG_QUERY_ARGS_PAIR_LENGTH);
        query.append('?');

        for (int i = 0; i < size; i++) {
            final QueryArgsPair param = queryArgs.get(i);

            if (i > 0) {
                query.append('&');
            }
            query.append(String.format("%s=%s", param.getName(), param.getValue()));
        }

        return query.toString();
    }


    private static class QueryArgsPair {
        private String mName = null;
        private String mValue = null;

        QueryArgsPair(final String name, final String value) {
            mName = name;
            mValue = value;
        }

        public String getName() {
            return mName;
        }

        public String getValue() {
            return mValue;
        }
    }
}
