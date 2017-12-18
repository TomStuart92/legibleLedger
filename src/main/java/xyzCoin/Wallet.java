package xyzCoin;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tom on 11/10/2017.
 *
 * Tracks transaction instances
 */

public class Wallet implements Serializable {
  private String hashedPassword;
  public PublicKey publicKey;
  private PrivateKey privateKey;

  Wallet(String password) throws InternalServerException {
    this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    try {
      WalletKeyGenerator keyGenerator = new WalletKeyGenerator(1048, "RSA");
      keyGenerator.createKeys();
      publicKey = keyGenerator.getPublicKey();
      privateKey = keyGenerator.getPrivateKey();
    } catch (NoSuchAlgorithmException e) {
      throw new InternalServerException("Error Calculating Wallet Private Keys");
    }
  }

  PublicKey getWalletAddress() {
    return this.publicKey;
  }

  Transaction createTransaction(String password, Blockchain blockchain, PublicKey walletAddress, double amount) throws ForbiddenServerException, InsufficientFundsException, InternalServerException {
    if (!checkPassword(password)) throw new ForbiddenServerException("Invalid Password");
    if (this.getWalletBalance(blockchain) < amount) throw new InsufficientFundsException("Insufficient Funds");
    Transaction transaction = new Transaction(this.getWalletAddress(), amount, walletAddress);
    PrivateKey walletPrivateKey = this.privateKey;
    try {
      transaction.sign(walletPrivateKey);
    } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new InternalServerException("Unable To Sign Transaction");
    }
    return transaction;
  }

  double getWalletBalance(Blockchain blockchain) {
    ArrayList<Block> blocks = blockchain.getBlocks();
    Iterator<Block> blockIterator = blocks.iterator();
    Double currentBalance = 0.0;
    PublicKey walletAddress = this.getWalletAddress();

    while (blockIterator.hasNext()) {
      currentBalance += 1;
      Block currentBlock = blockIterator.next();
      ArrayList<Transaction> transactions = currentBlock.getData();

      for (Transaction transaction : transactions) {
        if (transaction.getFrom() == walletAddress) {
          currentBalance -= transaction.getAmount();
        }
        if (transaction.getTo() == walletAddress) {
          currentBalance += transaction.getAmount();
        }
      }
    }
    return currentBalance;
  }

  @Override
  public String toString() {
    return "hashedPassword:" + hashedPassword + "\npublicKey: " + publicKey + "\nprivateKey: " + privateKey;
  }

  private Boolean checkPassword(String candidate) {
    return BCrypt.checkpw(candidate, this.hashedPassword);
  }
}
