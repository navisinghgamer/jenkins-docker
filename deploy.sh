#!/bin/bash

set -e
CONTAINER_NAME="fastapi-app"
IMAGE_NAME="fastapi-local"

echo "🛑 Stopping old container..."
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

echo "🚀 Running new container..."
docker run -d --name $CONTAINER_NAME -p 8000:8000 $IMAGE_NAME

echo "✅ App running at: http://localhost:8000/"