package com.example.demoadx.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "weather_data")
public class WeatherData {

  @Id
  @Column(name = "metric_date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate metricDate;

  @Column(name = "min_temperature")
  private Double minTemperature;

  @Column(name = "max_temperature")
  private Double maxTemperature;

  @Column(name = "total_precipitation")
  private Double totalPrecipitation;

  @Column(name = "min_wind_speed")
  private Double minWindSpeed;

  @Column(name = "max_wind_speed")
  private Double maxWindSpeed;

  @Column(name = "avg_total_cloud_cover")
  private Double avgTotalCloudCover;

}
