#!/bin/bash

# Prepare Jar
mvn clean
mvn package

#Ensure, that docker-compose stopped
docker-compose stop

# Add environment variables
export BOT_NAME='Good_music_taste_bot'
export BOT_TOKEN='${BOT_TOKEN}'
export BOT_DB_USERNAME='job_connect_user'
export BOT_DB_PASSWORD='${BOT_DB_PASSWORD}'

# Start new deployment
docker-compose up --build -d