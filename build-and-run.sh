#!/bin/bash

echo "============================================"
echo " Auction System - Build and Run"
echo "============================================"
echo ""

echo "[1/3] Building JAR files locally..."
echo "This may take a few minutes..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Maven build failed!"
    echo "Check the error messages above."
    exit 1
fi

echo ""
echo "[2/3] Building Docker images..."
docker-compose build

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Docker build failed!"
    exit 1
fi

echo ""
echo "[3/3] Starting all services..."
docker-compose up -d

echo ""
echo "============================================"
echo "  ✅ SUCCESS! All services are running."
echo "============================================"
echo ""
echo "  📋 Services:"
echo "    Auction REST:      http://localhost:8080"
echo "    Swagger:           http://localhost:8080/swagger-ui.html"
echo "    GraphiQL:          http://localhost:8080/graphiql"
echo "    Notifications:     http://localhost:8085"
echo "    Scheduler:         http://localhost:8084/api/scheduler"
echo "    RabbitMQ:          http://localhost:15672 (guest/guest)"
echo ""
echo "  📊 Logs:  docker-compose logs -f"
echo "  🛑 Stop:  docker-compose down"
echo ""