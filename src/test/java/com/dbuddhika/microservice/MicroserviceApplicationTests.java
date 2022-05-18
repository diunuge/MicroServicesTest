package com.dbuddhika.microservice;

import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.model.OrderBook;
import com.dbuddhika.microservice.model.OrderBookStatus;
import com.dbuddhika.microservice.repository.InstrumentRepository;
import com.dbuddhika.microservice.repository.OrderBookRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class MicroserviceApplicationTests {

  //Integration Tests
  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private OrderBookRepository orderBookRepository;

  @Autowired
  private MockMvc mockMvc;


  // given/ when/ then  format - BDD Style
  @Test
  public void integrationTestGetInstruments() throws Exception {

    //given - setup or precondition
    int totalNumberOfInstruments = 1;

    Instrument instrument = Instrument
        .builder()
        .name("Bitcoin")
        .build();

    instrumentRepository.save(instrument);

    //when - action
    ResultActions apiResponce = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/instrument"));

    //then - verify the output
    apiResponce.andExpect(MockMvcResultMatchers.status().isOk());
    apiResponce.andExpect(MockMvcResultMatchers.jsonPath("$.size()",
        CoreMatchers.is(totalNumberOfInstruments))); //size

  }

  @Test
  public void interationTestOpenOrderBook() throws Exception {

    //given
    Instrument instrument = Instrument
        .builder()
        .name("USDT")
        .build();

    instrumentRepository.save(instrument);

    //when - action
    ResultActions apiResponce = mockMvc
        .perform(MockMvcRequestBuilders
            .post("/api/v1/orderbook")
            .content("{\"instrumentId\": 1}")
            .contentType(MediaType.APPLICATION_JSON)
        );

    //then - verify the output
    apiResponce.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());

  }


  @Test
  public void interationTestAddOrder() throws Exception {

    //given
    Instrument instrument = Instrument
        .builder()
        .name("Eth")
        .build();

    instrumentRepository.save(instrument);

    OrderBook orderBook = OrderBook
        .builder()
        .instrumentId(1L)
        .orderBookStatus(OrderBookStatus.OPEN)
        .build();

    orderBookRepository.save(orderBook);

    //when - action
    ResultActions apiResponce = mockMvc
        .perform(MockMvcRequestBuilders
            .post("/api/v1/orderbook/1/order", orderBook)
            .content("{\n"
                + "  \"orderType\": \"MARKET_ORDER\",\n"
                + "  \"quantity\": 500,\n"
                + "  \"price\": 44.4,\n"
                + "  \"entryDate\": \"2022-05-18T11:11:48.655+00:00\"\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
        );

    //then - verify the output
    apiResponce.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());

  }
}
