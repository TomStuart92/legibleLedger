package xyzCoin;

import org.junit.Test;

import java.security.PublicKey;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests Wallet
 */
public class WalletTest {

  @Test
  public void getWalletAddressShouldReturnWalletPublicKey() throws Exception {
    Wallet tester = new Wallet("Password");
    assertThat(tester.getWalletAddress(), instanceOf(PublicKey.class));
  }

  @Test
  public void createTransactionShouldCreateATransaction() throws Exception {
    String password = "Password";
    Blockchain blockchain = mock(Blockchain.class);
    PublicKey publicKey = mock(PublicKey.class);
    Wallet tester = new Wallet(password);
    assertThat(tester.createTransaction(password, blockchain, publicKey, 0.00), instanceOf(Transaction.class));
  }

  @Test(expected = ForbiddenServerException.class)
  public void createTransactionShouldThrowIfWrongPassword() throws Exception {
    String password = "Password";
    Blockchain blockchain = mock(Blockchain.class);
    PublicKey publicKey = mock(PublicKey.class);
    Wallet tester = new Wallet(password);
    tester.createTransaction("Incorrect Password", blockchain, publicKey, 0.00);
  }

  @Test(expected = InsufficientFundsException.class)
  public void createTransactionShouldThrowIfNoFunds() throws Exception {
    String password = "Password";
    Blockchain blockchain = mock(Blockchain.class);
    PublicKey publicKey = mock(PublicKey.class);
    Wallet tester = new Wallet(password);
    tester.createTransaction(password, blockchain, publicKey, 100.00);
  }

  @Test
  public void getWalletBalanceShouldReturnWalletBalance() throws Exception {
    String password = "Password";
    Blockchain blockchain = mock(Blockchain.class);
    Wallet tester = new Wallet("Password");
    assertEquals(0.00, tester.getWalletBalance(blockchain));
  }

}