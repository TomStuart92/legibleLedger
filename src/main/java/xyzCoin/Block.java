package xyzCoin;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Callable;

/**
 * Created by Tom on 23/08/2017.
 *
 * Embodies a transaction list in a hash block
 */

public class Block implements Callable<Block>, Serializable {
  private ArrayList<Transaction> data;
  private String previousBlockHash;
  private String thisBlockHash;
  private int difficulty;

  Block(int blockDifficulty, String lastBlockHash, ArrayList<Transaction> newData) {
    data = newData;
    previousBlockHash = lastBlockHash;
    difficulty = blockDifficulty;
  }

  public Block call() {
    this.mineBlock();
    return this;
  }

  String getBlockHash() {
    return this.thisBlockHash;
  }

  ArrayList<Transaction> getData() {
    return this.data;
  }

  @Override
  public String toString() {
    return "Difficulty:" + difficulty + "\npreviousBlockHash: " + previousBlockHash + "\nthisBlockHash: " + thisBlockHash + "\ndata: " + data;
  }

  private void mineBlock() {
    String baseValue = getBaseValue();
    String encoded = "";

    Boolean minedFlag = false;
    int nonce = 0;

    while(!minedFlag) {
      nonce++;
      String blockValue = baseValue + nonce;
      encoded = encodeSHA256(blockValue);
      if (isValidHash(encoded)) minedFlag = true;
    }
    thisBlockHash = encoded;
  }

  private String getBaseValue() {
    return "previousBlockHash:" + previousBlockHash + "data:" + data + "nonce:";
  }

  private Boolean isValidHash(String hash) {
    String prefix = StringUtils.repeat("0", this.difficulty);
    return StringUtils.startsWith(hash, prefix);
  }

  private String encodeSHA256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch(NoSuchAlgorithmException x) {
      System.out.println("ERR: NO SUCH ALGORITHM - SHA256");
      return "ERR: NO SUCH ALGORITHM - SHA256";
    }
  }
}

