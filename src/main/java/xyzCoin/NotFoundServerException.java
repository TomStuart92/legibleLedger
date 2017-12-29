package xyzCoin;

/**
 * Created by Tom on 27/10/2017.
 *
 * Provides Abstraction for 403 HTTP ServerException
 */

public class NotFoundServerException extends Exception implements ServerException {
  private String message;
  private int statusCode;

  NotFoundServerException(String errorMessage) {
    this.message = errorMessage;
    this.statusCode = 404;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }
}
