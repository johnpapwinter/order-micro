# Logging Service

## Overview

## Features
- **
- **

### Endpoints
- **Create Log**
  - **Path**: `/api/logs`
  - **Method**: `POST`
  - **Request Body**:
  ```json
  {
    "timestamp": "timestamp",
    "thread": "string",
    "level": "string",
    "logger": "string",
    "message": "string"
  }
  ```
  - **Description**: Will save a new log
- **Fetch Logs**
  - **Path**: `/api/logs`
  - **Method**: `GET`
  - 
