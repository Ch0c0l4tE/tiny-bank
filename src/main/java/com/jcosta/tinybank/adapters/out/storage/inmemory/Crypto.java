package com.jcosta.tinybank.adapters.out.storage.inmemory;

import java.util.Arrays;
import java.util.Base64;

public class Crypto {
    public static String encrypt(String cursor) {
        return Base64.getEncoder().encodeToString(cursor.getBytes());
    }

    public static String decrypt(String cursor) {
        return Arrays.toString(Base64.getDecoder().decode(cursor));
    }
}
