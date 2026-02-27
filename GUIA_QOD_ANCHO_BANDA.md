# 🌐 Quality on Demand (QoD) - Guía de Uso

## ❓ Pregunta Frecuente: ¿Cómo se asigna el ancho de banda?

### Respuesta Corta:
**La API QoD de Nokia asigna el ancho de banda y la prioridad AUTOMÁTICAMENTE** cuando creas una sesión. **NO necesitas** (ni puedes) configurar el ancho de banda manualmente.

### Respuesta Detallada:

## 🎯 ¿Cómo funciona realmente?

### 1. Seleccionas un Perfil QoS Predefinido

En lugar de especificar "quiero 10 Mbps de ancho de banda", seleccionas un **perfil QoS** como:

```java
"DOWNLINK_L_UPLINK_L"  // Baja latencia
"DOWNLINK_H_UPLINK_H"  // Alto rendimiento
```

### 2. La Red Asigna Recursos Automáticamente

Cuando creas la sesión con ese perfil, la red móvil automáticamente:

✅ **Reserva ancho de banda** según el perfil
✅ **Asigna prioridad** sobre usuarios normales
✅ **Configura latencia máxima** 
✅ **Garantiza calidad de servicio**

### 3. Tu Dispositivo Obtiene Prioridad

Durante la sesión:
- Tu tráfico tiene **prioridad** sobre usuarios sin sesión QoD
- Se garantiza un **ancho de banda mínimo**
- Se mantiene la **latencia baja**

## 📊 Comparación con el Tráfico Normal

```
┌─────────────────────────────────────────────────┐
│        SIN Sesión QoD (Tráfico Normal)         │
├─────────────────────────────────────────────────┤
│ ❌ Sin prioridad                                 │
│ ❌ Sin garantía de ancho de banda                │
│ ❌ Latencia variable (puede ser alta)            │
│ ❌ Puede haber congestión                        │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│   CON Sesión QoD (Profile: DOWNLINK_L_UPLINK_L) │
├─────────────────────────────────────────────────┤
│ ✅ Alta prioridad en la red                      │
│ ✅ Ancho de banda garantizado (asignado auto)    │
│ ✅ Latencia baja garantizada (< 50ms típico)     │
│ ✅ Protección contra congestión                  │
└─────────────────────────────────────────────────┘
```

## 🔧 Ejemplo Práctico

### Código para crear sesión QoD:

```java
// NO especificas ancho de banda manualmente
// Solo seleccionas el perfil apropiado para tu caso de uso

QodSession session = deviceService.createQodSession(
    device,
    "233.252.0.2",              // IP del servicio
    "2001:db8::1",              // IPv6 del servicio
    "DOWNLINK_L_UPLINK_L",      // ← Perfil QoS (la red asigna recursos automáticamente)
    3600                         // Duración: 1 hora
);

// Después de crear la sesión:
// ✅ La red YA asignó ancho de banda
// ✅ Tu dispositivo YA tiene prioridad
// ✅ La calidad de servicio YA está garantizada
```

### Solicitud HTTP:

```bash
POST /api/network/qod-session
Content-Type: application/json

{
  "publicAddress": "233.252.0.2",
  "phoneNumber": "+34600123456",
  "serviceIpv4": "233.252.0.2",
  "profile": "DOWNLINK_L_UPLINK_L",  ← Seleccionas el perfil
  "duration": 3600
}
```

## 📋 Perfiles QoS Disponibles

Puedes consultar los perfiles disponibles:

```bash
GET /api/network/qos-profiles
```

Respuesta:
```json
[
  {
    "value": "DOWNLINK_L_UPLINK_L",
    "description": "Baja latencia bidireccional"
  },
  {
    "value": "DOWNLINK_M_UPLINK_M",
    "description": "Latencia media bidireccional"
  },
  {
    "value": "DOWNLINK_H_UPLINK_H",
    "description": "Alto rendimiento bidireccional"
  },
  {
    "value": "DOWNLINK_XL_UPLINK_XL",
    "description": "Rendimiento extra alto bidireccional"
  }
]
```

## 🎮 ¿Qué Perfil Usar Según tu Caso de Uso?

### Gaming Online / Videollamadas
```java
profile: "DOWNLINK_L_UPLINK_L"  // Baja latencia
```
- **Prioridad:** Latencia baja
- **Ancho de banda:** Medio (suficiente para el caso de uso)
- **Asignado automáticamente:** ~10-20 Mbps típico

### Streaming de Video (4K)
```java
profile: "DOWNLINK_H_UPLINK_L"  // Alto rendimiento en descarga
```
- **Prioridad:** Alto throughput en bajada
- **Ancho de banda:** Alto
- **Asignado automáticamente:** ~25-50 Mbps típico

### Transferencia de Archivos Grandes
```java
profile: "DOWNLINK_XL_UPLINK_XL"  // Rendimiento extra alto
```
- **Prioridad:** Máximo throughput
- **Ancho de banda:** Muy alto
- **Asignado automáticamente:** 50+ Mbps típico

### Navegación Web Normal
```java
profile: "DOWNLINK_M_UPLINK_M"  // Latencia media
```
- **Prioridad:** Balanceada
- **Ancho de banda:** Medio
- **Asignado automáticamente:** ~5-15 Mbps típico

## ⚠️ Importante: ¿Puedo Modificar el Ancho de Banda?

### ❌ NO, no puedes:
- Especificar "quiero exactamente 10 Mbps"
- Modificar el ancho de banda durante la sesión
- Configurar parámetros de red manualmente

### ✅ SÍ, puedes:
- Seleccionar diferentes perfiles según tus necesidades
- Crear múltiples sesiones QoD para diferentes dispositivos
- Cambiar de perfil creando una nueva sesión

## 🔍 ¿Cómo sé qué ancho de banda me asignaron?

Algunos operadores incluyen esta información en la respuesta:

```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "qosProfile": "DOWNLINK_L_UPLINK_L",
  "status": "ACTIVE",
  "qosDetails": {
    "guaranteedDownlinkBandwidthKbps": 10000,  ← 10 Mbps garantizados
    "guaranteedUplinkBandwidthKbps": 5000,     ← 5 Mbps garantizados
    "maxLatencyMs": 50,                         ← Latencia máxima 50ms
    "priorityLevel": 1,                         ← Máxima prioridad
    "hasPriority": true                         ← Tiene prioridad
  }
}
```

**Nota:** La disponibilidad de `qosDetails` depende del operador. Algunos operadores no exponen estos detalles por seguridad.

## 🚀 Beneficios de las Sesiones QoD

1. **Prioridad Automática:** Tu tráfico tiene prioridad sobre usuarios normales
2. **Calidad Garantizada:** La red garantiza la calidad de servicio
3. **Simplicidad:** No necesitas configurar parámetros de red complejos
4. **Flexibilidad:** Puedes cambiar entre perfiles según necesites
5. **Predecible:** Sabes qué calidad esperar de cada perfil

## 📞 Contacto con el Operador

Para información específica sobre los valores exactos de ancho de banda de cada perfil en tu red, contacta con:

- **Nokia Network as Code:** https://network-as-code.nokia.com
- **Tu operador móvil:** Consulta la documentación específica de tu proveedor
- **CAMARA Project:** https://github.com/camaraproject/QualityOnDemand

---

## 🎯 Resumen

| Pregunta | Respuesta |
|----------|-----------|
| ¿Se asigna ancho de banda automáticamente? | ✅ SÍ, automáticamente según el perfil |
| ¿Tengo prioridad sobre otros usuarios? | ✅ SÍ, durante la sesión QoD |
| ¿Puedo especificar el ancho de banda manualmente? | ❌ NO, se asigna según el perfil |
| ¿Puedo modificar el ancho de banda después? | ❌ NO, debes crear una nueva sesión |
| ¿Qué perfil debo usar? | Depende de tu caso de uso (ver guía arriba) |

---

**¡La red gestiona todo automáticamente! Solo selecciona el perfil adecuado para tu aplicación.** 🎉

