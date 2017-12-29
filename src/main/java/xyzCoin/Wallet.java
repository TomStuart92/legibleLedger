package xyzCoin;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

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

  Transaction createTransaction(String password, Blockchain blockchain, PublicKey walletAddress, double amount) throws ForbiddenServerException, InsufficientFundsServerException, InternalServerException {
    if (!checkPassword(password)) throw new ForbiddenServerException("Invalid Password");
    if (this.getWalletBalance(blockchain) < amount) throw new InsufficientFundsServerException("Insufficient Funds");
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
    CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>(blockchain.getBlocks());
    Iterator<Block> blockIterator = blocks.iterator();
    Double currentBalance = 0.0;
    PublicKey walletAddress = this.getWalletAddress();

    while (blockIterator.hasNext()) {
      currentBalance += 1;
      Block currentBlock = blockIterator.next();
      ArrayList<Transaction> transactions = currentBlock.getData();
      for (Transaction transaction : transactions) {
        if (transaction.getFrom().equals(walletAddress)) {
          currentBalance -= transaction.getAmount();
        }
        if (transaction.getTo().equals(walletAddress)) {
          currentBalance += transaction.getAmount();
        }
      }
    }
    return currentBalance;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeUTF(this.hashedPassword);

    int publicKeyLength = this.publicKey.getEncoded().length;
    out.writeInt(publicKeyLength);
    out.write(this.publicKey.getEncoded());

    int privateKeyLength = this.privateKey.getEncoded().length;
    out.writeInt(privateKeyLength);
    out.write(this.privateKey.getEncoded());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
    this.hashedPassword = in.readUTF();

    KeyFactory kf = KeyFactory.getInstance("RSA");
    int publicKeyLength = in.readInt();
    byte[] publicKeyBytes = new byte[publicKeyLength];
    in.read(publicKeyBytes, 0, publicKeyLength);
    this.publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

    int privateKeyLength = in.readInt();
    byte[] privateKeyBytes = new byte[privateKeyLength];
    in.read(privateKeyBytes, 0, privateKeyLength);
    this.privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
  }

  private Boolean checkPassword(String candidate) {
    return BCrypt.checkpw(candidate, this.hashedPassword);
  }
}
