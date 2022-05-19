package com.dbuddhika.microservice.controller;

import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.service.InstrumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/instrument")
public class InstrumentController {

  private final InstrumentService instrumentService;

  @Autowired
  public InstrumentController(InstrumentService instrumentService) {
    this.instrumentService = instrumentService;
  }

  @GetMapping
  public List<Instrument> getInstruments(){
    return this.instrumentService.getInstruments();
  }

  @PostMapping
  public Instrument registerNewInstrument(@RequestBody Instrument instrument){
    return instrumentService.addInstrument(instrument);
  }

  @DeleteMapping(path = "/{instrumentId}")
  public void deleteInstrument(@PathVariable("instrumentId") Long id){
    instrumentService.deleteInstrument(id);
  };

  @PutMapping (path = "/{instrumentId}")
  public void updateInstrument(
      @PathVariable("instrumentId") Long id,
      @RequestParam String name){
    instrumentService.updateInstrument(id, name);
  }
}
