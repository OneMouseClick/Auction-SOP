#!/bin/bash

# Текущая папка (где находится скрипт)
PROJECT_PATH="$(pwd)"

echo "Starting all services from: $PROJECT_PATH"
echo ""

# Функция для запуска сервиса
start_service() {
    local service_name=$1
    local service_path="$PROJECT_PATH/$service_name"

    if [ -d "$service_path" ]; then
        echo "Starting $service_name..."
        osascript -e "tell application \"Terminal\" to do script \"cd '$service_path' && ../mvnw spring-boot:run\""
    else
        echo "ERROR: $service_path not found!"
    fi
}

# Запускаем все сервисы
start_service "grpc-verification-service"
sleep 3

start_service "grpc-verification-client"
sleep 3

start_service "auction-rest"
sleep 3

start_service "auction-scheduler-service"

echo ""
echo "All services started in separate windows."
read -p "Press any key to continue..."