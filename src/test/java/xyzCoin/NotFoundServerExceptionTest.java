package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests AlreadyExistsServerException
 */

public class NotFoundServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn409() {
    NotFoundServerException tester = new NotFoundServerException("Test Message");
    assertEquals(404, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    NotFoundServerException tester = new NotFoundServerException(message);
    assertEquals(message, tester.getMessage());
  }
}