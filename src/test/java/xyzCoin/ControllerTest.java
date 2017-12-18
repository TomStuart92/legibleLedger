package xyzCoin;

import org.junit.Test;

/**
 * Created by Tom on 18/12/2017.
 */
public class ControllerTest {
  @Test
  public void saveStateShouldPersistState() throws Exception {
    Controller tester = new Controller(0, null, null);
    tester.createNewWallet("test", "password");
    tester.saveState();
    tester.shutdown();
  }

  @Test
  public void loadStateShouldLoadState() throws Exception {
    Controller testSave = new Controller(0, null, null);
    testSave.createNewWallet("test", "password");
    testSave.saveState();
    Controller testLoad = new Controller(0, null, null);
    testLoad.loadState("blockchainState1.txt", "walletControllerState1.txt");
    testSave.shutdown();
    testLoad.shutdown();
  }
}
