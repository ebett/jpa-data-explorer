package com.example.demoadx.infra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SqlAuthenticationToken;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
@Configuration
@Profile("prod")
public class AdxConfig {
  @Value("${adx-cluster-dns}")
  private String clusterDNS;

  @Value("${adx-db-server}")
  private String databaseServer;

  @Value("${adx-db-name}")
  private String dataBaseName;

  private final Environment environment;

  private final ObjectMapper objectMapper;

  @Bean
  public DataSource sqlServerDataSource() {
    System.out.printf("Init ADX data source %s/%s ...\n", clusterDNS, dataBaseName);
    SQLServerDataSource dataSource = new SQLServerDataSource();
    dataSource.setServerName(databaseServer);
    dataSource.setDatabaseName(dataBaseName);

    dataSource.setAccessTokenCallback((spn, stsurl) -> {
      final String accessToken = acquireToken();
      return new SqlAuthenticationToken(accessToken,
          Date.from(Instant.now().plus(45, ChronoUnit.MINUTES)));
    });
    dataSource.setTrustServerCertificate(true);

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDataSource(dataSource);

    return new HikariDataSource(hikariConfig);
  }

  private String acquireToken() {
    final String azureClientId = environment.getProperty("AZURE_CLIENT_ID");
    final String azureClientSecret = environment.getProperty("AZURE_CLIENT_SECRET");
    final String azureTenantId = environment.getProperty("AZURE_TENANT_ID");
    final String scope = "https://" + clusterDNS + "/.default";

    final String tokenUri = "https://login.microsoftonline.com/" + azureTenantId
        + "/oauth2/v2.0/token";

    final String postBody = String.format(
        "grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s",
        azureClientId, azureClientSecret, scope);

    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(tokenUri))
          .POST(HttpRequest.BodyPublishers.ofString(postBody))
          .header("Accept", "any")
          .header("Content-Type", "application/x-www-form-urlencoded").build();

      final HttpClient client = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        System.out.printf("Access Token response status: %s\n", response.statusCode());
      }
      final JsonNode jsonResponse = objectMapper.readTree(response.body());
      final String accessToken = jsonResponse.get("access_token").asText();

      return accessToken;
    } catch (Exception e) {
      System.out.printf("Error calling acquireToken: %s\n", e.getMessage());
    }

    return null;
  }
}
