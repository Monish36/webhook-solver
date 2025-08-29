package com.example.webhooksolver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class SqlSolver {
  @Value("${app.regNo}")
  private String regNo;

  public String determineQuestionType() {
    if (regNo == null) return "Q1";
    String digits = regNo.replaceAll("\\D+", "");
    if (digits.length() == 0) return "Q1";
    try {
      if (digits.length() < 2) {
        int val = Integer.parseInt(digits);
        return (val % 2 == 1) ? "Q1" : "Q2";
      }
      int lastTwo = Integer.parseInt(digits.substring(digits.length() - 2));
      return (lastTwo % 2 == 1) ? "Q1" : "Q2";
    } catch (NumberFormatException e) {
      return "Q1";
    }
  }

  public String loadFinalQuery() {
    try (InputStream is = getClass().getResourceAsStream("/solution.sql")) {
      if (is == null) throw new RuntimeException("solution.sql not found in resources");
      return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
    } catch (Exception e) {
      throw new RuntimeException("Unable to load solution.sql", e);
    }
  }
}
