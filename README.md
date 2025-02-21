# **Assignment: E-Commerce Event Processing System with Kafka**

## **Note for the reader**
I've used this assignment as a playground to learn Spring Cloud Stream with Apache Kafka. 
If you've stumbled upon this repository, feel free to do the same and even raise PRs if you feel like refactoring / enriching this code.

---
## **Assignment Overview**

The assignment is to implement an **e-commerce event processing system** using **Apache Kafka** and **Spring Cloud Streams with Kafka**. The system processes order events, calculates aggregate metrics, and writes results to an output topic for downstream systems. It integrates key components like **Kafka Connect**, **Schema Registry**, and **Spring Boot** for efficient and scalable event-driven architecture.

---

## **Assignment Objectives**

1. Understand how to set up and use **Apache Kafka** for real-time event streaming.
2. Learn to use **Kafka Connect** for integrating Kafka with external systems.
3. Implement **Kafka Streams** and **Spring cloud streams** to process and aggregate streaming data in real time.
4. Use **Schema Registry** to manage Avro schemas for data serialization and deserialization.
5. Build a modular and maintainable application using **Spring Boot**.

---

## **Phase I - basic requirements**:
1. **Producer**: Build a Kafka producer app that:
    - Receives newly created orders via REST
    - Publishes these orders to the `order-created-events` Kafka topic.
    - The `order-created-events` should have the following fields:
      ```
       {
         "orderId": "string",
         "customerId": "string",
         "productId": "string",
         "quantity": "int",
         "orderAmount": "double",
         "orderTimestamp": "string",
         "status": "string"
       }
      ```
    - The `status` field should have the following possible values: COMPLETED, FAILED, PENDING
   
2. **Kafka Streams Application**: Build a Kafka stream application that:
    - Reads order events from the `order-created-events` topic.
    - Groups orders by customer ID and calculates the total order amount per customer.
    - Writes the aggregated results to the `customer-order-totals` topic.
3. **Consumer**: Build a Kafka Consumer app that:
    - reads data from the `customer-order-totals` topic
    - exposes the data using a REST endpoint
4. **Kafka Connect**: Exports the `customer-order-totals` topic to an external data store.
5. **Schema Registry**: Manages the Avro schemas for `OrderCreatedEvent` events and ensures compatibility.

---

## **Phase II - more advanced requirements**:

1. **Add another producer**:
   - Publish `inventory` items with the following fields:
     ```
       {
         "productId": "string",
         "stockAvailable": "int",
         "threshold": "int"
       }
      ```
2. **Implement more advanced Spring Cloud Streams Processing**
   1. **Fraud Detection Service**:
      - Detect fraudulent transactions based on rules (e.g., order amount exceeding $5000).
      - Send flagged transactions to a `fraud-alert-order-totals` Kafka topic with fields:
        ```
           {
             "customerId": "string",
             "totalAmount": "double",
             "message": "string"
           }
        ```
   2. **Shipment service**:
      - Send the valid transactions (that have not been flagged as possibly fraudulent) to shipment.
      - The shipment has a status with two possible values:
        - LOW_PRIORITY (totalAmount < 500$)
        - HIGH_PRIORITY (totalAmount > 500$)
      - Publish `shipment` items with the following fields:
        ```
           {
              "shipmentId": "string",
              "orderId": "string",
              "shipmentStatus": "string"
           }
        ```
   3. **Order enrichment service (Chained event processing)**
      - When an order is received, enrich it with customer details from a database (ex: customerEmail). 
      - Send enriched order to a new Kafka topic (enriched-orders). 
      - Downstream consumers can process enriched data instead of raw orders.
   4. **Order summary processing**: Join the orders, inventory, and shipments topics to produce an enriched order-summary topic
      - The `order-summary` topic should include:
          - Order details.
          - Inventory availability for ordered products.
          - Shipment status
   5. **Low-Stock Alerts**: 
       - Monitor the inventory topic for low stock conditions
       - Generate alerts on the `inventory-alerts` topic when stockAvailable falls below the threshold.
   6. **Windowed Aggregation**:
       - Perform a 15-minute aggregation of total sales for each product.
       - Output the results to the product-sales topic with fields:
          ```
            {
              "productId": "string",
              "salesAmount": "double",
              "windowEnd": "long"
            }
          ```
   7. **Error Handling**:
       - Handle invalid events with missing or malformed fields
       - Redirect invalid events from the orders topic to the `failed-orders` topic for further investigation.

3. **Schema Evolution**
   - Use **Avro** for message serialization and enable schema evolution via **Schema Registry**.

4. **Kafka Connect Integration**
   - Use **Kafka Connect** to import and export data:
      - Import orders from a CSV file into the `order-created-topic` topic using a **File Source Connector**.
      - Export data from the `consumer-order-totals` topic to an external database (e.g., PostgreSQL) using a **JDBC Sink Connector**.

---

## **Tech Stack**

- **Java & Spring Boot**
- **Apache Kafka**: Distributed event streaming platform.
- **Spring Cloud Stream**: Framework for building highly scalable event-driven microservices.
- **Confluent Schema Registry**: Manages Avro schemas for data serialization.
- **Docker**: For running Kafka, Schema Registry, and Connect in containers.

---


