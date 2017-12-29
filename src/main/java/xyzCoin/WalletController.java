package xyzCoin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

/**
 * Created by Tom on 12/10/2017.
 *
 * Manages User Wallets
 */

class WalletController implements Serializable {
  private HashMap<String, Wallet> wallets;

  WalletController() {
    wallets = new HashMap<>();
  }

  Wallet createNewWallet(String name, String password) throws AlreadyExistsServerException, InternalServerException {
    if (wallets.get(name) != null) throw new AlreadyExistsServerException("Already Exists");
    Wallet newWallet = new Wallet(password);
    wallets.put(name, newWallet);
    return newWallet;
  }

  Wallet getWallet(String name) throws NotFoundException {
    Wallet wallet = wallets.get(name);
    if(wallet == null) {
      throw new NotFoundException("Wallet Does Not Exist");
    }
    return wallet;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeObject(this.wallets);
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.wallets = (HashMap) in.readObject();
  }
}
