package com.example.demoadx.infra;

import com.example.demoadx.domain.WeatherDataRepository;
import com.example.demoadx.domain.entities.WeatherData;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@Profile("local")
@RequiredArgsConstructor
public class WeatherDataSeeder {

  private final WeatherDataRepository repository;

  private static final Random random = new Random();

  @PostConstruct
  public void init() {
    if (repository.count() == 0) {
      // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS");

      for (int day = 1; day <= 30; day++) {
        LocalDate metricDate = LocalDate.of(2025, 1, day);
        //String metricDate = date.atStartOfDay().format(formatter);

        double minTemp = randomDouble(-15, 0);
        double maxTemp = randomDouble(0, 10);
        double precipitation = randomDouble(0, 5);
        double minWind = randomDouble(0, 10);
        double maxWind = randomDouble(minWind, 20);
        double cloudCover = randomDouble(0, 1);

        WeatherData weatherData = new WeatherData(
            metricDate,
            minTemp,
            maxTemp,
            precipitation,
            minWind,
            maxWind,
            cloudCover
        );

        repository.save(weatherData);
      }
    }
  }

  private static double randomDouble(double min, double max) {
    return Math.round((min + (max - min) * random.nextDouble()) * 100.0) / 100.0;
  }
}
