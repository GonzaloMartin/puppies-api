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
    habitat_natural  VARCHAR(255),
    adoptado         BOOLEAN      DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ficha_medica (
    id        VARCHAR(36) PRIMARY KEY,
    animal_id VARCHAR(36) NOT NULL UNIQUE,
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

CREATE TABLE IF NOT EXISTS seguimiento (
    id VARCHAR(36) PRIMARY KEY,
    adopcion_id INT NOT NULL,
    responsable_email VARCHAR(150) NOT NULL,
    dia_semana VARCHAR(15) NOT NULL,
    horario_desde VARCHAR(10) NOT NULL,
    horario_hasta VARCHAR(10) NOT NULL,
    estado VARCHAR(15) NOT NULL,
    preferencia_recordatorio VARCHAR(15) NOT NULL,
    FOREIGN KEY (adopcion_id) REFERENCES adopcion(id),
    FOREIGN KEY (responsable_email) REFERENCES usuario(email)
);

CREATE TABLE IF NOT EXISTS visitas (
    id VARCHAR(36) PRIMARY KEY,
    seguimiento_id VARCHAR(36) NOT NULL,
    fecha_programada DATE NOT NULL,
    fecha_real DATE,
    comentarios VARCHAR(255),
    completada BOOLEAN DEFAULT FALSE,
    continuar_visitas BOOLEAN DEFAULT TRUE,
    estado_general_animal VARCHAR(15),
    limpieza_lugar VARCHAR(15),
    ambiente VARCHAR(15),
    FOREIGN KEY (seguimiento_id) REFERENCES seguimiento(id)
);

CREATE TABLE IF NOT EXISTS tratamiento (
    id           VARCHAR(36)  PRIMARY KEY,
    ficha_id     VARCHAR(36)  NOT NULL,
    tipo         VARCHAR(50)  NOT NULL,
    estado       VARCHAR(20)  NOT NULL DEFAULT 'Pendiente',
    fecha_inicio DATETIME,
    fecha_fin    DATETIME,
    FOREIGN KEY (ficha_id) REFERENCES ficha_medica(id)
);

CREATE TABLE IF NOT EXISTS comentario_medico (
    id                VARCHAR(36)  PRIMARY KEY,
    ficha_id          VARCHAR(36)  NOT NULL,
    veterinario_email VARCHAR(150),
    texto             TEXT         NOT NULL,
    fecha             DATETIME     NOT NULL,
    FOREIGN KEY (ficha_id)          REFERENCES ficha_medica(id),
    FOREIGN KEY (veterinario_email) REFERENCES usuario(email)
);