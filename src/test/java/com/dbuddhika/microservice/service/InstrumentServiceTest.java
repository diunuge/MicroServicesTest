package com.dbuddhika.microservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.repository.InstrumentRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InstrumentServiceTest {

  @Mock
  private InstrumentRepository repository;
  private InstrumentService instrumentService;

  @BeforeEach
  @Disabled
  void setUp() {

    instrumentService = new InstrumentService(repository);
  }

  @AfterEach
  @Disabled
  void tearDown() {
  }

  @Test
  @Disabled
  void getInstruments() {
  }

  @Test
  void testAddInstrument() {

    // given
    Instrument instrument = new Instrument("Bitcoin");

    // when
    instrumentService.addInstrument(instrument);

    // then
    ArgumentCaptor<Instrument> instrumentArgumentCaptor = ArgumentCaptor.forClass(Instrument.class);

    verify(repository).save(instrumentArgumentCaptor.capture());

    Instrument capturedInstrument = instrumentArgumentCaptor.getValue();
    assertThat(capturedInstrument).isEqualTo(instrument);
  }

  @Test
  void testAddInstrumentWhenExist() {

    // given
    Instrument instrument = new Instrument("Bitcoin");

    given(repository.findInstrumentByName(instrument.getName()))
        .willReturn(Optional.of(instrument));

    // when
    // then
    assertThatThrownBy(() ->instrumentService.addInstrument(instrument))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("instrument already exists");

    verify(repository, never()).save(any());
  }

  @Test
  @Disabled
  void deleteInstrument() {
  }

  @Test
  @Disabled
  void updateInstrument() {
  }
}