#!/bin/sh
./mvnw clean package
java -jar target/dot-matrix-1.1.0.jar
