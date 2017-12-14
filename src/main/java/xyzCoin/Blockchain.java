package xyzCoin;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.*;

/**
 * Created by Tom on 11/10/2017.
 *
 * Aggregates Blocks Into A Chain
 */

class Blockchain {
  private int difficulty;
  private ArrayList<Block> blockchain;
  private ArrayList<Transaction> stagedTransactions;
  private String lastBlockHash;

  Blockchain(int blockDifficulty) {
    this.difficulty = blockDifficulty;
    this.blockchain = new ArrayList<>();
    this.stagedTransactions = new ArrayList<>();
    this.lastBlockHash = "0000000000";
  }

  ArrayList<Block> getBlocks(){
    return this.blockchain;
  }

  void mine() {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    CompletableFuture.supplyAsync(() -> this.createCallable(executor))
        .thenAccept(this::addNewBlock)
        .thenRun(this::mine);
  }

  void stageTransaction(Transaction transaction) throws InvalidRequestException {
    if (!verifyValidTransaction(transaction)) throw new InvalidRequestException("Invalid Request");
    stagedTransactions.add(transaction);
    System.out.println("Transaction Staged");
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
}