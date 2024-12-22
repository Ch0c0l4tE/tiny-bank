package com.jcosta.tinybank.config.di;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import com.jcosta.tinybank.domain.exceptions.InvalidParam;

import java.io.IOException;

public class StrictLongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        double value = p.getDoubleValue();
        if (value % 1 != 0) {
            throw new DomainException(
                    ExceptionCode.VALIDATION_EXCEPTION,
                    new InvalidParam(
                            p.currentName(),
                    "Decimal values are not allowed for long fields"));
        }
        return (long) value;
    }
}
