package com.example.webhooksolver.service;

import com.example.webhooksolver.dto.GenerateWebhookRequest;
import com.example.webhooksolver.dto.GenerateWebhookResponse;
import com.example.webhooksolver.dto.SubmitSolutionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HiringClient {
  private final WebClient webClient;

  @Value("${external.generateWebhookUrl}")
  private String generateWebhookUrl;

  @Value("${external.fallbackSubmitUrl}")
  private String fallbackSubmitUrl;

  public HiringClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {
    GenerateWebhookRequest req = new GenerateWebhookRequest(name, regNo, email);
    return webClient.post()
        .uri(generateWebhookUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(req)
        .retrieve()
        .bodyToMono(GenerateWebhookResponse.class)
        .block();
  }

  public void submitSolution(String submitUrl, String jwt, String finalSql) {
    SubmitSolutionRequest body = new SubmitSolutionRequest(finalSql);
    String target = (submitUrl != null && !submitUrl.isBlank()) ? submitUrl : fallbackSubmitUrl;
    webClient.post()
        .uri(target)
        .header(HttpHeaders.AUTHORIZATION, jwt == null ? "" : jwt)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
