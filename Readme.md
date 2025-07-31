# SpringBoot JPA - Azure Data Explorer

#### Example service consuming weather data from weather_data external table

**External table script:** scripts/external_table.kql

**Datasource config class**: src/main/java/infra.AdxConfig

## Run locally H2 db
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun 

## Run in prod
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun