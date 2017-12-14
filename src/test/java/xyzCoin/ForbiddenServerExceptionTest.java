package xyzCoin;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 */
public class ForbiddenServerExceptionTest {
  @Test
  public void getStatusCodeShouldReturn403() {
    ForbiddenServerException tester = new ForbiddenServerException("Test Message");
    assertEquals(403, tester.getStatusCode());
  }

  @Test
  public void getMessageShouldReturnMessage() {
    String message = "Test Message";
    ForbiddenServerException tester = new ForbiddenServerException(message);
    assertEquals(message, tester.getMessage());
  }
}