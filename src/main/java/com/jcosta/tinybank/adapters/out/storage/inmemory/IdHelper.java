package com.jcosta.tinybank.adapters.out.storage.inmemory;

import java.util.UUID;
import java.util.regex.Pattern;

public class IdHelper {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    public static UUID generate() {
        return UUID.randomUUID();
    }

    public static UUID generateFromString(String input) {
        if (input == null || !UUID_PATTERN.matcher(input).matches()) {
            return null;
        }

        return UUID.fromString(input);
    }
}
