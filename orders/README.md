# Order Management Service

## Overview
The Order Management Service is a RESTful API designed to manage orders for an e-commerce platform. It provides endpoints to create, retrieve, update, and delete orders, as well as authenticate users.

## Features
- **User Authentication**: Secure login with JWT token generation.
- **Order Management**: Create, retrieve, update, and delete orders.
- **Order Lines**: Manage individual order lines within an order.

## Endpoints

### Login
- **Path**: `/api/auth/login`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "username": "hank",
    "password": "123"
  }
  ```
  **Note**: The database is initialized with these default credentials. Ensure that the login request uses this `username` and `password` combination to authenticate successfully.
- **Response**:
  ```json
  {
    "username": "string",
    "roles": [
      "ADMIN"
    ],
    "token": "string (JWT token)"
  }
  ```
- **Description**: Returns a JWT token that must be used as a Bearer token to access protected endpoints.

### Create Order
- **Path**: `/api/orders`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "orderId": "string",
    "customerName": "string",
    "orderLines": [
      {
        "productId": "string",
        "quantity": "number",
        "price": "decimal"
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "id": "long"
  }
  ```
- **Description**: Create a new order with associated order lines.

### Retrieve Order
- **Path**: `/api/orders/{id}`
- **Method**: `GET`
- **Response**:
  ```json
  {
    "orderId": "string",
    "customerName": "string",
    "orderLines": [
      {
        "productId": "string",
        "quantity": "number",
        "price": "decimal"
      }
    ]
  }
  ```
- **Description**: Retrieve an order by its ID.

### Update Order
- **Path**: `/api/orders/{id}`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "orderId": "string",
    "customerName": "string",
    "orderLines": [
      {
        "productId": "string",
        "quantity": "number",
        "price": "decimal"
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "orderId": "string",
    "customerName": "string",
    "orderLines": [
      {
        "productId": "string",
        "quantity": "number",
        "price": "decimal"
      }
    ]
  }
  ```
- **Description**: Update an existing order by its ID.

### Delete Order
- **Path**: `/api/orders/{id}`
- **Method**: `DELETE`
- **Response**:
  ```json
  {}
  ```
- **Description**: Delete an order by its ID.

## Usage
1. **Login**: Authenticate using the default credentials to obtain a JWT token.
2. **Create Order**: Use the JWT token to create a new order.
3. **Retrieve Order**: Fetch an order by its ID.
4. **Update Order**: Modify an existing order.
5. **Delete Order**: Remove an order by its ID.

