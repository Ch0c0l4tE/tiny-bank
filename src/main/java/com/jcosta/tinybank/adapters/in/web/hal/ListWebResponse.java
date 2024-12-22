package com.jcosta.tinybank.adapters.in.web.hal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record ListWebResponse<T>(List<T> embedded, @JsonProperty("_links") Map<String, Link> links) {
}
