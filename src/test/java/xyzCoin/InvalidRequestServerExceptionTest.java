package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests InvalidRequestException
 */

public class InvalidRequestServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn400() {
    InvalidRequestException tester = new InvalidRequestException("Test Message");
    assertEquals(400, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    InvalidRequestException tester = new InvalidRequestException(message);
    assertEquals(message, tester.getMessage());
  }
}