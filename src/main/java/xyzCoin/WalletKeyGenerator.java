package xyzCoin;

import java.security.*;

/**
 * Created by Tom on 14/12/2017.
 *
 * Creates Private and Public Key based on the RSA algorithm
 */

class WalletKeyGenerator {

  private KeyPairGenerator keyGen;
  private PrivateKey privateKey;
  private PublicKey publicKey;

  WalletKeyGenerator(int keyLength, String algorithm) throws NoSuchAlgorithmException {
    this.keyGen = KeyPairGenerator.getInstance(algorithm);
    this.keyGen.initialize(keyLength);
  }

  KeyPair createKeys() {
    KeyPair pair = this.keyGen.generateKeyPair();
    this.privateKey = pair.getPrivate();
    this.publicKey = pair.getPublic();
    return pair;
  }

  PrivateKey getPrivateKey() {
    return this.privateKey;
  }

  PublicKey getPublicKey() {
    return this.publicKey;
  }
}