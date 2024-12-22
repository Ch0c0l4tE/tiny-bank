package com.jcosta.tinybank.domain;

import java.util.List;

public class Search<T> {

    private final List<T> items;
    private final String cursor;

    public Search(List<T> items, String cursor) {
        this.items = items;
        this.cursor = cursor;
    }

    public List<T> getItems() {
        return items;
    }

    public String getCursor() {
        return cursor;
    }
}
