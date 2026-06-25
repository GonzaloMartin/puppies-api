-- Script SQL para las nuevas tablas de la Parte 5 (Gonzalo): Seguimiento Post-Adopción
-- Ejecutar estas sentencias en la base de datos 'gudboy'.

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
