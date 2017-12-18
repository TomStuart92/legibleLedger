package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests AlreadyExistsServerException
 */

public class NotFoundExceptionTest {
  @Test
  public void getStatusCodeShouldReturn409() {
    NotFoundException tester = new NotFoundException("Test Message");
    assertEquals(404, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    NotFoundException tester = new NotFoundException(message);
    assertEquals(message, tester.getMessage());
  }
}