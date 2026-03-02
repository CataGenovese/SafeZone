-- Tabla Empresa
CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Empleado (Usuarios de la empresa)
CREATE TABLE empleado (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    rol VARCHAR(50) DEFAULT 'EMPLOYEE',
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_empleado_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- Tabla Cliente (vinculado a una empresa)
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    datos JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- Tabla API (catálogo de APIs disponibles de Nokia)
CREATE TABLE api (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT,
    endpoint VARCHAR(500) NOT NULL,
    version VARCHAR(50),
    tipo VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla intermedia Empresa_API (relación muchos a muchos con estado de habilitación)
CREATE TABLE empresa_api (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    api_id BIGINT NOT NULL,
    habilitada BOOLEAN DEFAULT FALSE,
    configuracion JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_empresa_api_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
    CONSTRAINT fk_empresa_api_api FOREIGN KEY (api_id) REFERENCES api(id) ON DELETE CASCADE,
    CONSTRAINT unique_empresa_api UNIQUE (empresa_id, api_id)
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_empleado_empresa ON empleado(empresa_id);
CREATE INDEX idx_empleado_email ON empleado(email);
CREATE INDEX idx_cliente_empresa ON cliente(empresa_id);
CREATE INDEX idx_empresa_api_empresa ON empresa_api(empresa_id);
CREATE INDEX idx_empresa_api_api ON empresa_api(api_id);

-- Insertar APIs de Nokia disponibles
INSERT INTO api (nombre, descripcion, endpoint, version, tipo) VALUES
('SIM_SWAP_RETRIEVE', 'Verifica la fecha del último cambio de SIM', '/passthrough/camara/v1/sim-swap/sim-swap/v0/retrieve-date', 'v0', 'SIM_SWAP'),
('SIM_SWAP_CHECK', 'Verifica si hubo cambio de SIM en un periodo específico', '/passthrough/camara/v1/sim-swap/sim-swap/v0/check', 'v0', 'SIM_SWAP'),
('LOCATION_VERIFICATION', 'Verifica la ubicación de un dispositivo', '/location-verification/v0/verify', 'v0', 'LOCATION_VERIFICATION'),
('KYC_MATCH', 'Verifica coincidencia de datos de identidad (Know Your Customer)', '/passthrough/camara/v1/kyc-match/kyc-match/v0.3/match', 'v0.3', 'KYC_MATCH'),
('QOD_SESSION', 'Crea sesión de Quality on Demand para priorizar ancho de banda', '/qod-sessions/v0/sessions', 'v0', 'QOD'),
('GEOFENCING_CREATE', 'Crea suscripción de geofencing', '/geofencing-subscriptions/v0.3/subscriptions', 'v0.3', 'GEOFENCING'),
('GEOFENCING_GET', 'Obtiene detalles de suscripción de geofencing', '/geofencing-subscriptions/v0.3/subscriptions/{subscriptionId}', 'v0.3', 'GEOFENCING');

-- Datos de ejemplo para pruebas
INSERT INTO empresa (name) VALUES
('TechCorp Solutions'),
('SecureNet Inc'),
('GlobalConnect Ltd');

-- Empleados de ejemplo (password: 'password123' - deberías hashear en producción)
INSERT INTO empleado (empresa_id, email, password, nombre, apellido, rol) VALUES
(1, 'admin@techcorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IYl8Zo3.jvKJZqVIJ5EH3fY3y9rV8W', 'Juan', 'García', 'ADMIN'),
(1, 'empleado1@techcorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IYl8Zo3.jvKJZqVIJ5EH3fY3y9rV8W', 'María', 'López', 'EMPLOYEE'),
(2, 'admin@securenet.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IYl8Zo3.jvKJZqVIJ5EH3fY3y9rV8W', 'Carlos', 'Martínez', 'ADMIN'),
(3, 'admin@globalconnect.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IYl8Zo3.jvKJZqVIJ5EH3fY3y9rV8W', 'Ana', 'Rodríguez', 'ADMIN');

-- Clientes de ejemplo con datos en formato JSON
INSERT INTO cliente (empresa_id, datos) VALUES
(1, '{"phoneNumber": "+34666777888", "nombre": "Cliente Premium", "plan": "enterprise", "deviceIp": "233.252.0.2"}'),
(1, '{"phoneNumber": "+34666777889", "nombre": "Cliente Standard", "plan": "standard", "deviceIp": "233.252.0.3"}'),
(2, '{"phoneNumber": "+34666777890", "nombre": "Cliente Business", "plan": "business", "deviceIp": "233.252.0.4"}');

-- Asignar APIs a empresas
-- TechCorp Solutions tiene todas las APIs habilitadas
INSERT INTO empresa_api (empresa_id, api_id, habilitada, configuracion) VALUES
(1, 1, TRUE, '{"maxRequests": 1000, "priority": "high"}'),
(1, 2, TRUE, '{"maxRequests": 1000, "priority": "high"}'),
(1, 3, TRUE, '{"maxRequests": 500, "priority": "medium"}'),
(1, 4, TRUE, '{"maxRequests": 500, "priority": "high"}'),
(1, 5, TRUE, '{"maxRequests": 200, "priority": "high"}'),
(1, 6, TRUE, '{"maxRequests": 300, "priority": "medium"}'),
(1, 7, TRUE, '{"maxRequests": 300, "priority": "medium"}');

-- SecureNet Inc solo tiene algunas APIs habilitadas
INSERT INTO empresa_api (empresa_id, api_id, habilitada, configuracion) VALUES
(2, 1, TRUE, '{"maxRequests": 500, "priority": "medium"}'),
(2, 2, TRUE, '{"maxRequests": 500, "priority": "medium"}'),
(2, 3, FALSE, '{"maxRequests": 0}'),
(2, 4, TRUE, '{"maxRequests": 300, "priority": "high"}');

-- GlobalConnect Ltd tiene configuración básica
INSERT INTO empresa_api (empresa_id, api_id, habilitada, configuracion) VALUES
(3, 1, TRUE, '{"maxRequests": 200, "priority": "low"}'),
(3, 3, TRUE, '{"maxRequests": 200, "priority": "low"}'),
(3, 6, TRUE, '{"maxRequests": 100, "priority": "low"}');

