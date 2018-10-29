package com.github.dvf.blockchain;

import java.util.List;

import org.json.JSONObject;

public class Block {

	private long index;
	private long timestamp;
	private List<Transaction> transactions;
	private long proof;
	private String previousHash;

	public Block(long index, long timestamp, List<Transaction> transactions, long proof, String previousHash) {
		super();
		this.index = index;
		this.timestamp = timestamp;
		this.transactions = transactions;
		this.proof = proof;
		this.previousHash = previousHash;
	}

	public long getIndex() {
		return this.index;
	}

	public long getProof() {
		return this.proof;
	}

    public long getTimestamp() {
        return timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public JSONObject toJson() {
        return new JSONObject(this);
    }
}
