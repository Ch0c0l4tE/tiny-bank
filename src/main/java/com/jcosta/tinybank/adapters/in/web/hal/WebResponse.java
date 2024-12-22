package com.jcosta.tinybank.adapters.in.web.hal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.Map;

public record WebResponse<T>(@JsonUnwrapped T embedded, @JsonProperty("_links") Map<String, Link> links) {
}
