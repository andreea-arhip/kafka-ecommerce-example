# **Assignment: E-Commerce Event Processing System with Kafka**

## **Overview**

The assignment is to implement an **e-commerce event processing system** using **Apache Kafka** and **Kafka Streams**. The system processes order events, calculates aggregate metrics, and writes results to an output topic for downstream systems. It integrates key components like **Kafka Connect**, **Schema Registry**, and **Spring Boot** for efficient and scalable event-driven architecture.

---

## **Assignment Objectives**

1. Understand how to set up and use **Apache Kafka** for real-time event streaming.
2. Learn to use **Kafka Connect** for integrating Kafka with external systems.
3. Implement **Kafka Streams** to process and aggregate streaming data in real time.
4. Use **Schema Registry** to manage Avro schemas for data serialization and deserialization.
5. Build a modular and maintainable application using **Spring Boot**.

---

## **Project components**
The project consists of the following components:
1. **Producer**: Publishes order events to the `order-created-events` topic.
2. **Kafka Streams Application**:
    - Reads order events from the `order-created-events` topic.
    - Groups orders by customer ID and calculates the total order amount per customer.
    - Writes the aggregated results to the `customer-order-totals` topic.
3. **Consumer**: Reads data from the `customer-order-totals` topic and exposes it using an endpoint
4. **Kafka Connect**: Exports the `customer-order-totals` topic to an external data store.
5. **Schema Registry**: Manages the Avro schemas for `OrderCreated` events and ensures compatibility.

---

## **Tech Stack**

- **Java & Spring Boot**
- **Apache Kafka**: Distributed event streaming platform.
- **Kafka Streams**: Real-time stream processing library.
- **Confluent Schema Registry**: Manages Avro schemas for data serialization.
- **Docker**: For running Kafka, Schema Registry, and Connect in containers.

---


