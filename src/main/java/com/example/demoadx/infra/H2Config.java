package com.example.demoadx.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class H2Config {
  // custom beans or overrides for dev if needed
}
