#!/bin/bash

echo "🚀 Starting services..."
docker-compose up -d

echo "⏳ Waiting for services to be healthy..."
sleep 10

echo "✅ Services started!"
echo "📊 API: http://localhost:8080"
echo "🗄️  MongoDB: localhost:27017"

echo ""
echo "📋 View logs with: docker-compose logs -f franchise-api"