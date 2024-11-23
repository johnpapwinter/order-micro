# Orders Microservices

## Overview
The Order Management Service is a microservices-architecture project to manage orders for an eCommerce platform.

## Features
- **User Authentication**: Secure login with JWT token generation.
- **Order Management**: Create, retrieve, update, and delete orders.
- **Order Lines**: Manage individual order lines within an order.
- **Processed Orders**: Logging of Processed orders to a dedicated database.

## Installation Instructions
### Running the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/johnpapwinter/order-micro.git
   cd order-micro
   ```

2. **Docker Setup**

   #### Development Environment

   ```bash
   docker-compose -f docker-compose.dev.yml up
   ```
   #### Production Environment

   ```bash
   docker-compose --env-file .env.prod -f docker-compose.prod.yml up
   ```

3. **Accessing the API**

- **Development**: Access the API directly without authentication.

## Future Improvements
1. Addition of a messaging queue (e.g. RabbitMQ) to handle intra-microservices communication
2. Full registration operations for new users
3. Appropriate error handling and logging for Logging Service
