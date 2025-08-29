package com.example.webhooksolver.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "solution_record")
public class SolutionRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String regNo;
  private String questionType;

  @Lob
  @Column(length = 20000)
  private String finalQuery;

  private String webhookUrl;
  private Instant createdAt;

  public SolutionRecord() {}

  public Long getId() { return id; }

  public String getRegNo() { return regNo; }
  public void setRegNo(String regNo) { this.regNo = regNo; }

  public String getQuestionType() { return questionType; }
  public void setQuestionType(String questionType) { this.questionType = questionType; }

  public String getFinalQuery() { return finalQuery; }
  public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }

  public String getWebhookUrl() { return webhookUrl; }
  public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
