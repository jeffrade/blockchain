package com.github.dvf.blockchain;

import com.github.dvf.blockchain.Block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

public class BlockchainTest {

    @Test
    public void testBlockToJson() throws Exception {
        Block mockBlock = new Block(
                1l,
                1231006505l,
                new ArrayList(),
                1234l,
                "previous-hash-1234567890"
        );
        String mockBlockJson = mockBlock.toJson().toString();
        String expectedJson = "{\"previousHash\":\"previous-hash-1234567890\",\"index\":1,\"proof\":1234,\"transactions\":[],\"timestamp\":1231006505}";
        assertEquals("Block json String does not equal expected.", expectedJson, mockBlockJson);
    }

    @Test
    public void testTransactionToJson() throws Exception {
        Transaction mockTransaction = new Transaction(
                "12cbQLTFMXRnSzktFkuoG3eHoMeFtpTu3S",
                "1Q2TWHE3GMdB6BZKafqwxXtWAWgFt5Jvm3",
                10.0d
        );

        String mockTransactionJson = mockTransaction.toJson().toString();
        String expectedJson = "{\"amount\":10,\"sender\":\"12cbQLTFMXRnSzktFkuoG3eHoMeFtpTu3S\",\"recipient\":\"1Q2TWHE3GMdB6BZKafqwxXtWAWgFt5Jvm3\"}";
        assertEquals("Transaction json String does not equal expected.", expectedJson, mockTransactionJson);
    }

}