# 📡 Guía Completa: Cómo Conectar Network as Code API en Java

Esta guía te muestra **paso a paso** cómo integrar la API de Network as Code (CAMARA Project) en tu aplicación Java Spring Boot.

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

**Network as Code** es una API estándar del proyecto CAMARA que permite a los desarrolladores controlar características de red móvil directamente desde su código. Con esta API puedes:

- ✅ Solicitar **Quality of Service (QoS)** garantizado
- ✅ Reducir **latencia** para tus aplicaciones
- ✅ Aumentar **ancho de banda** cuando lo necesitas
- ✅ Priorizar tráfico para **gaming, streaming, videollamadas**, etc.

---

## 📦 Prerequisitos

Antes de comenzar, asegúrate de tener:

- ✅ Java 17 o superior
- ✅ Maven 3.6+
- ✅ Spring Boot 3.x o 4.x
- ✅ Una cuenta en un proveedor de Network as Code (Telefónica, Vodafone, etc.)

---

## 🔑 Paso 1: Obtener tu API Key

### Opción A: Usando Nokia Network as Code (Developer Sandbox)

1. **Visita el portal de desarrolladores:**
   - Ve a: https://developer.networkascode.nokia.com/
   
2. **Crear una cuenta:**
   - Haz clic en "Sign Up" o "Register"
   - Completa el formulario con tus datos
   - Verifica tu email

3. **Crear una aplicación:**
   - Una vez logueado, ve a "My Apps" o "Applications"
   - Haz clic en "Create New Application"
   - Rellena los datos:
     - **Name**: SafeZone (o el nombre de tu app)
     - **Description**: Mi aplicación con QoS
     - **Redirect URL**: http://localhost:8080 (para desarrollo)

4. **Obtener las credenciales:**
   - Después de crear la app, verás tu **Application Key** o **API Key**
   - También obtendrás un **Client ID** y **Client Secret**
   - **¡GUÁRDALOS!** Los necesitarás en el siguiente paso

5. **Ejemplo de cómo se ve:**
   ```
   Application Key: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   Client ID: a1b2c3d4-e5f6-7890-abcd-ef1234567890
   Client Secret: s3cr3t_k3y_h3r3
   ```

### Opción B: Usando Telefónica Open Gateway

1. **Visita el portal:**
   - Ve a: https://opengateway.telefonica.com/
   
2. **Registrarse:**
   - Haz clic en "Developers" → "Get Started"
   - Crea tu cuenta de desarrollador

3. **Suscribirse a la API QoD:**
   - Navega a "APIs" → "Quality on Demand"
   - Haz clic en "Subscribe"
   - Completa el formulario de solicitud

4. **Obtener credenciales:**
   - Ve a "My Applications" → "Create App"
   - Suscribe tu app a la API "QoD"
   - Copia tu **API Key** y **Secret**

### Opción C: Otros Proveedores

- **Vodafone**: https://developer.vodafone.com/
- **Orange**: https://developer.orange.com/
- **Deutsche Telekom**: https://developer.telekom.com/

Cada proveedor tiene un proceso similar: registrarte, crear una app, y obtener credenciales.

---

## 🔧 Paso 2: Configurar el Proyecto

### 2.1 Agregar Dependencias Maven

Abre tu archivo `pom.xml` y asegúrate de tener estas dependencias:

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>
    
    <!-- Spring Boot WebFlux (para WebClient) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    <!-- Jackson para JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    
    <!-- Lombok (opcional, pero recomendado) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.42</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### 2.2 Configurar application.properties

Abre `src/main/resources/application.properties` y agrega:

```properties
# ===============================
# NETWORK AS CODE API
# ===============================

# URL de la API (cambia según tu proveedor)
# Nokia:       https://network-as-code.nokia.com
# Telefónica:  https://opengateway.telefonica.com
# Vodafone:    https://api.vodafone.com/network-as-code
network.as.code.api.url=https://network-as-code.example.com

# Tu API Key (pégala aquí SIN comillas)
network.as.code.token=TU_API_KEY_AQUI

# Ejemplo real:
# network.as.code.token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.abcd1234
```

**⚠️ IMPORTANTE:** 
- Reemplaza `TU_API_KEY_AQUI` con tu API Key real del Paso 1
- NO compartas tu API Key públicamente
- NO hagas commit de este archivo con tu API Key real (usa variables de entorno en producción)

### 2.3 Instalar Dependencias

Ejecuta en tu terminal:

```bash
./mvnw clean install
```

---

## 📁 Paso 3: Estructura de Clases

Tu proyecto debe tener esta estructura de packages y clases:

```
src/main/java/com/talenArena/SafeZone/
├── config/
│   └── NetworkAsCodeConfig.java          # Configuración de WebClient
├── models/
│   ├── Device.java                       # Modelo del dispositivo
│   ├── DeviceIpv4Addr.java              # Dirección IPv4
│   ├── QodSession.java                   # Sesión QoS
│   ├── QosProfile.java                   # Perfil QoS
│   └── CreateQodSessionRequest.java      # Request DTO
├── service/
│   ├── NetworkAsCodeClient.java          # Cliente principal
│   └── DeviceService.java                # Servicio de dispositivos
└── controller/
    └── NetworkAsCodeController.java      # REST API endpoints
```

---

## 💻 Paso 4: Código Completo

### 4.1 Clase de Configuración

**Archivo:** `src/main/java/com/talenArena/SafeZone/config/NetworkAsCodeConfig.java`

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

**¿Qué hace?**
- Lee la URL y el token de `application.properties`
- Crea un `WebClient` configurado con autenticación automática
- Agrega el header `Authorization: Bearer TU_TOKEN` a todas las peticiones

---

### 4.2 Modelos de Datos

#### DeviceIpv4Addr.java

**Archivo:** `src/main/java/com/talenArena/SafeZone/models/DeviceIpv4Addr.java`

```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIpv4Addr {

    @JsonProperty("public_address")
    private String publicAddress;     // IP pública del dispositivo

    @JsonProperty("private_address")
    private String privateAddress;    // IP privada (NAT)

    @JsonProperty("public_port")
    private Integer publicPort;       // Puerto público
}
```

#### Device.java

**Archivo:** `src/main/java/com/talenArena/SafeZone/models/Device.java`

```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @JsonProperty("ipv4_address")
    private DeviceIpv4Addr ipv4Address;   // Dirección IPv4

    @JsonProperty("ipv6_address")
    private String ipv6Address;            // Dirección IPv6 (opcional)

    @JsonProperty("phone_number")
    private String phoneNumber;            // Número de teléfono (opcional)

    @JsonProperty("network_access_identifier")
    private String networkAccessIdentifier; // NAI (opcional)
}
```

#### QodSession.java

**Archivo:** `src/main/java/com/talenArena/SafeZone/models/QodSession.java`

```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QodSession {

    @JsonProperty("session_id")
    private String sessionId;          // ID único de la sesión

    @JsonProperty("device")
    private Device device;             // Dispositivo

    @JsonProperty("service_ipv4")
    private String serviceIpv4;        // IP del servicio destino

    @JsonProperty("service_ipv6")
    private String serviceIpv6;        // IPv6 del servicio destino

    @JsonProperty("qos_profile")
    private String qosProfile;         // Perfil QoS (ej: DOWNLINK_L_UPLINK_L)

    @JsonProperty("duration")
    private Integer duration;          // Duración en segundos

    @JsonProperty("started_at")
    private LocalDateTime startedAt;   // Cuándo comenzó

    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;   // Cuándo expira

    @JsonProperty("status")
    private String status;             // Estado: ACTIVE, EXPIRED, etc.
}
```

#### CreateQodSessionRequest.java

**Archivo:** `src/main/java/com/talenArena/SafeZone/models/CreateQodSessionRequest.java`

```java
package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQodSessionRequest {

    @JsonProperty("device")
    private Device device;

    @JsonProperty("service_ipv4")
    private String serviceIpv4;

    @JsonProperty("service_ipv6")
    private String serviceIpv6;

    @JsonProperty("qos_profile")
    private String qosProfile;

    @JsonProperty("duration")
    private Integer duration;
}
```

---

### 4.3 Cliente de Network as Code

**Archivo:** `src/main/java/com/talenArena/SafeZone/service/NetworkAsCodeClient.java`

```java
package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.DeviceIpv4Addr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkAsCodeClient {

    @Value("${network.as.code.token:}")
    private String token;

    /**
     * Obtiene un dispositivo basado en sus identificadores.
     * Equivalente a: client.devices.get() en Python
     *
     * @param ipv4Address Dirección IPv4 del dispositivo
     * @param ipv6Address Dirección IPv6 del dispositivo (opcional)
     * @param phoneNumber Número de teléfono del dispositivo (opcional)
     * @return Device objeto que representa el dispositivo
     */
    public Device getDevice(DeviceIpv4Addr ipv4Address, String ipv6Address, String phoneNumber) {
        log.info("Obteniendo dispositivo con IPv4: {}, IPv6: {}, Teléfono: {}",
                ipv4Address, ipv6Address, phoneNumber);

        return Device.builder()
                .ipv4Address(ipv4Address)
                .ipv6Address(ipv6Address)
                .phoneNumber(phoneNumber)
                .build();
    }

    /**
     * Valida el token de autenticación
     *
     * @return true si el token está configurado
     */
    public boolean isTokenConfigured() {
        return token != null && !token.isEmpty();
    }
}
```

---

### 4.4 Servicio de Dispositivos

**Archivo:** `src/main/java/com/talenArena/SafeZone/service/DeviceService.java`

```java
package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.models.CreateQodSessionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final WebClient networkAsCodeWebClient;

    /**
     * Crea una sesión de Quality on Demand (QoD) para un dispositivo.
     * Equivalente a: my_device.create_qod_session() en Python
     *
     * @param device Dispositivo para el cual crear la sesión
     * @param serviceIpv4 Dirección IPv4 del servicio destino
     * @param serviceIpv6 Dirección IPv6 del servicio destino (opcional)
     * @param profile Perfil de QoS (ej: "DOWNLINK_L_UPLINK_L")
     * @param duration Duración de la sesión en segundos
     * @return QodSession objeto que representa la sesión creada
     */
    public QodSession createQodSession(Device device, String serviceIpv4, String serviceIpv6,
                                       String profile, Integer duration) {
        log.info("Creando sesión QoD para dispositivo con perfil: {} y duración: {} segundos",
                profile, duration);

        CreateQodSessionRequest request = CreateQodSessionRequest.builder()
                .device(device)
                .serviceIpv4(serviceIpv4)
                .serviceIpv6(serviceIpv6)
                .qosProfile(profile)
                .duration(duration)
                .build();

        try {
            QodSession session = networkAsCodeWebClient.post()
                    .uri("/qod/v0/sessions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();

            log.info("Sesión QoD creada exitosamente con ID: {}", session.getSessionId());
            return session;

        } catch (Exception e) {
            log.error("Error al crear sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo crear la sesión QoD", e);
        }
    }

    /**
     * Obtiene una sesión QoD existente por su ID
     */
    public QodSession getQodSession(String sessionId) {
        log.info("Obteniendo sesión QoD con ID: {}", sessionId);

        try {
            QodSession session = networkAsCodeWebClient.get()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .bodyToMono(QodSession.class)
                    .block();

            log.info("Sesión QoD obtenida exitosamente");
            return session;

        } catch (Exception e) {
            log.error("Error al obtener sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo obtener la sesión QoD", e);
        }
    }

    /**
     * Elimina/cancela una sesión QoD
     */
    public void deleteQodSession(String sessionId) {
        log.info("Eliminando sesión QoD con ID: {}", sessionId);

        try {
            networkAsCodeWebClient.delete()
                    .uri("/qod/v0/sessions/{sessionId}", sessionId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info("Sesión QoD eliminada exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar sesión QoD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo eliminar la sesión QoD", e);
        }
    }
}
```

---

### 4.5 Controlador REST (opcional)

**Archivo:** `src/main/java/com/talenArena/SafeZone/controller/NetworkAsCodeController.java`

```java
package com.talenArena.SafeZone.controller;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.DeviceIpv4Addr;
import com.talenArena.SafeZone.models.QodSession;
import com.talenArena.SafeZone.service.DeviceService;
import com.talenArena.SafeZone.service.NetworkAsCodeClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
public class NetworkAsCodeController {

    private final NetworkAsCodeClient networkAsCodeClient;
    private final DeviceService deviceService;

    /**
     * Crea una sesión QoD mediante REST API
     * POST /api/network/qod-session
     */
    @PostMapping("/qod-session")
    public ResponseEntity<QodSession> createQodSession(@RequestBody CreateQodSessionDto request) {
        log.info("Recibida petición para crear sesión QoD con perfil: {}", request.getProfile());

        // Crear el objeto Device
        DeviceIpv4Addr ipv4Addr = DeviceIpv4Addr.builder()
                .publicAddress(request.getPublicAddress())
                .privateAddress(request.getPrivateAddress())
                .publicPort(request.getPublicPort())
                .build();

        Device device = networkAsCodeClient.getDevice(
                ipv4Addr,
                request.getIpv6Address(),
                request.getPhoneNumber()
        );

        // Crear la sesión QoD
        QodSession session = deviceService.createQodSession(
                device,
                request.getServiceIpv4(),
                request.getServiceIpv6(),
                request.getProfile(),
                request.getDuration()
        );

        return ResponseEntity.ok(session);
    }

    /**
     * Obtiene una sesión QoD existente
     * GET /api/network/qod-session/{sessionId}
     */
    @GetMapping("/qod-session/{sessionId}")
    public ResponseEntity<QodSession> getQodSession(@PathVariable String sessionId) {
        QodSession session = deviceService.getQodSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Elimina una sesión QoD
     * DELETE /api/network/qod-session/{sessionId}
     */
    @DeleteMapping("/qod-session/{sessionId}")
    public ResponseEntity<Void> deleteQodSession(@PathVariable String sessionId) {
        deviceService.deleteQodSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    // DTO para la petición
    @Data
    public static class CreateQodSessionDto {
        private String publicAddress;
        private String privateAddress;
        private Integer publicPort;
        private String ipv6Address;
        private String phoneNumber;
        private String serviceIpv4;
        private String serviceIpv6;
        private String profile;
        private Integer duration;
    }
}
```

---

## 🚀 Paso 5: Uso y Ejemplos

### 5.1 Ejemplo Básico en Java

Código equivalente al ejemplo de Python de Network as Code:

**Python original:**
```python
import network_as_code as nac
from network_as_code.models.device import DeviceIpv4Addr

client = nac.NetworkAsCodeClient(token="<your-application-key-here>")

my_device = client.devices.get(
    ipv4_address=DeviceIpv4Addr(
        public_address="233.252.0.2",
        private_address="192.0.2.25",
        public_port=80
    ),
    ipv6_address="2001:db8:1234:5678:9abc:def0:fedc:ba98",
    phone_number="+999991234567"
)

my_session = my_device.create_qod_session(
    service_ipv4="233.252.0.2",
    service_ipv6="2001:db8:1234:5678:9abc:def0:fedc:ba98",
    profile="DOWNLINK_L_UPLINK_L",
    duration=3600
)
```

**Java equivalente:**
```java
@Service
public class MyService {

    @Autowired
    private NetworkAsCodeClient client;
    
    @Autowired
    private DeviceService deviceService;

    public void createQodSession() {
        // 1. Crear DeviceIpv4Addr
        DeviceIpv4Addr ipv4Address = DeviceIpv4Addr.builder()
                .publicAddress("233.252.0.2")
                .privateAddress("192.0.2.25")
                .publicPort(80)
                .build();
        
        // 2. Obtener el dispositivo
        Device myDevice = client.getDevice(
                ipv4Address,
                "2001:db8:1234:5678:9abc:def0:fedc:ba98",
                "+999991234567"
        );
        
        // 3. Crear la sesión QoD
        QodSession mySession = deviceService.createQodSession(
                myDevice,
                "233.252.0.2",                           // service_ipv4
                "2001:db8:1234:5678:9abc:def0:fedc:ba98", // service_ipv6
                "DOWNLINK_L_UPLINK_L",                   // profile
                3600                                      // duration (1 hora)
        );
        
        System.out.println("Session ID: " + mySession.getSessionId());
        System.out.println("Status: " + mySession.getStatus());
    }
}
```

---

### 5.2 Usar desde REST API

#### Crear una sesión QoD

**Request:**
```bash
curl -X POST http://localhost:8080/api/network/qod-session \
  -H "Content-Type: application/json" \
  -d '{
    "publicAddress": "233.252.0.2",
    "privateAddress": "192.0.2.25",
    "publicPort": 80,
    "ipv6Address": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
    "phoneNumber": "+999991234567",
    "serviceIpv4": "233.252.0.2",
    "serviceIpv6": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
    "profile": "DOWNLINK_L_UPLINK_L",
    "duration": 3600
  }'
```

**Response:**
```json
{
  "session_id": "123e4567-e89b-12d3-a456-426614174000",
  "device": {
    "ipv4_address": {
      "public_address": "233.252.0.2",
      "private_address": "192.0.2.25",
      "public_port": 80
    },
    "ipv6_address": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
    "phone_number": "+999991234567"
  },
  "service_ipv4": "233.252.0.2",
  "service_ipv6": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
  "qos_profile": "DOWNLINK_L_UPLINK_L",
  "duration": 3600,
  "started_at": "2026-03-01T10:00:00",
  "expires_at": "2026-03-01T11:00:00",
  "status": "ACTIVE"
}
```

#### Obtener una sesión existente

```bash
curl http://localhost:8080/api/network/qod-session/123e4567-e89b-12d3-a456-426614174000
```

#### Eliminar una sesión

```bash
curl -X DELETE http://localhost:8080/api/network/qod-session/123e4567-e89b-12d3-a456-426614174000
```

---

### 5.3 Perfiles QoS Disponibles

Los perfiles típicos son:

| Perfil | Descripción | Uso Recomendado |
|--------|-------------|-----------------|
| `DOWNLINK_L_UPLINK_L` | Baja latencia | Gaming online, videollamadas |
| `DOWNLINK_M_UPLINK_M` | Latencia media | Streaming de video, navegación |
| `DOWNLINK_H_UPLINK_H` | Alto rendimiento | Transferencias grandes, backups |
| `DOWNLINK_XL_UPLINK_XL` | Rendimiento extra | CDN, servidores |

**Nota:** Los perfiles exactos dependen de tu operador. Consulta su documentación.

---

## 🔍 Troubleshooting

### Problema 1: Error 401 Unauthorized

**Síntoma:**
```
Error: 401 Unauthorized
```

**Solución:**
- ✅ Verifica que tu API Key sea correcta en `application.properties`
- ✅ Comprueba que no haya espacios antes/después del token
- ✅ Asegúrate que el token no haya expirado
- ✅ Regenera el token en el portal del proveedor si es necesario

---

### Problema 2: Error de conexión

**Síntoma:**
```
java.net.ConnectException: Connection refused
```

**Solución:**
- ✅ Verifica la URL de la API en `application.properties`
- ✅ Comprueba tu conexión a internet
- ✅ Verifica que no haya firewall bloqueando la conexión
- ✅ Prueba hacer ping a la URL de la API

---

### Problema 3: Error 404 Not Found

**Síntoma:**
```
Error: 404 Not Found - /qod/v0/sessions
```

**Solución:**
- ✅ El endpoint puede ser diferente según el proveedor
- ✅ Verifica la documentación de tu proveedor
- ✅ Puede ser `/qod/sessions` en vez de `/qod/v0/sessions`
- ✅ Actualiza la URI en `DeviceService.java`

---

### Problema 4: Lombok no funciona

**Síntoma:**
```
Cannot find symbol: method builder()
```

**Solución:**
- ✅ Instala el plugin de Lombok en tu IDE
- ✅ En IntelliJ: Settings → Plugins → Buscar "Lombok" → Install
- ✅ Enable annotation processing: Settings → Build → Compiler → Annotation Processors → Enable
- ✅ Rebuild el proyecto: Build → Rebuild Project

---

### Problema 5: Token no se carga

**Síntoma:**
El token aparece vacío o null

**Solución:**
```properties
# Asegúrate que NO haya comillas
# ❌ MAL:
network.as.code.token="tu_token_aqui"

# ✅ BIEN:
network.as.code.token=tu_token_aqui
```

---

## 📚 Recursos Adicionales

- **CAMARA Project**: https://github.com/camaraproject
- **QoD API Spec**: https://github.com/camaraproject/QualityOnDemand
- **Nokia Network as Code SDK (Python)**: https://github.com/nokia/network-as-code-py
- **Telefónica Open Gateway**: https://opengateway.telefonica.com/
- **Spring WebClient Docs**: https://docs.spring.io/spring-framework/reference/web/webflux-webclient.html

---

## 🎯 Resumen de Pasos

1. ✅ **Obtén tu API Key** del proveedor (Nokia, Telefónica, etc.)
2. ✅ **Configura application.properties** con URL y token
3. ✅ **Crea las clases de modelo** (Device, DeviceIpv4Addr, QodSession)
4. ✅ **Configura el WebClient** en NetworkAsCodeConfig
5. ✅ **Implementa los servicios** (NetworkAsCodeClient, DeviceService)
6. ✅ **Crea controladores REST** (opcional)
7. ✅ **Prueba tu integración** con ejemplos
8. ✅ **¡Disfruta de QoS en tu aplicación!** 🎉

---

## 📝 Variables de Entorno (Producción)

Para producción, NO pongas el token en `application.properties`. Usa variables de entorno:

```properties
# application.properties
network.as.code.api.url=${NETWORK_API_URL:https://network-as-code.example.com}
network.as.code.token=${NETWORK_API_TOKEN}
```

Y configura las variables:

```bash
export NETWORK_API_URL=https://network-as-code.nokia.com
export NETWORK_API_TOKEN=tu_token_secreto
```

---

## ✅ Checklist Final

Antes de ir a producción:

- [ ] API Key obtenida y configurada
- [ ] URL de la API correcta para tu proveedor
- [ ] Todas las dependencias Maven instaladas
- [ ] Código compilando sin errores
- [ ] Pruebas básicas funcionando
- [ ] Token guardado de forma segura (variables de entorno)
- [ ] Logs configurados apropiadamente
- [ ] Documentación API actualizada

---

**¿Preguntas? ¿Problemas?** Revisa la sección de [Troubleshooting](#troubleshooting) o consulta la documentación de tu proveedor.

**¡Buena suerte con tu integración de Network as Code!** 🚀📡

