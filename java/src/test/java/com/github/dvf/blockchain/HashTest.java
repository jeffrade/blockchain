package com.github.dvf.blockchain;

import com.github.dvf.blockchain.Hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HashTest {

    @Test
    public void testHash() throws Exception {
        String actualHash = Hash.hash("g");
        String expectedHash = "cdaa9856147b6c5b4ff2b7dfee5da20aa38253099ef1b4a64aced233c9afe29";
        assertEquals("Hashes aren't equal!", expectedHash, actualHash);

        actualHash = Hash.hash("absdefghijklmnopqrstuvwxyz");
        expectedHash = "a25d491aff23fe73c9f2bd1a153982b04a52969a976d0eab6d7776c142a90";
        assertEquals("Hashes aren't equal!", expectedHash, actualHash);
    }

    @Test
    public void testBitOperators() throws Exception {
        byte b1 = 32;
        byte b2 = 64;
        assertEquals("Something wrong!", b1, b2 >> 1);
        assertEquals("Something wrong!", b2, b1 << 1);
        assertEquals("Something wrong!", b1, 32);
        assertEquals("Something wrong!", b2, 64);
    }
}