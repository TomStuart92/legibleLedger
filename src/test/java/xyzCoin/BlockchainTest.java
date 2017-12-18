package xyzCoin;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by Tom on 14/12/2017.
 */

public class BlockchainTest {
  @Test
  public void getBlocksShouldReturnEmptyOnNewBlockchain() throws Exception {
    Blockchain tester = new Blockchain(1);
    assertEquals(0, tester.getBlocks().size());
  }

  @Test
  public void mineShouldMineBlock() throws Exception {
    WalletKeyGenerator keyGenerator = new WalletKeyGenerator(1048, "RSA");
    keyGenerator.createKeys();
    Transaction transaction = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    transaction.sign(keyGenerator.getPrivateKey());
    Blockchain tester = new Blockchain(0);
    tester.stageTransaction(transaction);
    tester.mine();
    TimeUnit.SECONDS.sleep(1);
    assertThat(tester.getBlocks().size(), greaterThan(1));
    tester.stopMining();
  }

  @Test
  public void stageTransactionShouldAcceptATransaction() throws Exception {
    WalletKeyGenerator keyGenerator = new WalletKeyGenerator(1048, "RSA");
    keyGenerator.createKeys();
    Transaction transaction = new Transaction(keyGenerator.getPublicKey(), 10.00, keyGenerator.getPublicKey());
    transaction.sign(keyGenerator.getPrivateKey());
    Blockchain tester = new Blockchain(0);
    tester.stageTransaction(transaction);
  }
}