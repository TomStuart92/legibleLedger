package xyzCoin;

import java.io.Serializable;
import java.security.*;
import java.util.Base64;

/**
 * Created by Tom on 11/10/2017.
 *
 * Encapsulates movement of coins from one wallet to another
 */

class Transaction implements Serializable {
  private PublicKey from;
  private Double amount;
  private PublicKey to;
  private String transactionString;
  private String transactionSignature;

  Transaction(PublicKey from, Double amount, PublicKey to) {
    this.from = from;
    this.amount = amount;
    this.to = to;
    transactionString = createTransactionString(from, amount, to);
  }

  String sign(PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
    Signature privateSignature;
    privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(privateKey);
    privateSignature.update(transactionString.getBytes());
    byte[] signature = privateSignature.sign();
    this.transactionSignature = Base64.getEncoder().encodeToString(signature);
    return this.transactionSignature;
  }

  PublicKey getFrom() {
    return from;
  }

  Double getAmount() {
    return amount;
  }

  PublicKey getTo() {
    return to;
  }

  String getTransactionSignature() {
    return transactionSignature;
  }

  @Override
  public String toString() {
    return "from:" + from + "\namount: " + amount + "\nto: " + to + "\ntransactionString: " + transactionString + "\ntransactionSignature: " + transactionSignature;
  }

  private String createTransactionString(PublicKey from, Double amount, PublicKey to) {
    return "senderAddress:" + from + ", amount:" + amount + ", to:" + to;
  }
}
