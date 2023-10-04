# Notifications service system

> A web application that allows the user to add other people with their contact information, create a message template and send notifications to specified devices to all selected people.

## Base version:

### Requirements:

1. User send data to Server
2. User create a template - select people and their devices
3. Send notifications to any receiver's device, while sending to group of people

### Logic for that:

+ Application parses contactData.csv from User
+ Using database
+ Intergation with outer sending services like Amazon SES, Twilio, ApplePush , ... , etc
  
![scheema1](./service_1.0.png)

## Advanced version:

### Requirements:

1. Notifications can be sent to a million people simultaneously
2. All notifications must reach their recipient
3. The sending application should not be the bottleneck, it's death situation should be handled

### Logic for that:

+ Separation of the logic for receiving requests and sending notifications
+ Multiple copies of API and worker services
+ Kafka distributes the load between sending and receiver services
+ The Realancer monitors the status of notifications, marking them in the database

![scheema2](./service_2.0.png)

