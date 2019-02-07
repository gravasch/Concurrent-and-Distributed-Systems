# Concurrent-and-Distributed-Systems
Client - Server Java Examples of Concurrent and Distributed Systems (e.g Sockets, RMI, MOM)

In this project i built a simple distributed system with three levels: clients, server and database.The server manages a shared object (counter). Customers perform repeat concurrent get, inc, dec operations in random order and random frequency.
The server keeps log with customer activity in the database (day / yar, ip client, meter value).

Used technologies: Sockets, RPC / RMI, MoM
Java Requirements /  Dependencies:

1 . amqp-client-5.5.1.jar
2 . mysql-connector-java-8.0.14.jar
3 . sqlite-jdbc-3.6.20.1.jar
