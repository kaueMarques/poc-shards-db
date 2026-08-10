# Project Architecture

## Overview
A basic Java application accessing an AWS RDS database and communicating via Amazon SQS.

## Core Components
- **Persistence Layer:** Uses JPA/Hibernate with JDBC driver for RDS access.
- **Messaging Layer:** Uses AWS SDK for Java for SQS interaction.
- **Configuration:** Externalized using properties/environment variables for RDS and AWS credentials.

## Directory Structure
- `src/main/java/com/poc/`
    - `config/` (RDS, AWS clients)
    - `model/` (JPA Entities)
    - `repository/` (JPA Repositories)
    - `messaging/` (SQS Producer/Consumer)
- `.spec/` (Project documentation/specifications)

## Status
- [DONE] Project architecture documented.
