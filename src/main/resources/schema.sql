CREATE DATABASE IF NOT EXISTS gudboy CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gudboy;

CREATE TABLE IF NOT EXISTS animal (
    id               VARCHAR(36)  PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    especie          VARCHAR(100) NOT NULL,
    tipo_animal      VARCHAR(10)  NOT NULL,
    altura           DOUBLE       NOT NULL,
    peso             DOUBLE       NOT NULL,
    edad             INT          NOT NULL,
    condicion_medica VARCHAR(255),
    en_tratamiento   BOOLEAN      DEFAULT FALSE,
    habitat_natural  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ficha_medica (
    id        VARCHAR(36) PRIMARY KEY,
    animal_id VARCHAR(36) NOT NULL,
    peso      DOUBLE      NOT NULL,
    altura    FLOAT       NOT NULL,
    edad      INT         NOT NULL,
    FOREIGN KEY (animal_id) REFERENCES animal(id)
);

CREATE TABLE IF NOT EXISTS usuario (
    email                VARCHAR(150) PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    apellido             VARCHAR(100) NOT NULL,
    telefono             VARCHAR(20),
    tipo                 VARCHAR(12)  NOT NULL,       -- VETERINARIO / VISITADOR
    matricula_profesional INT,
    especialidad         VARCHAR(100),
    estado_civil         VARCHAR(20),
    ocupacion            VARCHAR(20),
    otras_mascotas       BOOLEAN,
    motivo_adopcion      VARCHAR(255),
    animales_interes     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS adopcion (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    adoptante_email   VARCHAR(150) NOT NULL,
    responsable_email VARCHAR(150) NOT NULL,
    FOREIGN KEY (adoptante_email)   REFERENCES usuario(email),
    FOREIGN KEY (responsable_email) REFERENCES usuario(email)
);

CREATE TABLE IF NOT EXISTS adopcion_animal (
    adopcion_id INT         NOT NULL,
    animal_id   VARCHAR(36) NOT NULL,
    PRIMARY KEY (adopcion_id, animal_id),
    FOREIGN KEY (adopcion_id) REFERENCES adopcion(id),
    FOREIGN KEY (animal_id)   REFERENCES animal(id)
);

CREATE TABLE IF NOT EXISTS alarmas (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    id_animal             VARCHAR(36)  NOT NULL,
    titulo                VARCHAR(200) NOT NULL,
    descripcion           TEXT,
    frecuencia_dias       INT          NOT NULL,
    fecha_proximo_disparo DATETIME     NOT NULL,
    estado                VARCHAR(20)  NOT NULL,
    acciones              TEXT,
    completada            BOOLEAN      DEFAULT FALSE,
    fecha_completado      DATETIME,
    FOREIGN KEY (id_animal) REFERENCES animal(id)
);
