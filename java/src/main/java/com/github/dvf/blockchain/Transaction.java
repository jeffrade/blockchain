package com.github.dvf.blockchain;

import org.json.JSONObject;

public class Transaction {

	private String sender;
	private String recipient;
	private double amount;
	
	public Transaction(String sender, String recipient, double amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
	}

	public String getSender() {
		return this.sender;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public double getAmount() {
		return this.amount;
	}

    public JSONObject toJson() {
        return new JSONObject(this);
    }
}
