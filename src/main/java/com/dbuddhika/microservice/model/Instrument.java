package com.dbuddhika.microservice.model;

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
public class Instrument {

  @Id
  @SequenceGenerator(
      name = "instructor_sequence",
      sequenceName = "instructor_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "instructor_sequence"
  )
  private Long id;

  @Column(unique = true)
  private String name;

  public Instrument(String name) {
    this.name = name;
  }
}
