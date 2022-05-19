package com.dbuddhika.microservice.service;

import com.dbuddhika.microservice.exception.ApiRequestException;
import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.repository.InstrumentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentService {

  private final InstrumentRepository instrumentRepository;

  @Autowired
  public InstrumentService(InstrumentRepository instrumentRepository) {
    this.instrumentRepository = instrumentRepository;
  }

  public List<Instrument> getInstruments() {
    return instrumentRepository.findAll();
  }

  public Instrument addInstrument(Instrument instrument) {
    Optional<Instrument> instrumentByName = instrumentRepository
        .findInstrumentByName(instrument.getName());
    if(instrumentByName.isPresent()) {
      throw new ApiRequestException("instrument already exists"); //BadRequestException
    }
    return instrumentRepository.save(instrument);
  }

  public void deleteInstrument(Long id){
    boolean exists = instrumentRepository.existsById(id);
    if(!exists){
      throw new IllegalStateException("instrument with id "+ id + " not exists");
    }
    instrumentRepository.deleteById(id);
  }

  @Transactional //Entity goes to a managed state
  public void updateInstrument(Long id, String name){

    Instrument instrument = instrumentRepository
        .findById(id)
        .orElseThrow(
            () -> new IllegalStateException("instrument with id "+ id + " not exists")
        );

    if(name != null && name.length() > 0 && !instrument.getName().equals(name)){
      instrument.setName(name);
    }
  }
}
