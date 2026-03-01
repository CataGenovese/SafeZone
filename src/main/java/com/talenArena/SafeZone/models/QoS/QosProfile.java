package com.talenArena.SafeZone.models.QoS;

/**
 * Perfiles QoS predefinidos para sesiones de Quality on Demand.
 *
 * Cada perfil tiene parámetros de red específicos configurados por el operador:
 * - Ancho de banda garantizado (throughput)
 * - Latencia máxima
 * - Jitter máximo
 * - Pérdida de paquetes máxima
 * - Prioridad en la red
 *
 * La API QoD NO permite especificar ancho de banda manualmente.
 * En su lugar, seleccionas un perfil y la red asigna recursos automáticamente.
 */
public enum QosProfile {

    /**
     * Baja latencia para descarga y subida
     * Típicamente:
     * - Latencia: < 50ms
     * - Throughput: Medio
     * - Uso: Gaming, videollamadas
     */
    DOWNLINK_L_UPLINK_L("DOWNLINK_L_UPLINK_L", "Baja latencia bidireccional"),

    /**
     * Latencia media para descarga y subida
     * Típicamente:
     * - Latencia: 50-100ms
     * - Throughput: Medio-Alto
     * - Uso: Streaming de video, navegación
     */
    DOWNLINK_M_UPLINK_M("DOWNLINK_M_UPLINK_M", "Latencia media bidireccional"),

    /**
     * Alto rendimiento para descarga y subida
     * Típicamente:
     * - Latencia: Tolerable
     * - Throughput: Alto
     * - Uso: Transferencia de archivos grandes
     */
    DOWNLINK_H_UPLINK_H("DOWNLINK_H_UPLINK_H", "Alto rendimiento bidireccional"),

    /**
     * Rendimiento extra alto para descarga y subida
     * Típicamente:
     * - Latencia: Tolerable
     * - Throughput: Muy Alto
     * - Uso: Backups, distribución de contenido
     */
    DOWNLINK_XL_UPLINK_XL("DOWNLINK_XL_UPLINK_XL", "Rendimiento extra alto bidireccional"),

    /**
     * Baja latencia solo para descarga
     * Típicamente:
     * - Downlink: Baja latencia
     * - Uplink: Estándar
     * - Uso: Streaming en vivo, video bajo demanda
     */
    DOWNLINK_L("DOWNLINK_L", "Baja latencia en descarga"),

    /**
     * Alto rendimiento solo para descarga
     * Típicamente:
     * - Downlink: Alto throughput
     * - Uplink: Estándar
     * - Uso: Descarga de contenido pesado
     */
    DOWNLINK_H("DOWNLINK_H", "Alto rendimiento en descarga");

    private final String value;
    private final String description;

    QosProfile(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Obtiene un perfil QoS por su valor
     */
    public static QosProfile fromValue(String value) {
        for (QosProfile profile : QosProfile.values()) {
            if (profile.value.equals(value)) {
                return profile;
            }
        }
        throw new IllegalArgumentException("Perfil QoS no válido: " + value);
    }
}

