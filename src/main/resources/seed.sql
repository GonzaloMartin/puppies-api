USE gudboy;
SET NAMES utf8mb4;

-- Animales domésticos
INSERT IGNORE INTO animal (id, nombre, especie, tipo_animal, altura, peso, edad, condicion_medica, en_tratamiento, habitat_natural) VALUES
  ('a1000000-0000-0000-0000-000000000001', 'Firulais', 'Perro',   'DOMESTICO', 0.55, 12.5, 3, 'EstadoSaludable',    false, NULL),
  ('a1000000-0000-0000-0000-000000000002', 'Michi',    'Gato',    'DOMESTICO', 0.30,  4.2, 5, 'EstadoSaludable',    false, NULL),
  ('a1000000-0000-0000-0000-000000000003', 'Piolín',   'Canario', 'DOMESTICO', 0.12,  0.2, 1, 'EstadoSaludable',    false, NULL),
  ('a1000000-0000-0000-0000-000000000004', 'Tomy',     'Tortuga', 'DOMESTICO', 0.20,  1.8, 8, 'EstadoEnTratamiento', true, NULL),
  ('a1000000-0000-0000-0000-000000000005', 'Loro',     'Loro',    'DOMESTICO', 0.35,  0.9, 4, 'EstadoSaludable',    false, NULL);

-- Animales salvajes
INSERT IGNORE INTO animal (id, nombre, especie, tipo_animal, altura, peso, edad, condicion_medica, en_tratamiento, habitat_natural) VALUES
  ('a1000000-0000-0000-0000-000000000006', 'Falco', 'Halcón',   'SALVAJE', 0.40, 1.2, 2, 'EstadoSaludable',     false, 'Sierras cordobesas'),
  ('a1000000-0000-0000-0000-000000000007', 'Pingu', 'Pingüino', 'SALVAJE', 0.50, 4.0, 3, 'EstadoSaludable',     false, 'Costa patagónica'),
  ('a1000000-0000-0000-0000-000000000008', 'Zorro', 'Zorro',    'SALVAJE', 0.60, 5.0, 4, 'EstadoEnTratamiento', false, 'Pampa húmeda');

-- Fichas médicas
INSERT IGNORE INTO ficha_medica (id, animal_id, peso, altura, edad) VALUES
  ('f1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', 12.5, 0.55, 3),
  ('f1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000002',  4.2, 0.30, 5),
  ('f1000000-0000-0000-0000-000000000003', 'a1000000-0000-0000-0000-000000000003',  0.2, 0.12, 1),
  ('f1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000004',  1.8, 0.20, 8),
  ('f1000000-0000-0000-0000-000000000005', 'a1000000-0000-0000-0000-000000000005',  0.9, 0.35, 4),
  ('f1000000-0000-0000-0000-000000000006', 'a1000000-0000-0000-0000-000000000006',  1.2, 0.40, 2),
  ('f1000000-0000-0000-0000-000000000007', 'a1000000-0000-0000-0000-000000000007',  4.0, 0.50, 3),
  ('f1000000-0000-0000-0000-000000000008', 'a1000000-0000-0000-0000-000000000008',  5.0, 0.60, 4);

-- Veterinarios
INSERT IGNORE INTO usuario (email, nombre, apellido, telefono, tipo, matricula_profesional, especialidad) VALUES
  ('ana.lopez@gudboy.com',    'Ana',    'López',    '1122334455', 'VETERINARIO', 1001, 'Clínica general'),
  ('carlos.diaz@gudboy.com',  'Carlos', 'Díaz',     '1133445566', 'VETERINARIO', 1002, 'Cirugía'),
  ('maria.gomez@gudboy.com',  'María',  'Gómez',    '1144556677', 'VETERINARIO', 1003, 'Fauna silvestre');

-- Visitadores (adoptantes)
INSERT IGNORE INTO usuario (email, nombre, apellido, telefono, tipo, estado_civil, ocupacion, otras_mascotas, motivo_adopcion, animales_interes) VALUES
  ('juan.perez@mail.com',   'Juan',   'Pérez',   '1155667788', 'VISITADOR', 'SOLTERO',  'EMPLEADO',   false, 'Compañía',        'Perro'),
  ('lucia.torres@mail.com', 'Lucía',  'Torres',  '1166778899', 'VISITADOR', 'CASADO',   'EMPLEADO',   true,  'Amor a animales', 'Gato'),
  ('pedro.ruiz@mail.com',   'Pedro',  'Ruiz',    '1177889900', 'VISITADOR', 'SOLTERO',  'ESTUDIANTE', false, 'Rescate',         'Cualquiera');

-- Adopciones
INSERT IGNORE INTO adopcion (id, adoptante_email, responsable_email) VALUES
  (1, 'juan.perez@mail.com',   'ana.lopez@gudboy.com'),
  (2, 'lucia.torres@mail.com', 'carlos.diaz@gudboy.com');

INSERT IGNORE INTO adopcion_animal (adopcion_id, animal_id) VALUES
  (1, 'a1000000-0000-0000-0000-000000000001'),
  (2, 'a1000000-0000-0000-0000-000000000002');

-- Alarmas
INSERT IGNORE INTO alarmas (id_animal, titulo, descripcion, frecuencia_dias, fecha_proximo_disparo, estado, acciones, completada) VALUES
  ('a1000000-0000-0000-0000-000000000001', 'Control parasitario Firulais', 'Revisar parásitos internos y externos', 30, DATE_ADD(NOW(), INTERVAL 5  DAY), 'ACTIVA',    'CONTROL_DE_PARASITOS,COLOCAR_ANTIPARASITARIOS', false),
  ('a1000000-0000-0000-0000-000000000002', 'Vacunación Michi',             'Vacuna anual triple felina',             365, DATE_ADD(NOW(), INTERVAL 10 DAY), 'ACTIVA',    'COLOCAR_VACUNA',                               false),
  ('a1000000-0000-0000-0000-000000000004', 'Seguimiento Tomy',             'Control de tratamiento tortuga',         7,   DATE_ADD(NOW(), INTERVAL 2  DAY), 'ACTIVA',    'COMPROBAR_PESO_TAMANIO,CHEQUEAR_NUTRICION',    false),
  ('a1000000-0000-0000-0000-000000000008', 'Revisión Zorro',               'Chequeo post-rescate',                   14,  DATE_ADD(NOW(), INTERVAL 1  DAY), 'ACTIVA',    'CONTROL_DE_PARASITOS,CHEQUEAR_NUTRICION',      false);

-- Seguimiento para Adopción ID 1 (Ana López es la veterinaria responsable)
INSERT IGNORE INTO seguimiento (id, adopcion_id, responsable_email, dia_semana, horario_desde, horario_hasta, estado, preferencia_recordatorio) VALUES
  ('s1000000-0000-0000-0000-000000000001', 1, 'ana.lopez@gudboy.com', 'SABADO', '10:00', '12:00', 'ACTIVO', 'WHATSAPP');

-- Visitas programadas y completadas para el Seguimiento 1
INSERT IGNORE INTO visitas (id, seguimiento_id, fecha_programada, fecha_real, comentarios, completada, continuar_visitas, estado_general_animal, limpieza_lugar, ambiente) VALUES
  ('v1000000-0000-0000-0000-000000000001', 's1000000-0000-0000-0000-000000000001', DATE_ADD(CURDATE(), INTERVAL -7 DAY), DATE_ADD(CURDATE(), INTERVAL -7 DAY), 'Excelente cuidado y adaptacion inicial de Firulais.', true, true, 'BUENO', 'BUENO', 'BUENO'),
  ('v1000000-0000-0000-0000-000000000002', 's1000000-0000-0000-0000-000000000001', DATE_ADD(CURDATE(), INTERVAL 2 DAY), NULL, NULL, false, true, NULL, NULL, NULL);

