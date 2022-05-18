package com.dbuddhika.microservice.repository;

import com.dbuddhika.microservice.model.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {

}
