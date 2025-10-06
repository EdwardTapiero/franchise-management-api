## 🐳 Despliegue con Docker

### Prerrequisitos
- Docker 20.10+
- Docker Compose 2.0+

### Construcción y Ejecución

#### Opción 1: Docker Compose (Recomendado)
```bash
# Construir y ejecutar todos los servicios
docker-compose up --build -d

# Ver logs
docker-compose logs -f franchise-api

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v