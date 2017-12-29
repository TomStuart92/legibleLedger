package xyzCoin;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.\
 *
 * Tests InsufficientFundsServerException
 */
public class InsufficientFundsServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn402() {
    InsufficientFundsServerException tester = new InsufficientFundsServerException("Test Message");
    assertEquals(402, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    InsufficientFundsServerException tester = new InsufficientFundsServerException(message);
    assertEquals(message, tester.getMessage());
  }
}