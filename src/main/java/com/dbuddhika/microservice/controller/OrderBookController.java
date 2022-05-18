package com.dbuddhika.microservice.controller;

import com.dbuddhika.microservice.model.Execution;
import com.dbuddhika.microservice.model.Order;
import com.dbuddhika.microservice.model.OrderBook;
import com.dbuddhika.microservice.service.OrderBookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/orderbook")
public class OrderBookController {

  private final OrderBookService orderBookService;

  @Autowired
  public OrderBookController(OrderBookService orderBookService) {
    this.orderBookService = orderBookService;
  }

  @PostMapping
  public OrderBook open(@RequestBody OrderBook orderBook) {
    return orderBookService.open(orderBook);
  }

  @DeleteMapping (path = "/{orderBookId}")
  public OrderBook close(@PathVariable("orderBookId") long orderBookId) {
    return orderBookService.close(orderBookId);
  }

  @GetMapping
  public List<OrderBook> getOrderBooks(){
    return this.orderBookService.getOrderBooks();
  }

  @PostMapping (path = "/{orderBookId}/order")
  public OrderBook addOrder(@PathVariable("orderBookId") long orderBookId, @RequestBody Order order) {
    return orderBookService.addOrder(orderBookId, order);
  }

  @GetMapping (path = "/{orderBookId}/order")
  public List<Order> getOrders(@PathVariable("orderBookId") long orderBookId) {
    return orderBookService.getOrders(orderBookId);
  }

  @PostMapping (path = "/{orderBookId}/execution")
  public OrderBook addExecution(@PathVariable("orderBookId") long orderBookId, @RequestBody Execution execution) {
    return orderBookService.addExecution(orderBookId, execution);
  }

  @GetMapping (path = "/{orderBookId}/execution")
  public List<Execution> getExecutions(@PathVariable("orderBookId") long orderBookId) {
    return orderBookService.getExecutions(orderBookId);
  }

}
