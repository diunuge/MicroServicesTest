package com.dbuddhika.microservice.service;

import com.dbuddhika.microservice.model.Execution;
import com.dbuddhika.microservice.model.Order;
import com.dbuddhika.microservice.model.OrderBook;
import java.util.List;

public interface OrderBookService {


  List<OrderBook> getOrderBooks();

  OrderBook open(OrderBook orderBook);

  OrderBook close(long orderBookId);

  List<Order> getOrders(long orderBookId);

  OrderBook addOrder(long orderBookId, Order order);

  List<Execution> getExecutions(long orderBookId);

  OrderBook addExecution(long orderBookId, Execution execution);

}
