package com.example.demoadx.domain;

import com.example.demoadx.domain.entities.WeatherData;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, String> {

  Slice<WeatherData> findAllByMetricDateBetween(
      LocalDate dateAfter, LocalDate dateBefore, Pageable pageable);
}
