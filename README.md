# 📡 Guía Completa: Cómo Conectar Network as Code API en Java

Esta guía te muestra **paso a paso** cómo integrar la API de Network as Code (CAMARA Project) en tu aplicación Java Spring Boot para usar la funcionalidad de **Quality on Demand (QoD)**.

---

## 📋 Tabla de Contenidos

1. [¿Qué es Network as Code?](#qué-es-network-as-code)
2. [Prerequisitos](#prerequisitos)
3. [Paso 1: Obtener tu API Key](#paso-1-obtener-tu-api-key)
4. [Paso 2: Configurar el Proyecto](#paso-2-configurar-el-proyecto)
5. [Paso 3: Estructura de Clases](#paso-3-estructura-de-clases)
6. [Paso 4: Código Completo](#paso-4-código-completo)
7. [Paso 5: Uso y Ejemplos](#paso-5-uso-y-ejemplos)
8. [Troubleshooting](#troubleshooting)

---

## 🌐 ¿Qué es Network as Code?

**Network as Code** es una iniciativa global (liderada por el proyecto CAMARA de la Linux Foundation) que expone las capacidades de las redes de telecomunicaciones a través de APIs fáciles de usar.

Con la API de **Quality on Demand (QoD)**, puedes:

- ✅ Solicitar **Calidad de Servicio (QoS)** mejorada para tus aplicaciones.
- ✅ Reducir la **latencia** y el *jitter*.
- ✅ Aumentar el **ancho de banda** de forma dinámica.
- ✅ Priorizar el tráfico de red para casos de uso sensibles como **gaming en la nube, streaming de video en alta definición, drones, o videollamadas críticas**.

---

## 📦 Prerequisitos

Antes de comenzar, asegúrate de tener:

- ✅ Java 17 o superior.
- ✅ Maven 3.6+ o Gradle.
- ✅ Una cuenta en el portal de desarrolladores de un operador que ofrezca la API (Telefónica, Orange, Vodafone, etc.).

---

## 🔑 Paso 1: Obtener tu API Key

Para usar la API, necesitas credenciales de autenticación. El proceso es similar en la mayoría de los operadores.

### Ejemplo con Telefónica Open Gateway

1.  **Visita el portal de desarrolladores:**
    -   Accede a [Telefónica Open Gateway](https://opengateway.telefonica.com/).

2.  **Regístrate:**
    -   Crea una cuenta de desarrollador. Es gratis y solo requiere un email.

3.  **Explora las APIs:**
    -   Navega a la sección "APIs" y busca **Quality on Demand (QoD)**.

4.  **Obtén tus credenciales:**
    -   En tu panel de control (dashboard), crea una nueva aplicación.
    -   Suscribe tu aplicación a la API de QoD.
    -   El portal te proporcionará un **API Key** (a veces llamado `token` o `client_id`/`client_secret`).
    -   **¡Copia y guarda esta clave!** La necesitarás en el siguiente paso.

### Otros Proveedores

-   **Orange Developer**: [https://developer.orange.com/](https://developer.orange.com/)
-   **Vodafone Business API**: [https://developer.vodafone.com/](https://developer.vodafone.com/)
-   **Deutsche Telekom (T-Mobile)**: [https://developer.telekom.com/](https://developer.telekom.com/)

---

## 🔧 Paso 2: Configurar el Proyecto

### 2.1. Dependencias (pom.xml)

Abre tu archivo `pom.xml` y añade las siguientes dependencias:

```xml
<dependencies>
    <!-- Spring Boot Starter Web: para crear controladores REST -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter WebFlux: para usar WebClient (cliente HTTP reactivo) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!-- Lombok: para reducir código boilerplate (getters, setters, constructores) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### 2.2. Configuración de la API Key (`application.properties`)

Abre el archivo `src/main/resources/application.properties` y añade las siguientes líneas:

```properties
# ===============================
# NETWORK AS CODE API CONFIG
# ===============================

# URL base de la API de QoD de tu proveedor
# Ejemplo para Telefónica Open Gateway Sandbox:
network.as.code.api.url=https://api.pre.opengateway.telefonica.com

# Pega aquí tu API Key obtenida en el Paso 1
network.as.code.token=TU_API_KEY_AQUI
```

**⚠️ ¡MUY IMPORTANTE!**
-   Reemplaza `TU_API_KEY_AQUI` con la clave que obtuviste.
-   **Nunca** subas este archivo a un repositorio público (como GitHub) con la clave real. Para producción, usa variables de entorno o un gestor de secretos (como HashiCorp Vault o AWS Secrets Manager).

---

## 📁 Paso 3: Estructura de Clases

Para mantener el código organizado, te recomendamos la siguiente estructura de paquetes y clases:

```
src/main/java/com/talenArena/SafeZone/
├── config/
│   └── NetworkAsCodeConfig.java      # Configuración del cliente HTTP (WebClient)
├── models/
│   ├── Device.java                   # Modelo de datos para un dispositivo
│   ├── DeviceIpv4Addr.java           # Modelo para la dirección IPv4
│   ├── QodSession.java               # Modelo para una sesión de QoD (respuesta de la API)
│   └── CreateQodSessionRequest.java  # DTO para crear una nueva sesión de QoD
├── service/
│   └── DeviceService.java            # Lógica de negocio para interactuar con la API
└── controller/
    └── NetworkAsCodeController.java  # Controlador REST para exponer la funcionalidad
```

---

## 💻 Paso 4: Código Completo

Aquí tienes el código completo para cada una de las clases.

### 4.1. `NetworkAsCodeConfig.java`

Esta clase crea un `WebClient` que se usará para todas las llamadas a la API. Automáticamente inyectará el token de autorización en cada petición.

**Ruta:** `src/main/java/com/talenArena/SafeZone/config/NetworkAsCodeConfig.java`

```java
package com.talenArena.SafeZone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NetworkAsCodeConfig {

    @Value("${network.as.code.api.url}")
    private String apiUrl;

    @Value("${network.as.code.token}")
    private String token;

    @Bean
    public WebClient networkAsCodeWebClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
```

### 4.2. Modelos de Datos (`models/`)

Estas clases representan los datos que se envían y reciben de la API.

**`DeviceIpv4Addr.java`**
```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceIpv4Addr {
    @JsonProperty("public_address")
    private String publicAddress;
}
```

**`Device.java`**

```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.talenArena.SafeZone.models.QoS.DeviceIpv4Addr;import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Device {
    @JsonProperty("ipv4_address")
    private DeviceIpv4Addr ipv4Address;
}
```

**`CreateQodSessionRequest.java`**

```java
package com.talenArena.SafeZone.models;

import com.talenArena.SafeZone.models.QoS.Device;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQodSessionRequest {
    private Device device;
    private String serviceIpv4;
    private String qosProfile;
    private Integer duration;
}
```

**`QodSession.java`**
```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QodSession {
    @JsonProperty("session_id")
    private String sessionId;
    private String qosProfile;
    private Integer duration;
    @JsonProperty("started_at")
    private LocalDateTime startedAt;
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
}
```

### 4.3. `DeviceService.java`

Este servicio contiene la lógica para llamar a los endpoints de la API de QoD (`crear`, `obtener`, `eliminar` sesión).

**Ruta:** `src/main/java/com/talenArena/SafeZone/service/DeviceService.java`

```java
package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.models.QoS.CreateQodSessionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final WebClient networkAsCodeWebClient;

    public QodSession createQodSession(com.talenArena.SafeZone.models.QoS.Device device, String serviceIpv4, String profile, Integer duration) {
        log.info("Creando sesión QoD para el dispositivo con perfil: {} y duración: {}", profile, duration);

        CreateQodSessionRequest request = CreateQodSessionRequest.builder()
                .device(device)
                .serviceIpv4(serviceIpv4)
                .qosProfile(profile)
                .duration(duration)
                .build();

        try {
            return networkAsCodeWebClient.post()
                    .uri("/qod/v0/sessions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al crear la sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear la sesión QoD", e);
        }
    }

    public QodSession getQodSession(String sessionId) {
        log.info("Obteniendo sesión QoD con ID: {}", sessionId);
        try {
            return networkAsCodeWebClient.get()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al obtener la sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo obtener la sesión QoD", e);
        }
    }

    public void deleteQodSession(String sessionId) {
        log.info("Eliminando sesión QoD con ID: {}", sessionId);
        try {
            networkAsCodeWebClient.delete()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            log.info("Sesión QoD eliminada exitosamente.");
        } catch (Exception e) {
            log.error("Error al eliminar la sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo eliminar la sesión QoD", e);
        }
    }
}
```

---

## ▶️ Paso 5: Uso y Ejemplos

Para usar el servicio, puedes inyectarlo en un controlador y crear un endpoint.

### `NetworkAsCodeController.java`

**Ruta:** `src/main/java/com/talenArena/SafeZone/controller/NetworkAsCodeController.java`

```java
package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.QoS.Device;
import com.talenArena.SafeZone.models.QoS.DeviceIpv4Addr;
import com.talenArena.SafeZone.models.QodSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qod")
@RequiredArgsConstructor
public class NetworkAsCodeController {

    private final DeviceService deviceService;

    @PostMapping("/sessions")
    public ResponseEntity<QodSession> createSession() {
        // --- 1. Identificar el dispositivo ---
        // La IP pública del dispositivo que usa la red móvil.
        // En un caso real, esta IP se obtendría dinámicamente.
        DeviceIpv4Addr deviceIp = DeviceIpv4Addr.builder()
                .publicAddress("212.145.128.21") // IP de ejemplo
                .build();

        Device device = Device.builder().ipv4Address(deviceIp).build();

        // --- 2. Definir los parámetros de la sesión ---
        String serviceIpv4 = "54.1.1.1"; // IP del servidor de tu aplicación/juego
        String qosProfile = "QOS_E"; // Perfil de alta calidad (baja latencia)
        Integer duration = 3600; // 1 hora

        // --- 3. Llamar al servicio para crear la sesión ---
        QodSession session = deviceService.createQodSession(device, serviceIpv4, qosProfile, duration);

        return ResponseEntity.ok(session);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<QodSession> getSession(@PathVariable String sessionId) {
        QodSession session = deviceService.getQodSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        deviceService.deleteQodSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
```

### Probar la API

1.  Ejecuta tu aplicación Spring Boot.
2.  Usa una herramienta como `curl` o Postman para llamar a tu endpoint:

**Crear una sesión:**
```bash
curl -X POST http://localhost:8080/api/qod/sessions
```

Esto devolverá una respuesta JSON con los detalles de la sesión creada, incluyendo su `session_id`.

---

## 🚨 Troubleshooting

-   **Error 401 Unauthorized**:
    -   Verifica que tu `network.as.code.token` en `application.properties` es correcto.
    -   Asegúrate de que el prefijo `Bearer ` esté presente en la cabecera `Authorization`. El `WebClient` configurado ya lo hace por ti.
-   **Error 400 Bad Request**:
    -   Revisa los datos que estás enviando. La IP del dispositivo, el perfil de QoS o la duración pueden ser inválidos.
    -   Consulta la documentación de la API de tu proveedor para ver los valores permitidos (ej. perfiles de QoS disponibles).
-   **No se puede conectar a `network.as.code.api.url`**:
    -   Verifica que la URL en `application.properties` es correcta y que no hay un firewall bloqueando la conexión.
-   **`NullPointerException` en `DeviceService`**:
    -   Asegúrate de que el bean `networkAsCodeWebClient` se está creando correctamente en `NetworkAsCodeConfig`. Revisa que todas las clases tengan las anotaciones `@Configuration`, `@Service`, etc.

¡Y eso es todo! Con estos pasos, tienes una base sólida para integrar la API de Network as Code en tus aplicaciones Java.

