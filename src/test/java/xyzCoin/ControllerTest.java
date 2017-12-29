package xyzCoin;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

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
    testSave.createNewWallet("send", "password");
    testSave.createNewWallet("receive", "password");
    testSave.mineBlockchain();
    testSave.sendCoin("password", "send", 1.0, "receive");
    testSave.shutdown();

    Controller testLoad = new Controller(0, "blockchainState.ser", "walletControllerState.ser");
    assertThat(testLoad.getWalletBalance("receive"), greaterThan(testLoad.getWalletBalance("send")));
    assertThat(testLoad.blockchainSize(), greaterThan(0));
  }
}
