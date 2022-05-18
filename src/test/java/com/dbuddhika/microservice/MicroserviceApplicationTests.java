package com.dbuddhika.microservice;

import com.dbuddhika.microservice.model.Instrument;
import com.dbuddhika.microservice.repository.InstrumentRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class MicroserviceApplicationTests {

	@Autowired
	private InstrumentRepository repository;

	@Autowired
	private MockMvc mockMvc;

	// given/ when/ then  format - BDD Style
	@Test
	public void givenInstrument_whenGetAllInstructors_thenListOfStudents() throws Exception {

		//given - setup or precondition
		Instrument instrument = Instrument.builder().name("Bitcoin").build();
		repository.save(instrument);

		//when - action
		ResultActions responce = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/instrument"));

		//then - verify the output
		responce.andExpect(MockMvcResultMatchers.status().isOk());
		responce.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2))); //size

	}
}
