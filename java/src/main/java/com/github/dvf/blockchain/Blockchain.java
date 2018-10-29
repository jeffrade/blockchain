package com.github.dvf.blockchain;

import com.github.dvf.blockchain.Block;
import com.github.dvf.blockchain.Hash;
import com.github.dvf.blockchain.Transaction;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SpringBootApplication
@RestController
public class Blockchain {

    private static final String DIFFICULTY = "000";

	private LinkedList<Block> chain;

	private List<Transaction> currentTransactions;

	private Set<String> nodes;

    private OkHttpClient client = new OkHttpClient();

	public Blockchain() {
		this.chain = new LinkedList<Block>();//Collections.synchronizedList()
		this.currentTransactions = new ArrayList<Transaction>();
		this.nodes = new HashSet<String>();

		// Create the genesis block
		newBlock("1", 100);
	}

    /**
     * Add a new node to the list of nodes
     *
     * @param address Address of node.
     *                E.g. 'http://192.168.0.5:5000' or '192.168.0.5:5000'
     */
	public void registerNode(String address) {
        try {
            String parsedUrl = URI.create(address).toString();
            nodes.add(parsedUrl);
        } catch(NullPointerException e) {
            System.out.println("Invalid URL");
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid URL");
        }
    }

    /**
     * Determine if a given blockchain is valid
     *
     * @param chain A blockchain
     * @return true if valid, false if not
     */
    public boolean validChain(LinkedList<Block> chain) {
        Block lastBlock = chain.getFirst();
        int currentIndex = 1;

        while (currentIndex < chain.size()) {
            Block block = chain.get(currentIndex);
            System.out.println(lastBlock.toJson());
            System.out.println(block.toJson());
            System.out.println("-----------");
            //Check that the hash of the block is correct
            String lastBlockHash = Hash.hash(lastBlock);
            if (block.getPreviousHash() != lastBlockHash)
                return false;

            //Check that the Proof of Work is correct
            if (!validProof(lastBlock.getProof(), block.getProof(), lastBlockHash))
                return false;

            lastBlock = block;
            currentIndex++;
        }

        return true;
    }

    /**
     * This is our consensus algorithm, it resolves conflicts
     * by replacing our chain with the longest one in the network.
     *
     * @return true if our chain was replaced, false if not.
     */
    public boolean resolveConflicts() throws IOException {
        Set<String> neighbours = this.nodes;
        LinkedList<Block> newChain = null;

        //We 're only looking for chains longer than ours
        int maxLength = chain.size();

        //Grab and verify the chains from all the nodes in our network
        for (String node : neighbours) {
            String protocol = node.startsWith("http") ? "" : "http://";
            String url = protocol + node + "/indices/global/ticker/BTCUSD";/*FIXME"/chain";*/
            Request request = new Request.Builder().url(url).build();
            Response resp = client.newCall(request).execute();

            if (resp.code() == 200) {
                JSONObject response = new JSONObject(resp.body().string());
                System.out.println("################## response=" + response);
                double length = Double.valueOf(response.get("last"/*FIXME"length"*/).toString());
                //TODOLinkedList<Block> chain = (LinkedList<Block>) response.get("chain");

                //Check if the length is longer and the chain is valid
                if (false/*TODO length > maxLength && self.validChain(chain)*/){
                    //TODO maxLength = length;
                    //TODO newChain = chain;
                }
            }
        }

        //Replace our chain if we discovered a new, valid chain longer than ours
        if (newChain != null) {
            this.chain = newChain;
            return true;
        }

        return false;
    }

    public Block newBlock(String previousHash, long proof) {
        String hash = previousHash == null ? Hash.hash(lastBlock()) : previousHash;
        Block block = new Block(
                (long) this.chain.size() + 1,
                (new Date()).getTime(),
                this.currentTransactions,
                proof,
                hash
        );
        this.currentTransactions = new ArrayList();
        this.chain.add(block);
        return block;
    }
    
	public long newTransaction(String sender, String recipient, double amount) {
		this.currentTransactions.add(new Transaction(sender, recipient, amount));
		return lastBlock().getIndex() + 1; //This is the next Block index to be mined
	}

	public Block lastBlock() {
		return chain.getLast();
    }

	public long proofOfWork(Block lastBlock) {
		long proof = 0l;
		long lastProof = lastBlock.getProof();
		String lastBlockHash = Hash.hash(lastBlock);
		while(!validProof(lastProof, proof, lastBlockHash)) {
			proof++;
		}

		return proof;
	}

	public static boolean validProof(long lastProof, long proof, String lastBlockHash) {
        String guess = String.valueOf(lastProof) + String.valueOf(proof) + lastBlockHash;
        String guessHash = Hash.hash(guess);
        return guessHash.startsWith(DIFFICULTY);
	}

	@RequestMapping("/status")
    public String status(@RequestParam(value="name", defaultValue="satoshi") String name) {
        JSONObject resp = new JSONObject("{ \"messsage\" : \"Hi "+ name + "!\", \"status\" : \"up\" }");
        return resp.toString();
    }

    @RequestMapping("/test/block")
    public String testBlock() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction("38NESedxnvmM7axFxyUGhPdDiL9aKMo5EE", "15GNygoo41Qv3b1HW1Ywv1aHvfgHQsrnan", 1.5553));
        txs.add(new Transaction("3HwUkTZEp26srzHepNA1xpETxFPwJ4g7y4", "3NRUnGkKS9ZZkZxg89Q9QmBqLS3mtc3NtX ", 0.0010));
        txs.add(new Transaction("3QmxY62naVfjsiDzUozXWNraSwgYa231sD", "1B3wcWczi3a76qZsk6KUwaXbBeDHuECDmo", 0.0005));
        Block mockBlock = new Block(551357l, System.currentTimeMillis(), txs, 2461276344l, "000000000000000000071bc9556b6eaa33dc81c58968c629bc76dcb763c84701");
        JSONObject resp = mockBlock.toJson();
        return resp.toString();
    }

    @RequestMapping("/test/httpClient")
    public String testHttpClient() throws IOException {
        Blockchain b = new Blockchain();
        b.registerNode("https://apiv2.bitcoinaverage.com");
        b.resolveConflicts();
        return "{ \"message\" : \"done\" }";
    }

    public static void main(String[] args) {
        SpringApplication.run(Blockchain.class, args);
    }

}
