#!/bin/bash
set -e

# Сборка jar
mvn clean package

# Остановить предыдущий деплой
docker-compose stop || true

# Переменные окружения берём из .env (шаблон — .env.example)
set -a
[ -f .env ] && . ./.env
set +a

# Запуск
docker-compose up --build -d
