# Description
This is a driver program which produces and writes the store receipt data in json format to a kafka topic

## Instructions:

Compile the project using command:

```
    mvn clean package
```

Inside the /target folder fat jar called kafka-producer-1.0-jar-with-dependencies.jar can be found.

In order to launch the producer run
```
    java -jar kafka-producer-1.0-jar-with-dependencies.jar <kafka broker> <topicname>
```

Sample command for running on local machine:
```
    java -jar kafka-producer-1.0-jar-with-dependencies.jar localhost:9092 sales_receipts
```
