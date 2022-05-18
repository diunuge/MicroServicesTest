package com.dbuddhika.microservice.service;

import com.dbuddhika.microservice.model.Execution;
import com.dbuddhika.microservice.model.Order;
import com.dbuddhika.microservice.model.OrderBook;
import com.dbuddhika.microservice.model.OrderBookStatus;
import com.dbuddhika.microservice.model.OrderType;
import com.dbuddhika.microservice.repository.OrderBookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBookServiceImpl implements OrderBookService{

  private final OrderBookRepository orderBookRepository;

  @Autowired
  public OrderBookServiceImpl(OrderBookRepository orderBookRepository) {
    this.orderBookRepository = orderBookRepository;
  }

  @Override
  public List<OrderBook> getOrderBooks() {

    return this.orderBookRepository.findAll();
  }

  @Override
  public OrderBook open(OrderBook orderBook) {

    if(orderBook.getOrderBookStatus() != OrderBookStatus.OPEN)
      orderBook.setOrderBookStatus(OrderBookStatus.OPEN);

    OrderBook orderBookCreated = orderBookRepository.save(orderBook);
    return orderBookCreated;
  }

  @Override
  public OrderBook close(long orderBookId) {

    OrderBook orderBook = this.orderBookRepository
        .findById(orderBookId)
        .orElseThrow(
            () -> new IllegalArgumentException("Order Book with id "+ orderBookId + " not exists")
        );

    if(orderBook.getOrderBookStatus() == OrderBookStatus.CLOSE){
      throw new IllegalStateException("Order Book is already closed!");
    }

    orderBook.setOrderBookStatus(OrderBookStatus.CLOSE);
    return orderBookRepository.save(orderBook);
  }

  @Override
  public List<Order> getOrders(long orderBookId) {

    OrderBook orderBook = orderBookRepository
        .findById(orderBookId)
        .orElseThrow(
            () -> new IllegalArgumentException("Order Book with id "+ orderBookId + " not exists")
        );

    return orderBook.getOrders();
  }


  @Override
  public OrderBook addOrder(long orderBookId, Order order) {

    OrderBook orderBook = orderBookRepository
        .findById(orderBookId)
        .orElseThrow(
            () -> new IllegalArgumentException("Order Book with id "+ orderBookId + " not exists")
        );

    if(orderBook.getOrderBookStatus() == OrderBookStatus.CLOSE){
      throw new IllegalStateException("Order Book is already closed!");
    }

    if(order.getOrderType().equals(OrderType.MARKET_ORDER)){
      order.setPrice(null);
    }
    else if (order.getOrderType().equals(OrderType.LIMIT_ORDER)) {

      if(order.getPrice()==null && order.getPrice()==0d){
        throw new IllegalArgumentException("Order price has to be defined!");
      }
    }
    else{
      throw new IllegalArgumentException("Order type should be \"MARKET_ORDER\" or \"LIMIT_ORDER\"!");
    }

    orderBook.getOrders().add(order);

    return orderBookRepository.save(orderBook);
  }

  @Override
  public List<Execution> getExecutions(long orderBookId) {

    OrderBook orderBook = orderBookRepository
        .findById(orderBookId)
        .orElseThrow(
            () -> new IllegalArgumentException("Order Book with id "+ orderBookId + " not exists")
        );

    return orderBook.getExecutions();
  }

  @Override
  public OrderBook addExecution(long orderBookId, Execution execution) {

    OrderBook orderBook = orderBookRepository
        .findById(orderBookId)
        .orElseThrow(
            () -> new IllegalArgumentException("Order Book with id "+ orderBookId + " not exists")
        );

    orderBook.getExecutions().add(execution);

    return orderBookRepository.save(orderBook);
  }
}
