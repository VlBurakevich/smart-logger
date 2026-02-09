package com.solution.apigateway.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class HeaderModifierWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> newHeaders = new HashMap<>();
    private final Set<String> removedHeaders = new HashSet<>();

    public HeaderModifierWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        this.newHeaders.put(name, value);
    }

    public void removeHeader(String name) {
        this.removedHeaders.add(name);
    }

    public void replace(String oldName, String newName, String value) {
        removeHeader(oldName);
        addHeader(newName, value);
    }

    @Override
    public String getHeader(String name) {
        if (removedHeaders.contains(name)) {
            return null;
        }
        return newHeaders.getOrDefault(name, super.getHeader(name));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names  = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        Enumeration<String> originNames = super.getHeaderNames();
        while (originNames.hasMoreElements()) {
            String name = originNames.nextElement();
            if (!removedHeaders.contains(name)) {
                names.add(name);
            }
        }
        names.addAll(newHeaders.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (removedHeaders.contains(name)) {
            return Collections.emptyEnumeration();
        }
        if (newHeaders.containsKey(name)) {
            return Collections.enumeration(Collections.singletonList(newHeaders.get(name)));
        }
        return super.getHeaders(name);
    }
}
