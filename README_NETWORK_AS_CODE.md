# Integración de Network as Code con Spring Boot

## 📋 Resumen

He integrado exitosamente la API de **Network as Code** (Python SDK) a tu proyecto Java Spring Boot mediante la creación de un cliente REST que se comunica con la API de CAMARA. Esta solución replica la funcionalidad del SDK de Python en Java.

## 🎯 ¿Qué se ha implementado?

### 1. **Modelos de Datos** (`/models/`)
- `DeviceIpv4Addr.java` - Representa la dirección IPv4 del dispositivo
- `Device.java` - Modelo del dispositivo (equivalente a `Device` en Python)
- `QodSession.java` - Modelo de sesión de Quality on Demand
- `CreateQodSessionRequest.java` - Request para crear sesiones QoD

### 2. **Servicios** (`/service/`)
- `NetworkAsCodeClient.java` - Cliente principal (equivalente a `NetworkAsCodeClient` de Python)
- `DeviceService.java` - Servicio para operaciones de dispositivos y sesiones QoD
- `NetworkAsCodeExampleService.java` - Ejemplos de uso

### 3. **Configuración** (`/config/`)
- `NetworkAsCodeConfig.java` - Configuración del WebClient para llamadas a la API

### 4. **Controlador REST** (`/controller/`)
- `NetworkAsCodeController.java` - Endpoints REST para gestionar sesiones QoD

## 🚀 Configuración

### Paso 1: Configurar credenciales de API

Edita `src/main/resources/application.properties`:

```properties
# URL de la API de Network as Code (reemplaza con la URL real de tu operador)
network.as.code.api.url=https://network-as-code.example.com

# Token de autenticación (reemplaza con tu application key)
network.as.code.token=your-application-key-here
```

### Paso 2: Instalar dependencias

```bash
./mvnw clean install
```

### Paso 3: Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

## 📝 Cómo usar

### Ejemplo 1: Código equivalente al Python original

**Python:**
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

**Java (Opción A - Usando servicios):**
```java
@Autowired
private NetworkAsCodeClient networkAsCodeClient;

@Autowired
private DeviceService deviceService;

public void createQodSession() {
    // 1. Crear el objeto DeviceIpv4Addr
    DeviceIpv4Addr ipv4Address = DeviceIpv4Addr.builder()
            .publicAddress("233.252.0.2")
            .privateAddress("192.0.2.25")
            .publicPort(80)
            .build();
    
    // 2. Obtener el dispositivo (equivalente a client.devices.get())
    Device myDevice = networkAsCodeClient.getDevice(
            ipv4Address,
            "2001:db8:1234:5678:9abc:def0:fedc:ba98",
            "+999991234567"
    );
    
    // 3. Crear la sesión QoD (equivalente a my_device.create_qod_session())
    QodSession mySession = deviceService.createQodSession(
            myDevice,
            "233.252.0.2",                           // service_ipv4
            "2001:db8:1234:5678:9abc:def0:fedc:ba98", // service_ipv6
            "DOWNLINK_L_UPLINK_L",                   // profile
            3600                                      // duration (1 hora)
    );
    
    System.out.println("Session ID: " + mySession.getSessionId());
}
```

**Java (Opción B - Usando servicio de ejemplo):**
```java
@Autowired
private NetworkAsCodeExampleService exampleService;

public void test() {
    // Este método replica exactamente el ejemplo de Python
    QodSession session = exampleService.createExampleQodSession();
    System.out.println("Sesión creada: " + session.getSessionId());
}
```

### Ejemplo 2: Usando el API REST

#### Crear una sesión QoD

**POST** `http://localhost:8080/api/network/qod-session`

```json
{
  "publicAddress": "233.252.0.2",
  "privateAddress": "192.0.2.25",
  "publicPort": 80,
  "ipv6Address": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
  "phoneNumber": "+999991234567",
  "serviceIpv4": "233.252.0.2",
  "serviceIpv6": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
  "profile": "DOWNLINK_L_UPLINK_L",
  "duration": 3600
}
```

**Respuesta:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "device": {
    "ipv4Address": {
      "publicAddress": "233.252.0.2",
      "privateAddress": "192.0.2.25",
      "publicPort": 80
    },
    "ipv6Address": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
    "phoneNumber": "+999991234567"
  },
  "serviceIpv4": "233.252.0.2",
  "serviceIpv6": "2001:db8:1234:5678:9abc:def0:fedc:ba98",
  "qosProfile": "DOWNLINK_L_UPLINK_L",
  "duration": 3600,
  "status": "ACTIVE"
}
```

#### Obtener una sesión existente

**GET** `http://localhost:8080/api/network/qod-session/{sessionId}`

#### Eliminar una sesión

**DELETE** `http://localhost:8080/api/network/qod-session/{sessionId}`

## 🔧 Perfiles QoS Disponibles

Los perfiles típicos de calidad de servicio son:

- `DOWNLINK_L_UPLINK_L` - Baja latencia en descarga y subida
- `DOWNLINK_M_UPLINK_M` - Latencia media en descarga y subida
- `DOWNLINK_H_UPLINK_H` - Alto rendimiento en descarga y subida
- `DOWNLINK_XL_UPLINK_XL` - Rendimiento extra alto

> **Nota:** Los perfiles específicos pueden variar según tu operador de red. Consulta la documentación de tu proveedor.

## 📁 Estructura del Proyecto

```
src/main/java/com/talenArena/SafeZone/
├── config/
│   └── NetworkAsCodeConfig.java       # Configuración del WebClient
├── models/
│   ├── Device.java                    # Modelo del dispositivo
│   ├── DeviceIpv4Addr.java           # Dirección IPv4
│   ├── QodSession.java                # Sesión QoD
│   └── CreateQodSessionRequest.java   # Request para crear sesión
├── service/
│   ├── NetworkAsCodeClient.java       # Cliente principal
│   ├── DeviceService.java             # Servicio de dispositivos
│   └── NetworkAsCodeExampleService.java # Ejemplos de uso
└── controller/
    └── NetworkAsCodeController.java   # Endpoints REST
```

## 🔍 Dependencias Agregadas

Se agregaron las siguientes dependencias al `pom.xml`:

```xml
<!-- WebClient for REST calls -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- Jackson for JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

## 🛠️ Troubleshooting

### Error: "Cannot resolve symbol 'log'"
- Asegúrate de que el plugin de Lombok esté instalado en tu IDE
- Verifica que el procesador de anotaciones de Lombok esté habilitado

### Error: "Connection refused"
- Verifica que la URL de la API en `application.properties` sea correcta
- Confirma que tu token de autenticación sea válido
- Revisa los logs para más detalles

### Error: "401 Unauthorized"
- Tu token de autenticación puede haber expirado
- Verifica que el formato del token sea correcto (debe incluir "Bearer" en el header)

## 📚 Referencias

- [CAMARA Project](https://github.com/camaraproject) - Especificación de las APIs
- [Network as Code Python SDK](https://github.com/nokia/network-as-code-py) - SDK original de Python
- [QoD API Specification](https://github.com/camaraproject/QualityOnDemand) - Especificación de la API de Quality on Demand
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html) - Documentación de WebClient

## ✅ Estado del Proyecto

- ✅ Compilación exitosa
- ✅ Modelos de datos implementados
- ✅ Cliente REST configurado
- ✅ Servicios implementados
- ✅ Controladores REST creados
- ✅ Ejemplos de uso documentados
- ✅ Documentación completa

## 🎓 Próximos Pasos

1. **Configurar las credenciales reales** en `application.properties`
2. **Probar la integración** con tu operador de red
3. **Personalizar** según tus necesidades específicas
4. **Agregar manejo de errores** más robusto
5. **Implementar logs** adicionales para debugging

---

**¡Listo!** Ahora tienes la API de Network as Code completamente integrada en tu proyecto Java Spring Boot.

