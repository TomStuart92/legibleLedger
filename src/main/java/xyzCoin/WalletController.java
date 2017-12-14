package xyzCoin;

import java.util.HashMap;

/**
 * Created by Tom on 12/10/2017.
 *
 * Manages User Wallets
 */

class WalletController {
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

  Wallet getWallet(String name) {
    return wallets.get(name);
  }
}
