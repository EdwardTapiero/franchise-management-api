#!/bin/bash

echo "🧹 Cleaning Docker resources..."
docker-compose down -v
docker rmi $(docker images -q 'franchise-management-api*') 2>/dev/null || true

echo "✅ Cleanup completed!"