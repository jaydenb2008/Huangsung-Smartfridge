package edu.sdccd.cisc191.jfxclient.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.common.model.Drink;
import edu.sdccd.cisc191.common.model.FoodItem;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodService {
    private static final String API_BASE_URL = "http://localhost:9090/api/food/foods";
    private final ObjectMapper objectMapper;

    public FoodService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Handle LocalDate
    }

    public List<FoodItem> getAllFoodItems() throws IOException {
        URL url = new URL(API_BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream inputStream = conn.getInputStream()) {
            // return objectMapper.readValue(inputStream, new TypeReference<List<FoodItem>>() {});
            List<Map<String, Object>> rawList = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
            List<FoodItem> result = new ArrayList<>();
            for (Map<String, Object> map : rawList) {
                if (map.containsKey("opened")) {
                    // It's a Drink
                    Drink drink = new Drink();
                    drink.setId(((Number) map.get("id")).longValue());
                    drink.setName((String) map.get("name"));
                    drink.setFoodType((String) map.get("foodType"));
                    drink.setQuantityLeft(((Number) map.get("quantityLeft")).floatValue());
                    drink.setExpirationDate(java.sql.Date.valueOf((String) map.get("expirationDate")));
                    drink.setOpened((Boolean) map.get("opened"));
                    result.add(drink);
                } else {
                    // It's a FoodItem
                    FoodItem item = new FoodItem();
                    item.setId(((Number) map.get("id")).longValue());
                    item.setName((String) map.get("name"));
                    item.setFoodType((String) map.get("foodType"));
                    item.setQuantityLeft(((Number) map.get("quantityLeft")).floatValue());
                    item.setExpirationDate(java.sql.Date.valueOf((String) map.get("expirationDate")));
                    result.add(item);
                }
            }
            return result;
        }
    }

    public void addFoodItem(FoodItem foodItem) throws IOException {
        URL url = new URL(API_BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            objectMapper.writeValue(os, foodItem);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            String errorMsg = readErrorStream(conn);
            throw new IOException("Failed to add food item: " + errorMsg);
        }
    }

    public void removeFoodItem(Long id) throws IOException {
        URL url = new URL(API_BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 204) {
            String errorMsg = readErrorStream(conn);
            throw new IOException("Failed to delete food item: " + errorMsg);
        }
    }

    // Optional: Add update support (PUT)
    public void updateFoodItem(FoodItem foodItem) throws IOException {
        URL url = new URL(API_BASE_URL + "/" + foodItem.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = conn.getOutputStream()) {
            objectMapper.writeValue(os, foodItem);
        }
        if (conn.getResponseCode() != 200) {
            String errorMsg = readErrorStream(conn);
            throw new IOException("Failed to update food item: " + errorMsg);
        }
    }

    private String readErrorStream(HttpURLConnection conn) throws IOException {
        try (InputStream es = conn.getErrorStream()) {
            if (es == null) return "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(es, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
