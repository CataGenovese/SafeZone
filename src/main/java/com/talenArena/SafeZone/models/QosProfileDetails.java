package com.talenArena.SafeZone.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detalles del perfil QoS asignado por la red.
 *
 * IMPORTANTE: Estos valores son informativos y dependen del operador.
 * La API QoD NO permite configurar estos valores manualmente.
 * Se asignan automáticamente cuando seleccionas un perfil QoS.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QosProfileDetails {

    /**
     * Nombre del perfil QoS
     * Ejemplo: "DOWNLINK_L_UPLINK_L"
     */
    @JsonProperty("name")
    private String name;

    /**
     * Ancho de banda máximo en downlink (Kbps)
     * Asignado automáticamente por la red según el perfil
     */
    @JsonProperty("max_downlink_bandwidth_kbps")
    private Integer maxDownlinkBandwidthKbps;

    /**
     * Ancho de banda máximo en uplink (Kbps)
     * Asignado automáticamente por la red según el perfil
     */
    @JsonProperty("max_uplink_bandwidth_kbps")
    private Integer maxUplinkBandwidthKbps;

    /**
     * Ancho de banda garantizado en downlink (Kbps)
     * La red garantiza al menos este throughput
     */
    @JsonProperty("guaranteed_downlink_bandwidth_kbps")
    private Integer guaranteedDownlinkBandwidthKbps;

    /**
     * Ancho de banda garantizado en uplink (Kbps)
     * La red garantiza al menos este throughput
     */
    @JsonProperty("guaranteed_uplink_bandwidth_kbps")
    private Integer guaranteedUplinkBandwidthKbps;

    /**
     * Latencia máxima permitida (milisegundos)
     */
    @JsonProperty("max_latency_ms")
    private Integer maxLatencyMs;

    /**
     * Jitter máximo permitido (milisegundos)
     */
    @JsonProperty("max_jitter_ms")
    private Integer maxJitterMs;

    /**
     * Pérdida de paquetes máxima permitida (porcentaje)
     */
    @JsonProperty("max_packet_loss_percentage")
    private Double maxPacketLossPercentage;

    /**
     * Nivel de prioridad en la red
     * Valores típicos: 1-10 (1 = máxima prioridad)
     */
    @JsonProperty("priority_level")
    private Integer priorityLevel;

    /**
     * Indica si el tráfico tiene prioridad sobre usuarios normales
     */
    @JsonProperty("has_priority")
    private Boolean hasPriority;
}

