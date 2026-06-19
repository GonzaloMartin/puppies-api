CREATE DATABASE IF NOT EXISTS gudboy;
USE gudboy;

CREATE TABLE IF NOT EXISTS animal (
    id               VARCHAR(36)  PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    especie          VARCHAR(100) NOT NULL,
    tipo_animal      VARCHAR(10)  NOT NULL,        -- DOMESTICO | SALVAJE
    altura           DOUBLE       NOT NULL,
    peso             DOUBLE       NOT NULL,
    edad             INT          NOT NULL,
    condicion_medica VARCHAR(255),
    en_tratamiento   BOOLEAN      DEFAULT FALSE,    -- solo doméstico
    habitat_natural  VARCHAR(255)                   -- solo salvaje
);

CREATE TABLE IF NOT EXISTS ficha_medica (
    id        VARCHAR(36) PRIMARY KEY,
    animal_id VARCHAR(36) NOT NULL,
    peso      DOUBLE      NOT NULL,
    altura    FLOAT       NOT NULL,
    edad      INT         NOT NULL,
    FOREIGN KEY (animal_id) REFERENCES animal(id)
);
