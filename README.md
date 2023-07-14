
# Blog Application

The subject of this project is to build the system for blog application with java and spring boot.



## Microservice diagram


![microservice_diagram](https://github.com/FurkanAr/BlogApp/assets/63981707/114db51a-52aa-46de-9227-18a716576a86)

## Requirements

• Users must be able to register and login to the system.

• After the user registration, an e-mail should be sent.

• Users can only see the written articles and comments without registering to the system.

• Users can post articles, comment, create tags and like articles after they are registered in the system.

• Admin can view user user information, memberships and payments made for these memberships.

• Users should be able to search for articles by tags.

• If the purchase is successful, the transaction should be completed and the ticket details should be sent to the user's e-mail account asynchronously.

• Only database registration is sufficient for Mail Notification sending operations. But these operations should be done through a single Service (application) and with polymorphic behavior.

• Users should be able to see their subscriptions and payments.
## System Acceptances

• Users can only be of one type: User, Admin.

• After the user becomes a member of the system, he/she can share a maximum of 10 articles during the current month.

• Membership period is 1 month. The user can extend this period before the end of the month.

• Mail Notification sending processes must be Asynchronous.

• Payment method can only be Credit card and Money Order / EFT.

• Payment Service transactions must be Synchronous.
## Used technologies:

• Java 17

• JUnit

• RESTful

• Spring Boot

• Spring Security

• Spring JPA

• Docker

• PostgreSQL

• MongoDB

• RabbitMQ

• Postman

## Authors

- [@Furkan Ar](https://www.github.com/FurkanAr)
