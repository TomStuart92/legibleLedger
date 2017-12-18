package xyzCoin;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tom on 12/10/2017.
 *
 * Provides Controller Interface for Server
 */

class Controller {
  private WalletController walletController;
  private Blockchain blockchain;

  Controller(int difficulty) {
    walletController = new WalletController();
    blockchain = new Blockchain(difficulty);
//    startSaveStateTask();
  }

  void mineBlockchain() {
    blockchain.mine();
  }

  void createNewWallet(String name, String password) throws AlreadyExistsServerException, InternalServerException {
    walletController.createNewWallet(name, password);
  }

  void sendCoin(String password, String fromName, Double amount, String toName) throws InternalServerException, ForbiddenServerException, InsufficientFundsException {
    Wallet fromWallet = walletController.getWallet(fromName);
    Wallet toWallet = walletController.getWallet(toName);

    Transaction transaction = fromWallet.createTransaction(password, this.blockchain, toWallet.getWalletAddress(), amount);
    try {
      blockchain.stageTransaction(transaction);
    } catch (InvalidRequestException e) {
      throw new InternalServerException("unable to send coin");
    }
  }

  double getWalletBalance(String name) {
    Wallet wallet = walletController.getWallet(name);
    return wallet.getWalletBalance(this.blockchain);
  }

  void shutdown() throws InternalServerException {
    blockchain.stopMining();
//    saveState();
  }

  void saveState() throws InternalServerException {
    try {
      FileOutputStream blockchainFile = new FileOutputStream(new File("blockchainState.txt"));
      ObjectOutputStream blockchainOutputStream = new ObjectOutputStream(blockchainFile);
      blockchainOutputStream.writeObject(this.blockchain);
      blockchainOutputStream.close();
      blockchainFile.close();

      FileOutputStream walletFile = new FileOutputStream(new File("walletControllerState.txt"));
      ObjectOutputStream walletOutputStream = new ObjectOutputStream(walletFile);
      walletOutputStream.writeObject(this.walletController);
      walletOutputStream.close();
      walletFile.close();

    } catch (IOException e) {
      System.out.print(e);
      throw new InternalServerException("unable to save state");
    }
  }

  void loadState(String blockChainStatePath, String walletControllerStatePath) throws InternalServerException {
    try {
      FileInputStream blockchainFile = new FileInputStream(new File(blockChainStatePath));
      ObjectInputStream blockchainInputStream = new ObjectInputStream(blockchainFile);
      this.blockchain = (Blockchain) blockchainInputStream.readObject();
      blockchainInputStream.close();
      blockchainFile.close();

      FileInputStream walletFile = new FileInputStream(new File(walletControllerStatePath));
      ObjectInputStream walletInputStream = new ObjectInputStream(walletFile);
      this.walletController = (WalletController) walletInputStream.readObject();
      walletInputStream.close();
      walletFile.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new InternalServerException("unable to load state");
    }
  }

  private void startSaveStateTask() {
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        try {
          saveState();
        } catch (InternalServerException e) {
          System.out.print("unable to save state");
        }
      }
    };
    // schedule to repeat every 10 minutes
    timer.schedule(task,0,600000);
  }
}
