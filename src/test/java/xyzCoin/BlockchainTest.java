package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

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
    assertEquals(1, tester.getBlocks().size());
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