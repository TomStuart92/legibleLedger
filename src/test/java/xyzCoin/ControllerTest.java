package xyzCoin;

import org.junit.Test;

/**
 * Created by Tom on 18/12/2017.
 */
public class ControllerTest {

  @Test
  public void saveStateShouldPersistState() throws Exception {
    Controller tester = new Controller(0);
    tester.createNewWallet("test", "password");
    tester.saveState();
  }

  @Test
  public void loadStateShouldLoadState() throws Exception {
    Controller testSave = new Controller(0);
    testSave.createNewWallet("test", "password");
    testSave.saveState();

    Controller testLoad = new Controller(0);
    testLoad.loadState("blockchainState.txt", "walletControllerState.txt");
  }
}
