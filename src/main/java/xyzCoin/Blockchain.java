package xyzCoin;

import org.omg.CORBA.TRANSACTION_MODE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.*;

/**
 * Created by Tom on 11/10/2017.
 *
 * Aggregates Blocks Into A Chain
 */

class Blockchain implements Serializable {
  private static final long serialVersionUID = 1;

  private int difficulty;
  private ArrayList<Block> blockchain;
  private ArrayList<Transaction> stagedTransactions;
  private String lastBlockHash;
  private transient ExecutorService executor;

  Blockchain(int blockDifficulty) {
    this.difficulty = blockDifficulty;
    this.blockchain = new ArrayList<>();
    this.stagedTransactions = new ArrayList<>();
    this.lastBlockHash = "GENESIS BLOCK";
    initializeThreadPool();
  }

  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeUTF(Integer.toString(this.difficulty));
    out.writeObject(this.stagedTransactions);
    out.writeObject(this.blockchain);
    out.writeUTF(this.lastBlockHash);
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.difficulty = Integer.parseInt(in.readUTF()) ;
    this.blockchain = (ArrayList<Block>)  in.readObject();
    this.stagedTransactions = (ArrayList<Transaction>)  in.readObject();
    this.lastBlockHash = in.readUTF();
    initializeThreadPool();
  }

  ArrayList<Block> getBlocks(){
    return this.blockchain;
  }

  int size() { return this.blockchain.size(); }

  void mine() {
    if (this.executor == null) {
      initializeThreadPool();
    }
    CompletableFuture.supplyAsync(() -> this.createCallable(this.executor))
        .thenAccept(this::addNewBlock)
        .thenRun(this::mine);
  }

  void stopMining() {
    if (this.executor != null) {
      this.executor.shutdownNow();
    }
  }

  void stageTransaction(Transaction transaction) throws InvalidRequestException {
    if (!verifyValidTransaction(transaction)) throw new InvalidRequestException("Invalid Request");
    stagedTransactions.add(transaction);
  }

  private Future<Block> createCallable(ExecutorService executor) {
    ArrayList<Transaction> stagedTransactions = this.stagedTransactions;
    this.stagedTransactions = new ArrayList<>();
    Block newBlock = new Block(this.difficulty, this.lastBlockHash, stagedTransactions);
    return executor.submit(newBlock);
  }

  private void addNewBlock(Future<Block> minedBlock) {
    try {
      this.blockchain.add(minedBlock.get());
      this.lastBlockHash = minedBlock.get().getBlockHash();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  private boolean verifyValidTransaction(Transaction transaction) {
    String signature = transaction.getTransactionSignature();
    PublicKey senderKey = transaction.getFrom();
    String transactionData = "senderAddress:" + transaction.getFrom() + ", amount:" + transaction.getAmount() + ", to:" + transaction.getTo();

    try {
      Signature publicSignature = Signature.getInstance("SHA256withRSA");
      publicSignature.initVerify(senderKey);
      publicSignature.update(transactionData.getBytes());
      byte[] signatureBytes = Base64.getDecoder().decode(signature);
      return publicSignature.verify(signatureBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
      return false;
    }
  }

  private void initializeThreadPool() {
    this.executor = Executors.newFixedThreadPool(10);
  }
}