package com.jcosta.tinybank.domain.exceptions;

import java.util.List;

public class ContractException extends RuntimeException {
    private final List<InvalidParam> invalidParams;

    public ContractException(InvalidParam invalidParam) {
        this.invalidParams = List.of(invalidParam);
    }

    public ContractException(List<InvalidParam> invalidParams) {
        this.invalidParams = invalidParams;
    }

    public List<InvalidParam> getInvalidParams() {
        return invalidParams;
    }
}
