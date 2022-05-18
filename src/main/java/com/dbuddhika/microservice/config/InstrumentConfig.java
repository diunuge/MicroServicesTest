package com.dbuddhika.microservice.config;

import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.repository.InstrumentRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstrumentConfig {

  @Bean
  CommandLineRunner commandLineRunnerInstrument(InstrumentRepository repository) {
    return args -> {
      Instrument defaultInstrument = new Instrument("Default Instrument");

      repository.saveAll(List.of(defaultInstrument));
    };
  }
}
