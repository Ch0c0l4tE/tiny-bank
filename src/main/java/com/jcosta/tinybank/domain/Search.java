package com.jcosta.tinybank.domain;

import java.util.List;

public class Search<T> {

    private final List<T> items;
    private final String cursor;
    private final int limit;

    public Search(List<T> items, String cursor, int limit) {
        this.items = items;
        this.cursor = cursor;
        this.limit = limit;
    }

    public List<T> getItems() {
        return items;
    }

    public String getCursor() {
        return cursor;
    }

    public int getLimit() {
        return limit;
    }
}
