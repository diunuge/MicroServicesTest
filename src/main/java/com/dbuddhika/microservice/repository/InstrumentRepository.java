package com.dbuddhika.microservice.repository;

import com.dbuddhika.microservice.model.Instrument;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository
    extends JpaRepository<Instrument, Long> {

  @Query("SELECT i FROM Instrument i WHERE i.name = ?1")
  Optional<Instrument> findInstrumentByName(String name);
}
