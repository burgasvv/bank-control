package org.burgas.bankservice.exception;

public class NullMoneyAmountOperationException extends RuntimeException {

  public NullMoneyAmountOperationException(String message) {
    super(message);
  }
}
