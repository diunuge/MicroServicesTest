package com.dbuddhika.microservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.dbuddhika.microservice.model.Execution;
import com.dbuddhika.microservice.model.Order;
import com.dbuddhika.microservice.model.OrderBook;
import com.dbuddhika.microservice.model.OrderBookStatus;
import com.dbuddhika.microservice.model.OrderType;
import com.dbuddhika.microservice.repository.OrderBookRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class OrderBookServiceImplTest {

  @Mock
  private OrderBookRepository orderBookRepository;
  private OrderBookService serviceUnderTest;

  @BeforeEach
  void setUp() {

    serviceUnderTest = new OrderBookServiceImpl(orderBookRepository);

  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testGetOrderBooks() {
    //when
    serviceUnderTest.getOrderBooks();
    //then
    verify(orderBookRepository).findAll();
  }

  @Test
  void testOpen() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .build();

    //when
    OrderBook orderBookOpened = serviceUnderTest.open(orderBook);

    //then
    ArgumentCaptor<OrderBook> orderBookArgumentCaptor = ArgumentCaptor.forClass(OrderBook.class);

    verify(orderBookRepository).save(orderBookArgumentCaptor.capture());

    OrderBook capturedOrderBook = orderBookArgumentCaptor.getValue();
    assertThat(capturedOrderBook).isEqualTo(orderBook);
  }

  @Test
  void testClose() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when
    serviceUnderTest.close(orderBook.getOrderBookId());

    //then
    ArgumentCaptor<OrderBook> orderBookArgumentCaptor = ArgumentCaptor.forClass(OrderBook.class);

    verify(orderBookRepository).save(orderBookArgumentCaptor.capture());

    OrderBook capturedOrderBook = orderBookArgumentCaptor.getValue();
    assertThat(orderBook.getOrderBookStatus()).isEqualTo(OrderBookStatus.CLOSE);
  }

  @Test
  void testCloseWhenOrderBookAlreadyClosed() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.CLOSE)
        .build();

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.close(orderBook.getOrderBookId()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Order Book is already closed!");
  }

  @Test
  void testCloseWhenOrderBookNotExists() {

    //given
    long orderBookId = 1L;

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.close(orderBookId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Order Book with id " + orderBookId + " not exists");
  }

  @Test
  void testGetOrders() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    Order order = Order
        .builder()
        .orderType(OrderType.LIMIT_ORDER)
        .price(45.5)
        .quantity(200)
        .entryDate(new Date())
        .build();

    orderBook.setOrders(new ArrayList<>());
    orderBook.getOrders().add(order);

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when
    serviceUnderTest.getOrders(orderBook.getOrderBookId());

    //then
    verify(orderBookRepository).findById(orderBook.getOrderBookId());
  }

  @Test
  void testGetOrdersWhenOrderBookNotExists() {

    //given
    long orderBookId = 1L;

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.getOrders(orderBookId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Order Book with id " + orderBookId + " not exists");

  }

  @Test
  void testAddOrder() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    Order order = Order
        .builder()
        .orderType(OrderType.LIMIT_ORDER)
        .price(45.5)
        .quantity(200)
        .entryDate(new Date())
        .build();

    orderBook.setOrders(new ArrayList<>());
    orderBook.getOrders().add(order);

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when
    OrderBook addedOrderBook = serviceUnderTest.addOrder(1L, order);

    //then
    ArgumentCaptor<OrderBook> orderBookArgumentCaptor = ArgumentCaptor.forClass(OrderBook.class);

    verify(orderBookRepository).save(orderBookArgumentCaptor.capture());

    OrderBook capturedOrdeBook = orderBookArgumentCaptor.getValue();
    assertThat(capturedOrdeBook).isEqualTo(orderBook);
  }

  @Test
  void testAddOrderWhenOrderBookClosed() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.CLOSE)
        .build();

    Order order = Order
        .builder()
        .orderType(OrderType.LIMIT_ORDER)
        .price(45.5)
        .quantity(200)
        .entryDate(new Date())
        .build();

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.addOrder(orderBook.getOrderBookId(), order))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Order Book is already closed!");

    verify(orderBookRepository, never()).save(any());
  }

  @Test
  void testAddOrderWhenNoOrderBook() {

    //given
    Long orderBookId = 1L;

    Order order = Order
        .builder()
        .orderType(OrderType.LIMIT_ORDER)
        .price(45.5)
        .quantity(200)
        .entryDate(new Date())
        .build();

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.addOrder(orderBookId, order))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Order Book with id " + orderBookId + " not exists");

    verify(orderBookRepository, never()).save(any());
  }

  @Test
  void testGetExecutions() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    Execution execution = Execution
        .builder()
        .quantity(50)
        .price(44.3)
        .build();

    orderBook.setExecutions(new ArrayList<>());
    orderBook.getExecutions().add(execution);

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when
    serviceUnderTest.getOrders(orderBook.getOrderBookId());

    //then
    verify(orderBookRepository).findById(orderBook.getOrderBookId());
  }

  @Test
  void testExecutionsWhenOrderBookNotExists() {

    //given
    long orderBookId = 1L;

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.getExecutions(orderBookId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Order Book with id " + orderBookId + " not exists");

  }

  @Test
  void testAddExecution() {

    //given
    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    Execution execution = Execution
        .builder()
        .quantity(50)
        .price(44.3)
        .build();

    orderBook.setExecutions(new ArrayList<>());
    orderBook.getExecutions().add(execution);

    given(orderBookRepository.findById(orderBook.getOrderBookId()))
        .willReturn(Optional.of(orderBook));

    //when
    OrderBook addedOrderBook = serviceUnderTest.addExecution(1L, execution);

    //then
    ArgumentCaptor<OrderBook> orderBookArgumentCaptor = ArgumentCaptor.forClass(OrderBook.class);

    verify(orderBookRepository).save(orderBookArgumentCaptor.capture());

    OrderBook capturedOrderBook = orderBookArgumentCaptor.getValue();
    assertThat(capturedOrderBook).isEqualTo(orderBook);
  }

  @Test
  void testAddExecutionWhenNoOrderBook() {

    //given
    Long orderBookId = 1L;

    Order order = Order
        .builder()
        .orderType(OrderType.LIMIT_ORDER)
        .price(45.5)
        .quantity(200)
        .entryDate(new Date())
        .build();

    //when

    //then
    assertThatThrownBy(() -> serviceUnderTest.addOrder(orderBookId, order))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Order Book with id " + orderBookId + " not exists");

    verify(orderBookRepository, never()).save(any());
  }

}