package com.jcosta.tinybank.adapters.in.web.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.util.List;

public record ErrorResponse(String type, String title, @JsonProperty("invalid-params") List<InvalidParam> invalidParams) {
}
