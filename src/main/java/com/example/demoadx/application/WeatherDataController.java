package com.example.demoadx.application;

import com.example.demoadx.domain.entities.WeatherData;
import com.example.demoadx.domain.WeatherDataRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

@RequiredArgsConstructor
@RestController
public class WeatherDataController {

  private final WeatherDataRepository weatherDataRepository;

  @Operation(summary = "Get weather data by date range")
  @GetMapping(path = "/weather-data")
  public List<WeatherData> findAll(
    @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Parameter(
        description = "Start date in ISO format (yyyy-MM-dd)",
        required = true,
        example = "2023-01-01"
    ) LocalDate startDate,
      @RequestParam
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @Parameter(
          description = "End date in ISO format (yyyy-MM-dd)",
          required = true,
          example = "2023-01-31"
      ) LocalDate endDate) {

    return weatherDataRepository.findAllByMetricDateBetween(
        startDate, endDate, PageRequest.of(0, 50))
        .toList();
  }
}
