# Assignment idea: E-commerce Real-Time Order Processing System
In this assignment, students will create a microservice-based system for an e-commerce platform using Spring Boot, Kafka, Kafka Streams, and Confluent Schema Registry. The system will consist of a producer, consumer, and streams processor for real-time order processing, fraud detection, and transaction analytics. The system will simulate the order lifecycle in an e-commerce platform, where orders are placed, processed, and analyzed for fraud and other business insights.

## Overview of the E-commerce System:
The system will have the following components:

### 1. Order Service (Producer):
A Spring Boot service that simulates customer orders and sends them to a Kafka topic (Order Created Event). 

### 2. Order Processing Service (Consumer):
A Spring Boot service that listens to the Order Created topic, processes the orders, checks for fraud, and generates an Order Processed event.

### 3. Kafka Streams Processor (Fraud Detection and Transaction Analytics):
A Kafka Streams application that performs real-time fraud detection and generates transaction analytics (e.g., calculating total order value per customer in a time window).

### 4. Schema Registry:
Use Confluent Schema Registry to manage Avro schemas for the messages sent between services. The schemas will ensure that data is structured in a consistent format across the producer, consumer, and Kafka Streams processor.


## Assignment Objectives:
- **Basic Concepts**: Implementing a producer, consumer, and using Kafka as the message broker.
- **Advanced Concepts**: Implementing Kafka Streams for real-time processing and analytics, using Confluent Schema Registry to manage Avro schemas, and handling message serialization and deserialization.
