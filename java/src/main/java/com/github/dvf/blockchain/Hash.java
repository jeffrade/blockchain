package com.github.dvf.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	private Hash() {
		super();
	}

    public static String hash(Block block) {
        return hash(block.toJson().toString());
    }

	public static String hash(String s) {
		return toHex(hash(s.getBytes(), 0, s.length()));
    }

    /**
     * See https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/core/Utils.java
     * for a full implementation. Uses Google's Guava lib which I excluded for simplicity.
     *
     * @return String hexadecimal
     */
    protected static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(format(b));
        }

        return sb.toString();
    }

    protected static String format(byte b) {
        return Integer.toHexString(b & 0xFF);
    }

	/**
	 * See https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/core/Sha256Hash.java
	 * for a full implementation.
     *
     * @return byte[] bytes
	 */
	private static byte[] hash(byte[] input, int offset, int length) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(input, offset, length);
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
