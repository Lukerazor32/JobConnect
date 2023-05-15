#!/bin/bash

# Prepare Jar
mvn clean
mvn package

#Ensure, that docker-compose stopped
docker-compose stop

# Add environment variables
export BOT_NAME='Good_music_taste_bot'
export BOT_TOKEN='5782512465:AAEoRUKU94W1XxzvK18r71lI6EvSfwYiUBo'
export BOT_DB_USERNAME='job_connect_user'
export BOT_DB_PASSWORD='YPHYkpdm?'

# Start new deployment
docker-compose up --build -d