# SafeZone monorepo

Este repositorio está organizado en dos paquetes principales:

- `backend/` – aplicación Spring Boot con todo el código Java, el `pom.xml`, 
  scripts Maven (`mvnw`) y un `Dockerfile` para construir el servicio.
- `frontend/` – contenido estático o proyecto SPA (React/Vue/etc.) servido por
  nginx, con su propio `Dockerfile`.

En la raíz sólo encontrarás el `docker-compose.yml` que orquesta ambos servicios.

## Cómo usar
1. Entra en el directorio `backend` y construye el JAR:
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. Desde la raíz ejecuta:
   ```bash
   docker-compose up --build
   ```

El backend queda en `localhost:8080` y el frontend en `localhost:3000`.

## Mantener la estructura
- Evita mezclar archivos de backend dentro de `frontend/` y viceversa.
- Si necesitas agregar otros servicios (e.g. base de datos), extiende el
  `docker-compose.yml` de la raíz.

