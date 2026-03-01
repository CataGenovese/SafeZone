# 🎯 Ejemplos Prácticos de Uso - API Geofencing

## 📋 Resumen de Implementación

✅ **13 archivos Java creados** organizados de forma modular
✅ **Compilación exitosa** sin errores
✅ **4 endpoints REST** disponibles
✅ **Modo mock** habilitado por defecto
✅ **Documentación completa** incluida

---

## 🚀 Inicio Rápido

### 1. Iniciar la aplicación

```bash
cd /Users/U01ACC84/Desktop/SafeZone
./mvnw spring-boot:run
```

La aplicación iniciará en: `http://localhost:8090`

---

## 📡 Ejemplos de Uso de la API

### 1️⃣ Crear una Suscripción de Geofencing

**Endpoint:** `POST /api/geofencing/subscriptions`

```bash
curl -X POST http://localhost:8090/api/geofencing/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "protocol": "HTTP",
    "sink": "https://tu-servidor.com/api/webhooks/geofencing/notifications",
    "types": ["org.camaraproject.geofencing-subscriptions.v0.area-entered"],
    "config": {
      "subscriptionDetail": {
        "device": {
          "phoneNumber": "+34600123456"
        },
        "area": {
          "areaType": "CIRCLE",
          "circle": {
            "center": {
              "latitude": 40.4168,
              "longitude": -3.7038
            },
            "radius": 1000
          }
        },
        "subscriptionMaxEvents": 10,
        "subscriptionExpireTime": "2026-12-31T23:59:59Z"
      }
    }
  }'
```

**Respuesta esperada:**
```json
{
  "subscriptionId": "mock-subscription-1234567890",
  "protocol": "HTTP",
  "sink": "https://tu-servidor.com/api/webhooks/geofencing/notifications",
  "types": ["org.camaraproject.geofencing-subscriptions.v0.area-entered"],
  "config": { ... },
  "startsAt": "2026-03-01T18:00:00Z",
  "expiresAt": "2026-03-01T19:00:00Z"
}
```

---

### 2️⃣ Obtener una Suscripción

**Endpoint:** `GET /api/geofencing/subscriptions/{subscriptionId}`

```bash
curl http://localhost:8090/api/geofencing/subscriptions/mock-subscription-1234567890
```

---

### 3️⃣ Listar Todas las Suscripciones

**Endpoint:** `GET /api/geofencing/subscriptions`

```bash
curl http://localhost:8090/api/geofencing/subscriptions
```

---

### 4️⃣ Eliminar una Suscripción

**Endpoint:** `DELETE /api/geofencing/subscriptions/{subscriptionId}`

```bash
curl -X DELETE http://localhost:8090/api/geofencing/subscriptions/mock-subscription-1234567890
```

---

## 🔔 Recibir Notificaciones (Webhook)

Cuando un dispositivo entra o sale del área configurada, Nokia enviará una notificación a tu endpoint webhook.

**Endpoint interno:** `POST /api/webhooks/geofencing/notifications`

**Ejemplo de notificación que recibirás:**
```json
{
  "subscriptionId": "sub-12345",
  "type": "org.camaraproject.geofencing-subscriptions.v0.area-entered",
  "eventTime": "2026-03-01T12:30:00Z",
  "device": {
    "phoneNumber": "+34600123456"
  },
  "area": {
    "areaType": "CIRCLE",
    "circle": {
      "center": {
        "latitude": 40.4168,
        "longitude": -3.7038
      },
      "radius": 1000
    }
  }
}
```

---

## 🔧 Configuración

### Modo Mock (Desarrollo)

Por defecto está habilitado en `application.properties`:

```properties
network.as.code.mock.enabled=true
```

En este modo:
- ✅ No se hacen llamadas reales a la API de Nokia
- ✅ Se generan datos de prueba automáticamente
- ✅ Ideal para desarrollo y pruebas

### Modo Producción

Para usar la API real de Nokia:

1. Edita `application.properties`:
```properties
network.as.code.mock.enabled=false
```

2. Reinicia la aplicación:
```bash
./mvnw spring-boot:run
```

---

## 📂 Archivos Creados

```
src/main/java/com/talenArena/SafeZone/
├── models/
│   ├── GeofencingSubscription.java
│   ├── CreateGeofencingSubscriptionRequest.java
│   └── geofencing/
│       ├── GeofencingPoint.java
│       ├── GeofencingCircle.java
│       ├── GeofencingArea.java
│       ├── GeofencingDeviceInfo.java
│       ├── GeofencingSubscriptionDetail.java
│       ├── GeofencingSubscriptionConfig.java
│       ├── GeofencingNotification.java
│       └── GeofencingErrorResponse.java
├── service/
│   └── GeofencingService.java
└── controller/
    ├── GeofencingController.java
    └── GeofencingWebhookController.java
```

---

## 🎯 Casos de Uso Prácticos

### Caso 1: Notificar cuando un usuario entra en zona de evento

```bash
# Crear suscripción para estadio de fútbol
curl -X POST http://localhost:8090/api/geofencing/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "sink": "https://tu-app.com/webhooks/geofencing",
    "types": ["org.camaraproject.geofencing-subscriptions.v0.area-entered"],
    "config": {
      "subscriptionDetail": {
        "device": {"phoneNumber": "+34600123456"},
        "area": {
          "areaType": "CIRCLE",
          "circle": {
            "center": {"latitude": 40.4530, "longitude": -3.6884},
            "radius": 500
          }
        }
      }
    }
  }'
```

### Caso 2: Alertar cuando un usuario sale de zona segura

```bash
curl -X POST http://localhost:8090/api/geofencing/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "sink": "https://tu-app.com/webhooks/geofencing",
    "types": ["org.camaraproject.geofencing-subscriptions.v0.area-left"],
    "config": {
      "subscriptionDetail": {
        "device": {"phoneNumber": "+34600123456"},
        "area": {
          "areaType": "CIRCLE",
          "circle": {
            "center": {"latitude": 40.4168, "longitude": -3.7038},
            "radius": 2000
          }
        }
      }
    }
  }'
```

---

## 🧪 Probar la Integración

### Opción 1: Con Postman

1. Importa la siguiente colección:
   - Nombre: SafeZone Geofencing API
   - Base URL: `http://localhost:8090`
   - Endpoints: Los 4 mencionados arriba

### Opción 2: Con curl (scripts de prueba)

Crea un archivo `test_geofencing.sh`:

```bash
#!/bin/bash

echo "=== Test 1: Crear Suscripción ==="
RESPONSE=$(curl -s -X POST http://localhost:8090/api/geofencing/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "sink": "https://example.com/webhook",
    "types": ["org.camaraproject.geofencing-subscriptions.v0.area-entered"],
    "config": {
      "subscriptionDetail": {
        "device": {"phoneNumber": "+34600000000"},
        "area": {
          "areaType": "CIRCLE",
          "circle": {
            "center": {"latitude": 40.4168, "longitude": -3.7038},
            "radius": 1000
          }
        }
      }
    }
  }')

echo $RESPONSE | jq .
SUBSCRIPTION_ID=$(echo $RESPONSE | jq -r '.subscriptionId')

echo -e "\n=== Test 2: Obtener Suscripción ==="
curl -s http://localhost:8090/api/geofencing/subscriptions/$SUBSCRIPTION_ID | jq .

echo -e "\n=== Test 3: Listar Suscripciones ==="
curl -s http://localhost:8090/api/geofencing/subscriptions | jq .

echo -e "\n=== Test 4: Eliminar Suscripción ==="
curl -s -X DELETE http://localhost:8090/api/geofencing/subscriptions/$SUBSCRIPTION_ID
echo "Eliminado con éxito"
```

Ejecutar:
```bash
chmod +x test_geofencing.sh
./test_geofencing.sh
```

---

## 📊 Logs de la Aplicación

La aplicación genera logs detallados de todas las operaciones:

```
2026-03-01 18:30:00 INFO  GeofencingService - Modo mock habilitado. Simulando creacion de suscripcion de geofencing
2026-03-01 18:30:00 INFO  GeofencingController - Recibida solicitud para crear suscripcion de geofencing: {...}
2026-03-01 18:31:00 INFO  GeofencingWebhookController - Notificacion de geofencing recibida:
2026-03-01 18:31:00 INFO  GeofencingWebhookController -   - Tipo: org.camaraproject.geofencing-subscriptions.v0.area-entered
```

---

## ✅ Verificación Final

Comprueba que todo funciona:

```bash
# 1. Compilar
./mvnw clean install -DskipTests

# 2. Iniciar
./mvnw spring-boot:run

# 3. Probar (en otra terminal)
curl http://localhost:8090/api/geofencing/subscriptions
```

**Respuesta esperada:** Array JSON con al menos una suscripción mock

---

## 🎉 ¡Integración Completada!

Tu proyecto SafeZone ahora tiene:
- ✅ Integración completa con API de Geofencing de Nokia
- ✅ Endpoints REST funcionales
- ✅ Modo mock para desarrollo
- ✅ Sistema de notificaciones webhook
- ✅ Código modular y bien organizado
- ✅ Documentación completa

**¡Listo para usar!** 🚀

