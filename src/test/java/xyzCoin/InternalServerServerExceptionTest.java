package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests InternalServerException
 */

public class InternalServerServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn409() {
    InternalServerException tester = new InternalServerException("Test Message");
    assertEquals(500, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    InternalServerException tester = new InternalServerException(message);
    assertEquals(message, tester.getMessage());
  }

}