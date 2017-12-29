package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests InvalidRequestServerException
 */

public class InvalidRequestServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn400() {
    InvalidRequestServerException tester = new InvalidRequestServerException("Test Message");
    assertEquals(400, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    InvalidRequestServerException tester = new InvalidRequestServerException(message);
    assertEquals(message, tester.getMessage());
  }
}