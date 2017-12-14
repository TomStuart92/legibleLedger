package xyzCoin;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.\
 *
 * Tests InsufficientFundsException
 */
public class InsufficientFundsServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn402() {
    InsufficientFundsException tester = new InsufficientFundsException("Test Message");
    assertEquals(402, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    InsufficientFundsException tester = new InsufficientFundsException(message);
    assertEquals(message, tester.getMessage());
  }
}