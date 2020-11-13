# Enrollment Service [![Build Status](https://travis-ci.com/abhithube/enrollment-service.svg?branch=master)](https://travis-ci.com/abhithube/enrollment-service) ![Uptime Robot status](https://img.shields.io/uptimerobot/status/m786310891-a31c47a8cf55015eb4d32123?label=status)


This is the enrollment microservice of the AT Insurance web application. It defines the REST API for updating a member's enrollment status. In addition, Apache Kafka is used to notify other services when a member's enrollment status has changed or when a member's account has been charged. The Stripe API is used to handle payments, and a webhook has been created to receive Stripe invoices and save the payment details into the member's account.

The [README](https://github.com/abhithube/insurance-portal-angular) in the front end repo has more details about the whole project.

**NOTE:** To save on AWS server costs, the application is only up during the day. A cron job scheduler on AWS auto-scaling terminates all of the EC2 instances every night, and starts them up again in the morning. The "status" badge above indicates if the application is currently up.

## API Documentation
The API documentation for this microservice was generated using Swagger. It can be found at https://at-insurance.com/enrollment-service/swagger-ui/.

## Technologies
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Messaging**: Kafka
- **Testing**: JUnit, Mockito
- **Build**: Maven
- **CI/CD**: Travis CI, Docker, AWS
