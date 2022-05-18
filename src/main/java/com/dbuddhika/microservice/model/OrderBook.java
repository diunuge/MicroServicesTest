package com.dbuddhika.microservice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {

  @Id
  @SequenceGenerator(
      name = "orderbook_sequence",
      sequenceName = "orderbook_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "orderbook_sequence"
  )
  private Long orderBookId;

  private Long instrumentId;

  @ElementCollection
  @CollectionTable(name = "orderbook_orders", joinColumns = @JoinColumn(name = "order_book_id"))
  private List<Order> orders;

  @ElementCollection
  @CollectionTable(name = "orderbook_execution", joinColumns = @JoinColumn(name = "order_book_id"))
  private List<Execution> executions;

  private OrderBookStatus orderBookStatus;

  public OrderBook(Long instrumentId, OrderBookStatus orderBookStatus) {
    this.instrumentId = instrumentId;
    this.orderBookStatus = orderBookStatus;
  }

  public OrderBook(Long orderBookId, Long instrumentId, OrderBookStatus orderBookStatus) {
    this.orderBookId = orderBookId;
    this.instrumentId = instrumentId;
    this.orderBookStatus = orderBookStatus;
  }

}
