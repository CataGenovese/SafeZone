-- =============================================
-- Script de inicialización de base de datos
-- SafeZone - PostgreSQL
-- =============================================

-- Crear la base de datos (ejecutar primero si no existe)
-- CREATE DATABASE "SmartGate";

-- =============================================
-- ELIMINAR TABLAS SI EXISTEN (orden por dependencias)
-- =============================================
DROP TABLE IF EXISTS empresa_api CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS api CASCADE;
DROP TABLE IF EXISTS empresa CASCADE;
DROP TABLE IF EXISTS company CASCADE;

-- =============================================
-- TABLA: company
-- =============================================
CREATE TABLE company (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- =============================================
-- TABLA: empresa
-- =============================================
CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- =============================================
-- TABLA: api
-- =============================================
CREATE TABLE api (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    endpoint VARCHAR(500) NOT NULL,
    version VARCHAR(50),
    tipo VARCHAR(100) NOT NULL,
    created_at TIMESTAMP
);

-- =============================================
-- TABLA: empleado
-- =============================================
CREATE TABLE empleado (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    rol VARCHAR(50) DEFAULT 'EMPLOYEE',
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_empleado_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- =============================================
-- TABLA: cliente
-- =============================================
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    datos VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_cliente_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- =============================================
-- TABLA: empresa_api (relación N:M entre empresa y api)
-- =============================================
CREATE TABLE empresa_api (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    habilitada BOOLEAN NOT NULL DEFAULT FALSE,
    configuracion VARCHAR(4000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_empresa_api_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
    CONSTRAINT fk_empresa_api_api FOREIGN KEY (api_id) REFERENCES api(id) ON DELETE CASCADE
);

-- =============================================
-- DATOS INICIALES
-- =============================================

-- APIs disponibles
INSERT INTO api (nombre, descripcion, endpoint, version, tipo, created_at) VALUES
('SIM Swap', 'Verificación de cambio de SIM para prevenir fraudes', 'https://network-as-code.p-eu.rapidapi.com/passthrough/camara/v1/sim-swap', 'v0', 'SEGURIDAD', NOW()),
('Location Verification', 'Verificación de ubicación de dispositivos móviles', 'https://network-as-code.p-eu.rapidapi.com/location-verification/v1', 'v1', 'UBICACION', NOW()),
('KYC Fill-In', 'Verificación de identidad del cliente (Know Your Customer)', 'https://network-as-code.p-eu.rapidapi.com/kyc/fill-in/v0', 'v0', 'IDENTIDAD', NOW());

-- Empresa de ejemplo
INSERT INTO empresa (name, created_at, updated_at) VALUES
('TalenArena', NOW(), NOW()),
('CaixaBank', NOW(), NOW());

-- Empleados de ejemplo (password: BCrypt de "password123")
INSERT INTO empleado (empresa_id, email, password, nombre, apellido, rol, activo, created_at, updated_at) VALUES
(1, 'admin@talenarena.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'TalenArena', 'ADMIN', TRUE, NOW(), NOW()),
(1, 'user@talenarena.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Usuario', 'Test', 'EMPLOYEE', TRUE, NOW(), NOW()),
(2, 'admin@caixabank.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'CaixaBank', 'ADMIN', TRUE, NOW(), NOW());

-- Asignación de APIs a empresas
INSERT INTO empresa_api (empresa_id, api_id, habilitada, configuracion, created_at, updated_at) VALUES
(1, 1, TRUE, '{"maxAge": 120}', NOW(), NOW()),
(1, 2, TRUE, '{"radius": 50000}', NOW(), NOW()),
(1, 3, TRUE, '{"mockEnabled": true}', NOW(), NOW()),
(2, 1, TRUE, '{"maxAge": 240}', NOW(), NOW()),
(2, 2, TRUE, '{"radius": 100000}', NOW(), NOW()),
(2, 3, FALSE, NULL, NOW(), NOW());

-- Clientes de ejemplo
INSERT INTO cliente (empresa_id, datos, created_at, updated_at) VALUES
(1, '{"nombre": "Juan García", "telefono": "+34600123456", "email": "juan@example.com"}', NOW(), NOW()),
(1, '{"nombre": "María López", "telefono": "+34640199054", "email": "maria@example.com"}', NOW(), NOW()),
(2, '{"nombre": "Pedro Martínez", "telefono": "+34611222333", "email": "pedro@example.com"}', NOW(), NOW());

-- Company (tabla legacy)
INSERT INTO company (name) VALUES
('TalenArena'),
('CaixaBank');

-- =============================================
-- ÍNDICES
-- =============================================
CREATE INDEX idx_empleado_empresa ON empleado(empresa_id);
CREATE INDEX idx_empleado_email ON empleado(email);
CREATE INDEX idx_cliente_empresa ON cliente(empresa_id);
CREATE INDEX idx_empresa_api_empresa ON empresa_api(empresa_id);
CREATE INDEX idx_empresa_api_api ON empresa_api(api_id);

