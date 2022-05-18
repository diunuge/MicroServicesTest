package com.dbuddhika.microservice.model;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Enumerated(value = EnumType.STRING)
  private OrderType orderType;

  private Integer quantity;

  private Double price;

  private Date entryDate;
}
