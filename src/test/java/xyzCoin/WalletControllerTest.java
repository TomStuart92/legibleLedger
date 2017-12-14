package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests Wallet Controller
 */
public class WalletControllerTest {
  @Test
  public void createNewWalletShouldReturnANewWallet() throws Exception {
    WalletController tester = new WalletController();
    assertThat(tester.createNewWallet("Tom", "Password"), instanceOf(Wallet.class));
  }

  @Test
  public void getWalletShouldReturnNamedWallet() throws Exception {
    WalletController tester = new WalletController();
    String name = "Tom";
    Wallet wallet = tester.createNewWallet(name, "Password");
    assertEquals(tester.getWallet(name), wallet);
  }

}