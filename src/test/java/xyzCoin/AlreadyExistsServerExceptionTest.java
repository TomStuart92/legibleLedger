package xyzCoin;

import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests AlreadyExistsServerException
 */

public class AlreadyExistsServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn409() {
    AlreadyExistsServerException tester = new AlreadyExistsServerException("Test Message");
    assertEquals(409, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    AlreadyExistsServerException tester = new AlreadyExistsServerException(message);
    assertEquals(message, tester.getMessage());
  }
}