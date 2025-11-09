-- ============================================================
--  MySQL 8+ | esquema sencillo para "seguridad_calidad"
--  SOLO CREACIÓN + INSERTS BÁSICOS (sin variables ni extras)
-- ============================================================

-- (Opcional) Crear base y usarla
CREATE DATABASE IF NOT EXISTS seguridad_calidad
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE seguridad_calidad;

-- Para recrear limpio (opcional: comentar si no quieres borrar)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS mantenimiento;
DROP TABLE IF EXISTS avisos;
DROP TABLE IF EXISTS maquinarias;
DROP TABLE IF EXISTS usuario_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS medios_pago;
DROP TABLE IF EXISTS perfiles;
SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------
-- Tabla: perfiles
-- ------------------------------
CREATE TABLE perfiles (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre      VARCHAR(50)  NOT NULL UNIQUE,
  descripcion VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: usuarios
-- ------------------------------
CREATE TABLE usuarios (
  id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre                VARCHAR(120)  NOT NULL,
  email                 VARCHAR(150)  NOT NULL UNIQUE,
  password_hash         VARCHAR(255)  NOT NULL,
  telefono              VARCHAR(30)   NULL,
  direccion             VARCHAR(200)  NULL,
  activo                TINYINT(1)    NOT NULL DEFAULT 1,
  perfil_id             BIGINT        NULL,
  fecha_creacion        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_usuarios_perfil FOREIGN KEY (perfil_id) REFERENCES perfiles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: roles
-- ------------------------------
CREATE TABLE roles (
  id      BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre  VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: usuario_roles (N:M)
-- ------------------------------
CREATE TABLE usuario_roles (
  usuario_id BIGINT NOT NULL,
  role_id    BIGINT NOT NULL,
  PRIMARY KEY (usuario_id, role_id),
  CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  CONSTRAINT fk_ur_role    FOREIGN KEY (role_id)    REFERENCES roles(id)    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: maquinarias
-- ------------------------------
CREATE TABLE maquinarias (
  id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
  tipo                 VARCHAR(60)   NOT NULL,
  marca                VARCHAR(80)   NULL,
  modelo               VARCHAR(80)   NULL,
  anio_fabricacion     INT           NULL,
  capacidad            VARCHAR(80)   NULL,
  ubicacion_comuna     VARCHAR(100)  NULL,
  ubicacion_region     VARCHAR(100)  NULL,
  precio_por_dia       DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  dueno_id             BIGINT        NOT NULL,
  activo               TINYINT(1)    NOT NULL DEFAULT 1,
  fecha_creacion       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_maquinaria_dueno FOREIGN KEY (dueno_id) REFERENCES usuarios(id),
  INDEX idx_maquinaria_dueno (dueno_id),
  INDEX idx_maquinaria_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: medios_pago
-- ------------------------------
CREATE TABLE medios_pago (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre   VARCHAR(60) NOT NULL UNIQUE,
  activo   TINYINT(1)  NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: avisos
-- ------------------------------
CREATE TABLE avisos (
  id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
  maquinaria_id           BIGINT        NOT NULL,
  precio_oferta_por_dia   DECIMAL(12,2) NULL,
  fecha_disponible_desde  DATE          NULL,
  fecha_disponible_hasta  DATE          NULL,
  destacado               TINYINT(1)    NOT NULL DEFAULT 0,
  condiciones_arriendo    TEXT          NULL,
  medio_pago_id           BIGINT        NULL,
  activo                  TINYINT(1)    NOT NULL DEFAULT 1,
  fecha_creacion          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_aviso_maquinaria FOREIGN KEY (maquinaria_id) REFERENCES maquinarias(id),
  CONSTRAINT fk_aviso_mediopago FOREIGN KEY (medio_pago_id)  REFERENCES medios_pago(id),
  INDEX idx_aviso_maquinaria (maquinaria_id),
  INDEX idx_aviso_destacado (destacado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: reservas   (sin columna generada 'dias' para simplificar)
-- ------------------------------
CREATE TABLE reservas (
  id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  maquinaria_id     BIGINT        NOT NULL,
  arrendatario_id   BIGINT        NOT NULL,
  fecha_inicio      DATE          NOT NULL,
  fecha_fin         DATE          NOT NULL,
  precio_por_dia    DECIMAL(12,2) NOT NULL,
  total             DECIMAL(14,2) NOT NULL,
  estado            VARCHAR(30)   NOT NULL,  -- PENDIENTE/CONFIRMADA/EN_CURSO/FINALIZADA/CANCELADA
  medio_pago_id     BIGINT        NULL,
  notas             TEXT          NULL,
  fecha_creacion    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_reserva_maquinaria   FOREIGN KEY (maquinaria_id)   REFERENCES maquinarias(id),
  CONSTRAINT fk_reserva_arrendatario FOREIGN KEY (arrendatario_id) REFERENCES usuarios(id),
  CONSTRAINT fk_reserva_mediopago    FOREIGN KEY (medio_pago_id)   REFERENCES medios_pago(id),
  INDEX idx_reserva_maquinaria (maquinaria_id),
  INDEX idx_reserva_arrendatario (arrendatario_id),
  INDEX idx_reserva_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------
-- Tabla: mantenimiento
-- ------------------------------
CREATE TABLE mantenimiento (
  id                BIGINT AUTO_INCREMENT PRIMARY KEY,
  maquinaria_id     BIGINT        NOT NULL,
  descripcion       VARCHAR(255)  NOT NULL,
  fecha             DATE          NOT NULL,
  costo             DECIMAL(12,2) NULL,
  realizado_por     VARCHAR(120)  NULL,
  fecha_creacion    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_mant_maquinaria FOREIGN KEY (maquinaria_id) REFERENCES maquinarias(id),
  INDEX idx_mant_maquinaria (maquinaria_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- INSERTS BÁSICOS (IDs explícitos para evitar variables)
-- ============================================================

-- Perfiles
INSERT INTO perfiles (id, nombre, descripcion) VALUES
  (1, 'ADMIN',        'Administrador del sistema'),
  (2, 'DUENO',        'Propietario de maquinarias'),
  (3, 'ARRENDATARIO', 'Cliente que arrienda maquinarias')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Usuarios (usa un hash BCrypt real en producción)
INSERT INTO usuarios (id, nombre, email, password_hash, telefono, direccion, activo, perfil_id)
VALUES
  (1, 'Admin', 'admin@example.com', '$2a$10$dummyhashdummyhashdummyha', '111111111', 'Santiago', 1, 1)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), perfil_id = VALUES(perfil_id);

-- Roles
INSERT INTO roles (id, nombre) VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER'),
  (3, 'ROLE_DUENO')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Usuario-roles
INSERT INTO usuario_roles (usuario_id, role_id) VALUES
  (1, 1), (1, 2)
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- Medios de pago
INSERT INTO medios_pago (id, nombre, activo) VALUES
  (1, 'EFECTIVO', 1),
  (2, 'TRANSFERENCIA', 1),
  (3, 'TARJETA', 1)
ON DUPLICATE KEY UPDATE activo = VALUES(activo);

-- Maquinaria demo (del dueño = usuario 1)
INSERT INTO maquinarias
(id, tipo, marca, modelo, anio_fabricacion, capacidad, ubicacion_comuna, ubicacion_region, precio_por_dia, dueno_id, activo)
VALUES
  (1, 'TRACTOR', 'John Deere', 'JD-5050', 2020, '50 HP', 'Maipú', 'Metropolitana', 85000.00, 1, 1)
ON DUPLICATE KEY UPDATE precio_por_dia = VALUES(precio_por_dia);

-- Aviso demo
INSERT INTO avisos
(id, maquinaria_id, precio_oferta_por_dia, fecha_disponible_desde, fecha_disponible_hasta, destacado, condiciones_arriendo, medio_pago_id, activo)
VALUES
  (1, 1, 80000.00, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 30 DAY), 1, 'Uso responsable, devolución con estanque lleno.', 2, 1)
ON DUPLICATE KEY UPDATE precio_oferta_por_dia = VALUES(precio_oferta_por_dia);

-- (Reservas y mantenimiento: sin inserts por ser operacionales)
