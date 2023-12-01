package com.example.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

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
				assertThat(results.get(i).get("value").intValue() >= results.get(i + 1).get("value").intValue());
			}
		}
	}

	@Test
	public void testCountFrequencyWithText() throws Exception {
		ArrayNode results = getResult("aaaabbbccd");
		assertThat(results).hasSize(4);

		assertThat(results.get(0).get("key").textValue()).isEqualTo("a");
		assertThat(results.get(0).get("value").intValue()).isEqualTo(4);

		assertThat(results.get(1).get("key").textValue()).isEqualTo("b");
		assertThat(results.get(1).get("value").intValue()).isEqualTo(3);

		assertThat(results.get(2).get("key").textValue()).isEqualTo("c");
		assertThat(results.get(2).get("value").intValue()).isEqualTo(2);

		assertThat(results.get(3).get("key").textValue()).isEqualTo("d");
		assertThat(results.get(3).get("value").intValue()).isEqualTo(1);
	}
	@Test
	public void testCountFrequencyConcurrentWithText() throws Exception {
		ArrayNode results = getResult(randomText());

		for (int i = 0; i < results.size() - 1; i++) {
			assertThat(results.get(i).get("value").intValue() >= results.get(i + 1).get("value").intValue());
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

}
