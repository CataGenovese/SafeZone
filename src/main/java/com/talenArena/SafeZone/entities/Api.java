package com.talenArena.SafeZone.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "api", schema = "app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 500)
    private String endpoint;

    @Column(length = 50)
    private String version;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmpresaApi> empresaApis;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

