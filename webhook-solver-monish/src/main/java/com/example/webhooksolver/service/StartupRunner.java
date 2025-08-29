package com.example.webhooksolver.service;

import com.example.webhooksolver.domain.SolutionRecord;
import com.example.webhooksolver.dto.GenerateWebhookResponse;
import com.example.webhooksolver.repo.SolutionRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StartupRunner implements ApplicationRunner {

  private final HiringClient hiringClient;
  private final SqlSolver sqlSolver;
  private final SolutionRecordRepository repo;

  @Value("${app.name}")
  private String name;

  @Value("${app.regNo}")
  private String regNo;

  @Value("${app.email}")
  private String email;

  public StartupRunner(HiringClient hiringClient,
                       SqlSolver sqlSolver,
                       SolutionRecordRepository repo) {
    this.hiringClient = hiringClient;
    this.sqlSolver = sqlSolver;
    this.repo = repo;
  }

  @Override
  public void run(ApplicationArguments args) {
    System.out.println("=== StartupRunner: Begin ===");
    try {
      // 1) Generate webhook
      System.out.println("Calling generateWebhook with name=" + name + ", regNo=" + regNo);
      GenerateWebhookResponse resp = hiringClient.generateWebhook(name, regNo, email);
      String webhookUrl = resp == null ? null : resp.getWebhook();
      String jwt = resp == null ? null : resp.getAccessToken();
      System.out.println("Received webhookUrl=" + webhookUrl + " jwt=" + jwt);

      // 2) Prepare solution
      String questionType = sqlSolver.determineQuestionType();
      String finalQuery = sqlSolver.loadFinalQuery();

      // 3) Save to DB
      SolutionRecord record = new SolutionRecord();
      record.setRegNo(regNo);
      record.setQuestionType(questionType);
      record.setFinalQuery(finalQuery);
      record.setWebhookUrl(webhookUrl);
      record.setCreatedAt(Instant.now());
      repo.save(record);
      System.out.println("Saved solution to DB (id=" + record.getId() + ")");

      // 4) Submit solution
      System.out.println("Submitting finalQuery to webhook...");
      hiringClient.submitSolution(webhookUrl, jwt, finalQuery);
      System.out.println("Submission completed.");

    } catch (Exception e) {
      System.err.println("Error during startup flow: " + e.getMessage());
      e.printStackTrace();
    }
    System.out.println("=== StartupRunner: End ===");
  }
}
