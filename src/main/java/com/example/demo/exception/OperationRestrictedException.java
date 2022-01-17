package com.example.demo.exception;

public class OperationRestrictedException extends RuntimeException {

  public OperationRestrictedException(String message) {
    super(message);
  }
}
