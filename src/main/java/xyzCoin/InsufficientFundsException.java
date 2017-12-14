package xyzCoin;

/**
 * Created by Tom on 27/10/2017.
 *
 * Provides Abstraction for 409 HTTP ServerException
 */

public class InsufficientFundsException extends Exception implements ServerException {
  private String message;
  private int statusCode;

  InsufficientFundsException(String errorMessage) {
    this.message = errorMessage;
    this.statusCode = 402;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }
}
