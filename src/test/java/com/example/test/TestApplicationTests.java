package com.example.test;

import com.example.test.rep.ResponseRep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ResponseRep rep;

	@Test
	public void testCountFrequencyConcurrentWithEmptyText() throws Exception {
		mockMvc.perform(post("")
						.param("text", " ")
						.param("number", "2"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.details").value("Параметр 'text' не может быть пустым"));
	}

	@Test
	public void testGetAll() throws Exception {
		MvcResult result = mockMvc.perform(get(""))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		for (JsonNode j : jsonNode) {
			ArrayNode results = (ArrayNode) j.get("results");
			for (int i = 0; i < results.size() - 1; i++) {
				assertThat(results.get(i).elements().next().intValue() >= results.get(i + 1).elements().next().intValue());
			}
		}
	}

	@Test
	public void testCountFrequencyWithText() throws Exception {
		ArrayNode results = getResult("aaaabbbccd");
		assertThat(results).hasSize(4);

		assertThat(results.get(0).fields().next().getKey()).isEqualTo("a");
		assertThat(results.get(0).elements().next().asInt()).isEqualTo(4);

		assertThat(results.get(1).fields().next().getKey()).isEqualTo("b");
		assertThat(results.get(1).elements().next().asInt()).isEqualTo(3);

		assertThat(results.get(2).fields().next().getKey()).isEqualTo("c");
		assertThat(results.get(2).elements().next().asInt()).isEqualTo(2);

		assertThat(results.get(3).fields().next().getKey()).isEqualTo("d");
		assertThat(results.get(3).elements().next().asInt()).isEqualTo(1);
	}
	@Test
	public void testCountFrequencyConcurrentWithText() throws Exception {
		ArrayNode results = getResult(randomText());

		for (int i = 0; i < results.size() - 1; i++) {
			assertThat(results.get(i).elements().next().intValue() >= results.get(i + 1).elements().next().intValue());
		}
	}

	private String randomText() {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;':,.<>?/";
		Random random = new Random();
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			char c = characters.charAt(random.nextInt(characters.length()));
			text.append(c);
		}
		return text.toString();
	}

	private ArrayNode getResult(String text) throws Exception {
		MvcResult result = mockMvc.perform(post("")
						.param("text", text)
						.param("number", "2"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		return  (ArrayNode) jsonNode.get("data").get("results");
	}

	@Test
	public void getWhenEmpty() throws Exception {

		when(rep.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get(""))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.details").value("В базе данных пока ничего нет"));
	}

}
