package com.urlshortener.service.hashgenerator;

import java.math.BigInteger;

public class Base62 {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    public static String encode(byte[] bytes) {
        BigInteger num = new BigInteger(1, bytes);
        StringBuilder sb = new StringBuilder();

        if (num.equals(BigInteger.ZERO)) return "0";

        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divMod = num.divideAndRemainder(BigInteger.valueOf(BASE));
            sb.append(ALPHABET.charAt(divMod[1].intValue()));
            num = divMod[0];
        }

        return sb.reverse().toString();
    }
}
