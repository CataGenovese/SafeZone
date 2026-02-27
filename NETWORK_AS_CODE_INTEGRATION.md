# Network as Code Integration - SafeZone

## Descripción

Esta integración permite usar la API de Network as Code (CAMARA Project) en Java, equivalente a la librería Python `network_as_code`.

## Configuración

### 1. Configurar las propiedades de la aplicación

Edita el archivo `src/main/resources/application.properties`:

```properties
# URL de la API de Network as Code
network.as.code.api.url=https://network-as-code.example.com

# Token de autenticación (tu application key)
network.as.code.token=your-application-key-here
```

### 2. Instalar dependencias

Ejecuta Maven para instalar las dependencias:

```bash
./mvnw clean install
```

## Uso

### Ejemplo básico (equivalente al código Python)

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
    
    // 2. Obtener el dispositivo
    Device myDevice = networkAsCodeClient.getDevice(
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
}
```

### Usar el servicio de ejemplo

Ya hay un servicio creado con el ejemplo completo:

```java
@Autowired
private NetworkAsCodeExampleService exampleService;

public void test() {
    QodSession session = exampleService.createExampleQodSession();
    System.out.println("Sesión creada: " + session.getSessionId());
}
```

### Usar el endpoint REST

Puedes crear una sesión QoD usando el endpoint REST:

**POST** `/api/network/qod-session`

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

**GET** `/api/network/qod-session/{sessionId}` - Obtener una sesión

**DELETE** `/api/network/qod-session/{sessionId}` - Eliminar una sesión

## Estructura del proyecto

```
src/main/java/com/talenArena/SafeZone/
├── config/
│   └── NetworkAsCodeConfig.java          # Configuración del cliente WebClient
├── models/
│   ├── Device.java                       # Modelo del dispositivo
│   ├── DeviceIpv4Addr.java              # Dirección IPv4 del dispositivo
│   ├── QodSession.java                   # Modelo de la sesión QoD
│   └── CreateQodSessionRequest.java      # Request para crear sesión
├── service/
│   ├── NetworkAsCodeClient.java          # Cliente principal (equivalente a NetworkAsCodeClient de Python)
│   ├── DeviceService.java                # Servicio de dispositivos (métodos del device)
│   └── NetworkAsCodeExampleService.java  # Ejemplos de uso
└── controller/
    └── NetworkAsCodeController.java      # Endpoints REST
```

## Perfiles QoS disponibles

Los perfiles de calidad de servicio típicos son:

- `DOWNLINK_L_UPLINK_L` - Low latency downlink y uplink
- `DOWNLINK_M_UPLINK_M` - Medium latency downlink y uplink
- `DOWNLINK_H_UPLINK_H` - High throughput downlink y uplink
- `DOWNLINK_XL_UPLINK_XL` - Extra high throughput

Consulta la documentación de tu operador para los perfiles específicos disponibles.

## Notas importantes

1. **URL de la API**: Cambia `network.as.code.api.url` por la URL real de tu operador
2. **Token de autenticación**: Reemplaza `your-application-key-here` con tu token real
3. **Endpoints**: Los endpoints pueden variar según el operador. Los mostrados siguen el estándar CAMARA QoD API v0

## Troubleshooting

Si encuentras errores de conexión:
1. Verifica que la URL de la API sea correcta
2. Confirma que tu token de autenticación sea válido
3. Revisa los logs en la consola para más detalles

## Referencias

- [CAMARA Project](https://github.com/camaraproject)
- [Network as Code Python SDK](https://github.com/nokia/network-as-code-py)
- [QoD API Specification](https://github.com/camaraproject/QualityOnDemand)
package com.talenArena.SafeZone.service;

import com.talenArena.SafeZone.models.Device;
import com.talenArena.SafeZone.models.DeviceIpv4Addr;
import com.talenArena.SafeZone.models.QodSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Ejemplo de uso del cliente de Network as Code
 * Este servicio demuestra cómo usar la API de Network as Code en Java
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkAsCodeExampleService {

    private final NetworkAsCodeClient networkAsCodeClient;
    private final DeviceService deviceService;

    /**
     * Ejemplo equivalente al código Python proporcionado:
     * 
     * Python:
     * my_device = client.devices.get(
     *     ipv4_address=DeviceIpv4Addr(
     *         public_address="233.252.0.2",
     *         private_address="192.0.2.25",
     *         public_port=80
     *     ),
     *     ipv6_address="2001:db8:1234:5678:9abc:def0:fedc:ba98",
     *     phone_number="+999991234567"
     * )
     * 
     * my_session = my_device.create_qod_session(
     *     service_ipv4="233.252.0.2",
     *     service_ipv6="2001:db8:1234:5678:9abc:def0:fedc:ba98",
     *     profile="DOWNLINK_L_UPLINK_L",
     *     duration=3600
     * )
     */
    public QodSession createExampleQodSession() {
        log.info("Creando sesión QoD de ejemplo - equivalente al código Python");
        
        // 1. Crear el objeto DeviceIpv4Addr (equivalente a Python)
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
        
        log.info("Sesión QoD creada exitosamente: {}", mySession.getSessionId());
        return mySession;
    }

    /**
     * Ejemplo de uso personalizado
     */
    public QodSession createCustomQodSession(String publicIp, String privateIp, Integer port,
                                             String ipv6, String phoneNumber,
                                             String serviceIpv4, String serviceIpv6,
                                             String profile, Integer durationSeconds) {
        
        DeviceIpv4Addr ipv4Address = DeviceIpv4Addr.builder()
                .publicAddress(publicIp)
                .privateAddress(privateIp)
                .publicPort(port)
                .build();
        
        Device device = networkAsCodeClient.getDevice(ipv4Address, ipv6, phoneNumber);
        
        return deviceService.createQodSession(
                device,
                serviceIpv4,
                serviceIpv6,
                profile,
                durationSeconds
        );
    }
}

