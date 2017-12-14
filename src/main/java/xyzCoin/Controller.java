package xyzCoin;

import java.util.ArrayList;

/**
 * Created by Tom on 12/10/2017.
 *
 * Provides Controller Interface for Server
 */

public class Controller {
  private WalletController walletController;
  private Blockchain blockchain;

  Controller(int difficulty) {
    walletController = new WalletController();
    blockchain = new Blockchain(difficulty);
  }

  public void mineBlockchain() {
    blockchain.mine();
  }

  public ArrayList<Block> getBlocks() {
    return blockchain.getBlocks();
  }

  public Blockchain getBlockchain() {
    return this.blockchain;
  }

  public void createNewWallet(String name, String password) throws AlreadyExistsServerException, InternalServerException {
    walletController.createNewWallet(name, password);
  }

  public void sendCoin(String password, String fromName, Double amount, String toName) throws InternalServerException, ForbiddenServerException, InsufficientFundsException {
    Wallet fromWallet = walletController.getWallet(fromName);
    Wallet toWallet = walletController.getWallet(toName);

    Transaction transaction = fromWallet.createTransaction(password, this.blockchain, toWallet.getWalletAddress(), amount);
    try {
      blockchain.stageTransaction(transaction);
    } catch (InvalidRequestException e) {
      throw new InternalServerException("unable to send coin");
    }
  }

  public double getWalletBalance(String name) {
    Wallet wallet = walletController.getWallet(name);
    return wallet.getWalletBalance(this.blockchain);
  }
}
