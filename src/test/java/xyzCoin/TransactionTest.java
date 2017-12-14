package xyzCoin;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests Transaction
 */
public class TransactionTest {
  private WalletKeyGenerator keyGenerator;

  @Before
  public void initialize() throws Exception {
    keyGenerator = new WalletKeyGenerator(1048, "RSA");
    keyGenerator.createKeys();
  }
  @Test
  public void signShouldReturnSignedSignature() throws Exception {
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    assertThat(tester.sign(keyGenerator.getPrivateKey()), instanceOf(String.class));
  }

  @Test
  public void getFromShouldReturnPublicKeyOfFrom() throws Exception {
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    assertEquals(keyGenerator.getPublicKey(), tester.getFrom());
  }

  @Test
  public void getAmountShouldReturnAmount() throws Exception {
    Double amount = 10.00;
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), amount, keyGenerator.getPublicKey());
    assertEquals(amount, tester.getAmount());
  }

  @Test
  public void getToShouldReturnPublicKeyOfTo() throws Exception {
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    assertEquals(keyGenerator.getPublicKey(), tester.getTo());
  }

  @Test
  public void getTransactionSignatureShouldReturnNullIfNotSigned() throws Exception {
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    assertEquals(null, tester.getTransactionSignature());
  }

  @Test
  public void getTransactionSignatureShouldReturnSigIfSigned() throws Exception {
    Transaction tester = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    tester.sign(keyGenerator.getPrivateKey());
    assertThat(tester.getTransactionSignature(), instanceOf(String.class));
  }
}