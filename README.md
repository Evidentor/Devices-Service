# Devices-Service

## About
The Devices Service is responsible for managing communication with 
IoT devices. It receives telemetry data from devices via MQTT and 
handles all other interactions, including configuration updates, 
commands, and status monitoring. This service acts as the central 
hub for device connectivity and control.

## System Requirements

- Java 21
- Apache Maven 3.9.9
- Docker (if running the service within the Docker container)

## Configuration

### Database
Configure database connection in `application.yaml` file:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/evidentor
    driverClassName: org.postgresql.Driver
    username: postgres
    password: postgres
```

### MQTT Broker
Configure MQTT connection in `application.yaml` file:
```yaml
mqtt:
  broker-url: tcp://localhost:1883
  client-id: devicesServiceClient
```

#### Decision Service
Configure connection to Decision Service in `application.yaml` file:
```yaml
spring:
  grpc:
    client:
      decision:
        host: localhost
        port: 9092
```

## How to Install?

### 1. Clone the repository
```shell
git clone https://github.com/Evidentor/Devices-Service.git
cd Devices-Service
```

### 2. Install dependencies
```shell
mvn clean install
```

## How to Run?

### Run with java
```shell
java --enable-preview -jar target/*.jar
```

### Run with docker
#### 1. Build the docker image
```shell
docker build -t devices-service .
```

#### 2. Create the docker container
```shell
docker run -d --network host --name devices-service devices-service:latest
```

#### 3. Stop the docker container
```shell
docker stop devices-service
```

#### 4. Start the docker container
```shell
docker start devices-service
```

## How to Test?
```shell
mvn test
```
