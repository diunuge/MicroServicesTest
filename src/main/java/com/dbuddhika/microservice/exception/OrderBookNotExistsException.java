package com.dbuddhika.microservice.exception;

public class OrderBookNotExistsException extends RuntimeException {

  public OrderBookNotExistsException(Long id) {
    super("Order Book with id "+ id + " not exists");
  }
}
