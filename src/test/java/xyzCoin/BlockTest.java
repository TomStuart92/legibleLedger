package xyzCoin;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests Block
 */

public class BlockTest {
  @Test
  public void callShouldReturnBlock() throws Exception {
    ArrayList<Transaction> transactions = new ArrayList<>();
    Block tester = new Block(1, "000", transactions);
    assertEquals(tester, tester.call());
  }

  @Test
  public void getBlockHashShouldReturnNullBeforeBeingMined() throws Exception {
    ArrayList<Transaction> transactions = new ArrayList<>();
    Block tester = new Block(1, "000", transactions);
    assertEquals(null, tester.getBlockHash());
  }

  @Test
  public void getBlockHashShouldReturnHashAfterBeingMined() throws Exception {
    ArrayList<Transaction> transactions = new ArrayList<>();
    Block tester = new Block(1, "000", transactions);
    tester.call();
    assertThat(tester.getBlockHash(), instanceOf(String.class));
  }

  @Test
  public void getDataShouldReturnBlockData() throws Exception {
    ArrayList<Transaction> transactions = new ArrayList<>();
    Block tester = new Block(1, "000", transactions);
    assertEquals(transactions, tester.getData());
  }
}