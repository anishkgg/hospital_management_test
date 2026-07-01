.PHONY: start stop restart rebuild clean status logs

# Start the application in the background (detached mode)
start:
	docker compose up --build -d
	@echo "=========================================================="
	@echo "Hospital Management Platform is booting up!"
	@echo "Once complete, open http://localhost:8080 in your browser."
	@echo "=========================================================="

# Stop the running application containers
stop:
	docker compose down
	@echo "Application stopped successfully."

# Restart the application
restart: stop start

# Rebuild the Docker containers from scratch
rebuild:
	docker compose build --no-cache

# Stop containers and delete all persistent volumes (clears database)
clean:
	docker compose down -v
	@echo "Application stopped and database volumes cleared."

# View container status
status:
	docker compose ps

# View container console logs
logs:
	docker compose logs -f
