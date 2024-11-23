# Logging Microservice

## Overview
The Logging Microservice is a RESTful API designed to log orders for an e-commerce platform. It provides endpoints to save and retrieve logged orders, enabling comprehensive tracking and analysis of order activities.

## Features
- **Order Logging**: Save detailed logs of orders.
- **Order Retrieval**: Retrieve logged orders with pagination support.

## Endpoints

### Save Logged Order
- **Path**: `/api/logs`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "id": "string",
    "orderId": "string",
    "amount": "double",
    "itemsCount": "integer",
    "date": "yyyy-MM-dd"
  }
  ```
- **Response**:
  ```json
  {}
  ```
- **Description**: Save a new logged order.

### Retrieve Logged Orders
- **Path**: `/api/logs`
- **Method**: `GET`
- **Query Parameters**:
  - `page`: The page number to retrieve (default is 0).
  - `size`: The number of items per page (default is 10).
  - `sort`: The sorting criteria (e.g., `date,asc` for ascending order by date).
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "string",
        "orderId": "string",
        "amount": "double",
        "itemsCount": "integer",
        "date": "yyyy-MM-dd"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": "boolean",
        "unsorted": "boolean",
        "empty": "boolean"
      },
      "offset": "long",
      "pageNumber": "integer",
      "pageSize": "integer",
      "paged": "boolean",
      "unpaged": "boolean"
    },
    "totalPages": "integer",
    "totalElements": "long",
    "last": "boolean",
    "size": "integer",
    "number": "integer",
    "sort": {
      "sorted": "boolean",
      "unsorted": "boolean",
      "empty": "boolean"
    },
    "numberOfElements": "integer",
    "first": "boolean",
    "empty": "boolean"
  }
  ```
- **Description**: Retrieve logged orders with pagination support.

## Usage
1. **Save Logged Order**: Log a new order by sending a POST request with the order details.
2. **Retrieve Logged Orders**: Fetch logged orders with pagination support by sending a GET request.
