# 🍔 Franchise Management API

API Reactiva para gestión de franquicias, sucursales y productos desarrollada con Spring Boot WebFlux y arquitectura hexagonal.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Coverage](https://img.shields.io/badge/Coverage-85%25-brightgreen.svg)](./target/site/jacoco/index.html)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución](#-ejecución)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Docker](#-docker)
- [Decisiones de Diseño](#-decisiones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)

---

## ✨ Características

### Funcionalidades Core
- ✅ Gestión completa de franquicias (CRUD)
- ✅ Gestión de sucursales por franquicia
- ✅ Gestión de productos por sucursal
- ✅ Control de inventario (stock de productos)
- ✅ Consulta de productos con mayor stock por sucursal

### Características Técnicas
- ⚡ **API Reactiva** con Spring WebFlux
- 🏗️ **Arquitectura Hexagonal** (Ports & Adapters)
- 🗄️ **MongoDB Reactivo** para persistencia
- ✅ **Validación** de datos con Bean Validation
- 🔒 **Manejo de errores** centralizado
- 📊 **Logging** estructurado en todas las capas
- 🧪 **85% de cobertura** de tests
- 🐳 **Dockerizado** y listo para producción

---

## 🏗️ Arquitectura

El proyecto implementa **Arquitectura Hexagonal** (Clean Architecture), separando la lógica de negocio de los detalles de implementación.
┌─────────────────────────────────────────────────────────────┐
│                     Infrastructure Layer                     │
│  ┌──────────────────────┐      ┌──────────────────────┐    │
│  │   REST Controllers   │      │  MongoDB Adapters    │    │
│  │   (Entry Points)     │      │  (Repositories)      │    │
│  └──────────┬───────────┘      └──────────┬───────────┘    │
│             │                              │                 │
│             ▼                              ▼                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Application Layer (Services)            │   │
│  │           Orchestrates business logic                │   │
│  └─────────────────────┬───────────────────────────────┘   │
│                        │                                     │
│                        ▼                                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Domain Layer (Core)                     │   │
│  │  ┌─────────────┐  ┌──────────────┐  ┌────────────┐ │   │
│  │  │   Models    │  │    Ports     │  │ Exceptions │ │   │
│  │  │ (Entities)  │  │ (Interfaces) │  │  (Domain)  │ │   │
│  │  └─────────────┘  └──────────────┘  └────────────┘ │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘

### Flujo de Dependencias
Infrastructure → Application → Domain
↓              ↓            ↑
Adapters      Use Cases    Business Rules

**Principio clave:** Las dependencias siempre apuntan hacia el dominio (Dependency Inversion Principle).

---

## 🛠️ Tecnologías

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| **Lenguaje** | Java | 17 |
| **Framework** | Spring Boot | 3.5.6 |
| **Programación Reactiva** | Project Reactor | 2024.0.1 |
| **Web** | Spring WebFlux | 6.2.2 |
| **Base de Datos** | MongoDB | 6.0 |
| **ORM** | Spring Data MongoDB Reactive | 4.4.2 |
| **Build Tool** | Maven | 3.9+ |
| **Testing** | JUnit 5 + Mockito + Reactor Test | - |
| **Code Coverage** | JaCoCo | 0.8.11 |
| **Containerization** | Docker + Docker Compose | - |
| **Logging** | SLF4J + Logback | - |
| **Validation** | Jakarta Bean Validation | 3.0 |

---

## 📋 Requisitos Previos

- **Java JDK 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- **MongoDB 6.0+** ([Download](https://www.mongodb.com/try/download/community)) o Docker
- **Docker & Docker Compose** (opcional, para contenedores) ([Download](https://www.docker.com/))

---

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/franchise-management-api.git
cd franchise-management-api
2. Configurar MongoDB
Opción A: MongoDB Local
bash# Iniciar MongoDB (si está instalado localmente)
mongod --dbpath /path/to/data/db
Opción B: MongoDB con Docker (Recomendado)
bashdocker run -d \
  --name franchise-mongodb \
  -p 27017:27017 \
  -v mongodb_data:/data/db \
  mongo:6.0
3. Configurar variables de entorno (Opcional)
bash# application.yml usa valores por defecto
# Para sobreescribir:
export MONGODB_URI=mongodb://localhost:27017/franchise_db
export PORT=8080

▶️ Ejecución
Desarrollo Local
bash# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# O ejecutar el JAR generado
java -jar target/franchise-management-api-0.0.1-SNAPSHOT.jar
La API estará disponible en: http://localhost:8080
Con Docker Compose (Recomendado)
bash# Construir y ejecutar todos los servicios
docker-compose up --build -d

# Ver logs
docker-compose logs -f franchise-api

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (limpieza completa)
docker-compose down -v
Verificar que está funcionando
bash# Health check
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}

📚 API Documentation
Base URL
http://localhost:8080/api/v1
Endpoints
Franquicias
MétodoEndpointDescripciónPOST/franchisesCrear nueva franquiciaGET/franchisesListar todas las franquiciasGET/franchises/{id}Obtener franquicia por IDPATCH/franchises/{id}/nameActualizar nombre de franquiciaDELETE/franchises/{id}Eliminar franquicia
Sucursales
MétodoEndpointDescripciónPOST/franchises/{franchiseId}/branchesAgregar sucursal a franquiciaGET/branches/{id}Obtener sucursal por IDGET/franchises/{franchiseId}/branchesListar sucursales de franquiciaPATCH/branches/{id}/nameActualizar nombre de sucursalDELETE/branches/{id}Eliminar sucursal
Productos
MétodoEndpointDescripciónPOST/branches/{branchId}/productsAgregar producto a sucursalGET/products/{id}Obtener producto por IDGET/branches/{branchId}/productsListar productos de sucursalPATCH/products/{id}/stockModificar stock de productoPATCH/products/{id}/nameActualizar nombre de productoDELETE/products/{id}Eliminar producto
Consultas Especiales
MétodoEndpointDescripciónGET/franchises/{franchiseId}/max-stock-productsProducto con más stock por sucursal

Ejemplos de Uso
1. Crear una Franquicia
Request:
bashcurl -X POST http://localhost:8080/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{
    "name": "McDonalds"
  }'
Response:
json{
  "id": "67890abc-def1-2345-6789-0abcdef12345",
  "name": "McDonalds"
}
2. Agregar Sucursal a Franquicia
Request:
bashcurl -X POST http://localhost:8080/api/v1/franchises/67890abc-def1-2345-6789-0abcdef12345/branches \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sucursal Centro"
  }'
Response:
json{
  "id": "branch-uuid",
  "name": "Sucursal Centro",
  "franchiseId": "67890abc-def1-2345-6789-0abcdef12345"
}
3. Agregar Producto a Sucursal
Request:
bashcurl -X POST http://localhost:8080/api/v1/branches/branch-uuid/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Big Mac",
    "stock": 50
  }'
Response:
json{
  "id": "product-uuid",
  "name": "Big Mac",
  "stock": 50,
  "branchId": "branch-uuid"
}
4. Modificar Stock de Producto
Request:
bashcurl -X PATCH http://localhost:8080/api/v1/products/product-uuid/stock \
  -H "Content-Type: application/json" \
  -d '{
    "stock": 75
  }'
Response:
json{
  "id": "product-uuid",
  "name": "Big Mac",
  "stock": 75,
  "branchId": "branch-uuid"
}
5. Obtener Producto con Mayor Stock por Sucursal
Request:
bashcurl http://localhost:8080/api/v1/franchises/67890abc-def1-2345-6789-0abcdef12345/max-stock-products
Response:
json[
  {
    "productId": "product-uuid-1",
    "productName": "Big Mac",
    "stock": 100,
    "branchId": "branch-uuid-1",
    "branchName": "Sucursal Centro"
  },
  {
    "productId": "product-uuid-2",
    "productName": "Papas Fritas",
    "stock": 150,
    "branchId": "branch-uuid-2",
    "branchName": "Sucursal Norte"
  }
]
6. Manejo de Errores
Request (Franquicia no existe):
bashcurl http://localhost:8080/api/v1/franchises/non-existent-id
Response (404 Not Found):
json{
  "error": "Franchise Not Found",
  "message": "Franchise with id 'non-existent-id' not found",
  "status": 404,
  "timestamp": "2025-10-05T23:30:00.123456",
  "path": "/api/v1/franchises/non-existent-id"
}

🧪 Testing
Ejecutar Tests
bash# Ejecutar todos los tests
mvn test

# Ejecutar tests con reporte de cobertura
mvn clean test jacoco:report

# Abrir reporte de cobertura en navegador
open target/site/jacoco/index.html  # macOS
xdg-open target/site/jacoco/index.html  # Linux
start target/site/jacoco/index.html  # Windows
Cobertura de Tests
┌─────────────────────────────────────────────┐
│  Code Coverage Report                       │
├─────────────────────────────────────────────┤
│  Total Coverage:           85%              │
│  Domain Layer:            100%              │
│  Application Services:    100%              │
│  Infrastructure:           81%              │
│  REST Controllers:         92%              │
└─────────────────────────────────────────────┘
Tipos de Tests

✅ Unit Tests: Lógica de negocio (Domain)
✅ Service Tests: Casos de uso (Application)
✅ Controller Tests: Endpoints REST (Infrastructure)
✅ Integration Tests: Flujos reactivos completos
✅ Mapper Tests: Conversiones DTO ↔ Domain ↔ Entity


🐳 Docker
Imágenes

API: Multi-stage build con Maven y Eclipse Temurin JRE 17
MongoDB: Imagen oficial mongo:6.0

docker-compose.yml
yamlservices:
  mongodb:
    image: mongo:6.0
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db
    healthcheck:
      test: mongosh --eval "db.adminCommand('ping')"
      interval: 10s
      timeout: 5s
      retries: 5

  franchise-api:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      mongodb:
        condition: service_healthy
    environment:
      - MONGODB_URI=mongodb://mongodb:27017/franchise_db
      - SPRING_PROFILES_ACTIVE=prod
Comandos Docker
bash# Construir imagen
docker build -t franchise-api:latest .

# Ejecutar solo la API (MongoDB aparte)
docker run -d \
  -p 8080:8080 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017/franchise_db \
  franchise-api:latest

# Ver logs
docker logs -f franchise-api

# Detener contenedor
docker stop franchise-api
Optimizaciones Docker

✅ Multi-stage build (reduce tamaño de imagen)
✅ Caché de dependencias Maven
✅ Usuario no-root para seguridad
✅ Health checks configurados
✅ Variables de entorno parametrizadas


💡 Decisiones de Diseño
1. Arquitectura Hexagonal
¿Por qué?

✅ Separación clara de responsabilidades
✅ Testabilidad (fácil mockear dependencias)
✅ Independencia de frameworks
✅ Facilita el mantenimiento y escalabilidad

Capas:

Domain: Lógica de negocio pura (sin dependencias externas)
Application: Casos de uso (orquestación)
Infrastructure: Detalles técnicos (DB, REST, etc.)

2. Programación Reactiva con WebFlux
¿Por qué?

✅ No bloqueante (mejor uso de recursos)
✅ Escalabilidad horizontal
✅ Backpressure nativo
✅ Ideal para alta concurrencia

Operadores utilizados:

flatMap: Transformaciones asíncronas
map: Transformaciones síncronas
switchIfEmpty: Manejo de casos vacíos
zip: Combinación de flujos
doOnSuccess/doOnError: Logging y side-effects

3. MongoDB con Colecciones Separadas
¿Por qué no documentos anidados?

✅ Mejor para demostrar operadores reactivos
✅ Actualizaciones más eficientes
✅ Queries más flexibles
✅ Escalabilidad (documentos no crecen indefinidamente)

Modelo:
franchises → branches → products
    ↓           ↓          ↓
   _id      franchiseId  branchId
4. PATCH vs PUT para Actualizaciones
¿Por qué PATCH?

✅ Semánticamente correcto para actualizaciones parciales
✅ Menos verbose (solo el campo a cambiar)
✅ Sigue RFC 5789
✅ Endpoints específicos (/products/{id}/stock, /products/{id}/name)

5. Validación con Bean Validation
¿Por qué?

✅ Validación declarativa
✅ Reutilizable
✅ Mensajes de error claros
✅ Estándar de Jakarta EE

Ejemplo:
java@NotBlank(message = "Name is required")
@Size(min = 2, max = 100)
private String name;
6. Logging Estructurado
Niveles:

DEBUG: Detalles técnicos (desarrollo)
INFO: Operaciones exitosas
WARN: Situaciones anormales recuperables
ERROR: Errores que requieren atención

Ubicación:

Controllers: Entrada/salida de requests
Services: Lógica de negocio
Adapters: Operaciones de persistencia

7. Manejo de Errores Centralizado
GlobalExceptionHandler:

✅ Un solo lugar para manejar errores
✅ Respuestas consistentes
✅ Logging automático
✅ Códigos HTTP apropiados

Excepciones del Dominio:
DomainException (abstract)
├── FranchiseNotFoundException
├── BranchNotFoundException
└── ProductNotFoundException

📁 Estructura del Proyecto
franchise-management-api/
├── src/
│   ├── main/
│   │   ├── java/co/com/nequi/api/franchise_management_api/
│   │   │   ├── FranchiseManagementApiApplication.java
│   │   │   ├── domain/                      # Capa de Dominio
│   │   │   │   ├── model/                   # Entidades
│   │   │   │   │   ├── Franchise.java
│   │   │   │   │   ├── Branch.java
│   │   │   │   │   └── Product.java
│   │   │   │   ├── port/                    # Puertos (Interfaces)
│   │   │   │   │   ├── in/                  # Casos de uso
│   │   │   │   │   └── out/                 # Repositorios
│   │   │   │   └── exception/               # Excepciones
│   │   │   ├── application/                 # Capa de Aplicación
│   │   │   │   └── service/                 # Implementación casos de uso
│   │   │   │       ├── FranchiseService.java
│   │   │   │       ├── BranchService.java
│   │   │   │       └── ProductService.java
│   │   │   └── infrastructure/              # Capa de Infraestructura
│   │   │       ├── adapter/
│   │   │       │   ├── in/web/              # REST Controllers
│   │   │       │   │   ├── controller/
│   │   │       │   │   ├── dto/
│   │   │       │   │   ├── mapper/
│   │   │       │   │   └── handler/
│   │   │       │   └── out/persistence/     # MongoDB Adapters
│   │   │       │       ├── adapter/
│   │   │       │       ├── entity/
│   │   │       │       ├── mapper/
│   │   │       │       └── repository/
│   │   │       └── config/                  # Configuraciones
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── logback-spring.xml
│   └── test/                                # Tests unitarios
│       └── java/co/com/nequi/api/...
├── docker-compose.yml
├── Dockerfile
├── .dockerignore
├── .gitignore
├── pom.xml
└── README.md

🎓 Patrones y Principios Aplicados
Patrones de Diseño

✅ Repository Pattern: Abstracción de persistencia
✅ Adapter Pattern: Integración con servicios externos
✅ Builder Pattern: Construcción de objetos (Lombok)
✅ Strategy Pattern: Diferentes implementaciones de puertos
✅ Facade Pattern: Simplificación de subsistemas complejos

Principios SOLID

✅ Single Responsibility: Cada clase una responsabilidad
✅ Open/Closed: Abierto a extensión, cerrado a modificación
✅ Liskov Substitution: Interfaces intercambiables
✅ Interface Segregation: Interfaces específicas
✅ Dependency Inversion: Depender de abstracciones

Clean Code

✅ Nombres descriptivos
✅ Funciones pequeñas y focalizadas
✅ Comentarios solo cuando agregan valor
✅ Manejo consistente de errores
✅ Tests como documentación


🔐 Seguridad

✅ Usuario no-root en Docker
✅ Validación de entrada en todos los endpoints
✅ Sanitización de datos
✅ Manejo seguro de excepciones

📈 Performance
Optimizaciones Implementadas

✅ Programación reactiva
✅ Índices en MongoDB (franchiseId, branchId)
✅ Caché de dependencias en Docker
✅ Healthchecks para orquestación
✅ Logs asíncronos

Métricas (Actuator)
bash# Ver métricas
curl http://localhost:8080/actuator/metrics

# Ver health
curl http://localhost:8080/actuator/health

🤝 Contribución
Este es un proyecto de prueba técnica. Para contribuir:

Fork el repositorio
Crea una rama feature (git checkout -b feature/AmazingFeature)
Commit tus cambios (git commit -m 'Add some AmazingFeature')
Push a la rama (git push origin feature/AmazingFeature)
Abre un Pull Request

📖 API Documentation

La documentación interactiva de la API está disponible en:
http://localhost:8080/swagger-ui/index.html


📝 Licencia
Este proyecto es una prueba técnica y está disponible para fines educativos.

👤 Autor
Edward Tapiero

GitHub: @EdwardTapiero
LinkedIn: https://www.linkedin.com/in/edward-tapiero-88617019b/


🙏 Agradecimientos

Spring Boot Team por el excelente framework
MongoDB por la base de datos reactiva
Project Reactor por la programación reactiva
Clean Architecture principles por Robert C. Martin


Desarrollado con ❤️ usando Spring Boot WebFlux y Arquitectura Hexagonal
