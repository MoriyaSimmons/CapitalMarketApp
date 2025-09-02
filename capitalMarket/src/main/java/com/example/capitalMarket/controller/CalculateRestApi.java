package com.example.capitalMarket.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.CalculateRequest;
import model.CalculateResponse;

@RestController
@RequestMapping("/calculate")
public class CalculateRestApi {

	@Value("${twelvedata.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final File historyFile = new File("stock-history.json");

	@PostMapping
	public ResponseEntity<List<CalculateResponse>> calculate(@RequestBody CalculateRequest req) throws IOException {
		List<CalculateResponse> responses = new ArrayList<>();

		String[] symbols = req.getSymbol().split(",");
		int quantity = req.getQuantity();

		for (String rawSymbol : symbols) {
			String symbol = rawSymbol.trim().toUpperCase();

			double price = getCurrentPriceFromAPI(symbol, apiKey);
			 if (price == -1) {
		            // סימון שהמניה לא חוקית
		            CalculateResponse errorResponse = new CalculateResponse(symbol, -1, -1);
		            responses.add(errorResponse);
		            continue;
		        }

			double total = price * quantity;

			CalculateResponse response = new CalculateResponse(symbol, price, total);
			responses.add(response);
		}

		saveToHistory(responses);
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/history")
	public ResponseEntity<List<CalculateResponse>> getHistory() throws IOException {
		if (!historyFile.exists()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		return ResponseEntity.ok(Arrays.asList(objectMapper.readValue(historyFile, CalculateResponse[].class)));
	}

	private double getCurrentPriceFromAPI(String symbol, String apiKey) {
		try {
			String url = "https://api.twelvedata.com/price?symbol=" + symbol + "&apikey=" + apiKey;

			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			JsonNode node = objectMapper.readTree(response.getBody());

			if (node.has("price")) {
				return Double.parseDouble(node.get("price").asText());
			} else {
				System.err.println("API Error: " + node.toString());
				return -1;
			}
		} catch (Exception e) {
			System.err.println("API call failed for symbol: " + symbol);
			return -1;
		}
	}

	private void saveToHistory(List<CalculateResponse> newEntries) throws IOException {
		List<CalculateResponse> history = new ArrayList<>();

		if (historyFile.exists() && historyFile.length() > 0) {
			history.addAll(Arrays.asList(objectMapper.readValue(historyFile, CalculateResponse[].class)));
		}

		history.addAll(newEntries);

		objectMapper.writerWithDefaultPrettyPrinter().writeValue(historyFile, history);
	}

}
