package com.dbuddhika.microservice.config;

import com.dbuddhika.microservice.model.Execution;
import com.dbuddhika.microservice.model.Order;
import com.dbuddhika.microservice.model.OrderBook;
import com.dbuddhika.microservice.model.OrderBookStatus;
import com.dbuddhika.microservice.model.OrderType;
import com.dbuddhika.microservice.repository.OrderBookRepository;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBookConfig {

  @Bean
  CommandLineRunner commandLineRunnerOrderBook(OrderBookRepository repository) {
    return args -> {
      OrderBook ob = new OrderBook(1L, OrderBookStatus.OPEN);

      Order order = Order
          .builder()
          .orderType(OrderType.MARKET_ORDER)
          .quantity(200)
          .entryDate(new Date())
          .build();
      ob.setOrders(List.of(order));

      Execution execution = Execution
          .builder()
          .quantity(30)
          .price(22.3)
          .build();

      ob.setExecutions(List.of(execution));

      repository.saveAll(List.of(ob));
    };
  }

}
