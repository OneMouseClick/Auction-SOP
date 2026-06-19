@echo off
echo Starting all services...

start "gRPC Verification Server" cmd /c "cd grpc-verification-service && ..\mvnw spring-boot:run"
timeout /t 5

start "gRPC Verification Client" cmd /c "cd grpc-verification-client && ..\mvnw spring-boot:run"
timeout /t 5

start "Auction REST" cmd /c "cd auction-rest && ..\mvnw spring-boot:run"
timeout /t 5

start "Scheduler Service" cmd /c "cd auction-scheduler-service && ..\mvnw spring-boot:run"

echo All services started in separate windows.
pause