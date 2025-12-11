-- Tabla Usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- Tabla para la colección de roles (ElementCollection de Usuario)
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla Perfiles
CREATE TABLE perfiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    cultivos VARCHAR(255),
    usuario_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla Maquinarias
CREATE TABLE maquinarias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    marca VARCHAR(255),
    modelo VARCHAR(255),
    anio_fabricacion INT,
    capacidad VARCHAR(255),
    ubicacion_comuna VARCHAR(255),
    ubicacion_region VARCHAR(255),
    precio_por_dia DECIMAL(19, 2),
    dueno_id BIGINT NOT NULL,
    FOREIGN KEY (dueno_id) REFERENCES usuarios(id)
);

-- Tabla Mantenciones
CREATE TABLE mantenciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquinaria_id BIGINT NOT NULL,
    fecha DATE,
    detalle VARCHAR(255),
    FOREIGN KEY (maquinaria_id) REFERENCES maquinarias(id)
);

-- Tabla Avisos
CREATE TABLE avisos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquinaria_id BIGINT NOT NULL,
    disponible_desde DATE,
    disponible_hasta DATE,
    precio_por_dia DECIMAL(19, 2),
    destacado BOOLEAN NOT NULL,
    medio_pago VARCHAR(50),
    condiciones_arriendo VARCHAR(2000),
    FOREIGN KEY (maquinaria_id) REFERENCES maquinarias(id)
);

-- Tabla Reservas
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquinaria_id BIGINT NOT NULL,
    arrendatario_id BIGINT NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    precio_total DECIMAL(19, 2),
    estado VARCHAR(50),
    FOREIGN KEY (maquinaria_id) REFERENCES maquinarias(id),
    FOREIGN KEY (arrendatario_id) REFERENCES usuarios(id)
);
