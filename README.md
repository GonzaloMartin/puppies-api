# Gudboy — Refugio de Animales 🐾

[![Java CI + Docker & Maven](https://github.com/GonzaloMartin/puppies-api/actions/workflows/ci.yml/badge.svg)](https://github.com/GonzaloMartin/puppies-api/actions/workflows/ci.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/GonzaloMartin/puppies-api?style=flat-square)

### 🛠️ Tecnologías y Versiones
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4-59666C?style=flat-square&logo=hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)

---

Bienvenido a **Gudboy**, una aplicación Java de escritorio para la gestión integral de refugios de animales domésticos y clínicas veterinarias.

El sistema fue desarrollado con una arquitectura en capas, patrones de diseño (Repositorio, Servicio, Controlador, Observador, Adaptador, State, Factory, Strategy) y una interfaz gráfica construida sobre **Java Swing**.

---

## 📌 Tabla de Contenidos

- [🏗️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
- [🎨 Patrones de Diseño Aplicados](#-patrones-de-diseño-aplicados)
- [📦 DTOs](#-dtos)
- [🚀 Módulos Principales](#-módulos-principales)
- [🛠️ Requisitos del Sistema](#️-requisitos-del-sistema)
- [⚙️ Instalación y Ejecución](#️-instalación-y-ejecución)
- [🤝 Contribuciones del Equipo](#-contribuciones-del-equipo)

---

## 🏗️ Arquitectura del Sistema

El proyecto sigue un modelo en capas desacopladas:

**`com.gudboy.domain` — Capa de Dominio**
Contiene las entidades de negocio anotadas con JPA (`Animal`, `AnimalDomestico`, `AnimalSalvaje`, `Usuario`, `Veterinario`, `Visitador`, `Adopcion`, `FichaMedica`, `Seguimiento`, `Visita`, `Encuesta`, `Alarma`), enumeraciones, interfaces de estado e interfaces de repositorio. No tiene dependencias de infraestructura.

**`com.gudboy.dto` — Data Transfer Objects**
Clases inmutables que transportan datos entre capas sin exponer las entidades de dominio (`AnimalDTO`, `UsuarioDTO`, `FichaMedicaDTO`, `SeguimientoDTO`, `VisitaDTO`, `AdopcionDTO`, `AlarmaDTO`, `EncuestaDTO`).

**`com.gudboy.repository` — Capa de Persistencia**
Implementa los contratos de repositorio. Las implementaciones activas (`AnimalRepositoryHibernate`, `FichaMedicaRepositoryHibernate`, `SeguimientoRepositoryHibernate`, `VisitaRepositoryHibernate`, `AlarmaRepositoryMySql`) utilizan Hibernate a través de `HibernateUtil`. También existen implementaciones en memoria para pruebas unitarias.

**`com.gudboy.service` — Capa de Negocio**
Orquesta la lógica de negocio, validaciones y reglas operativas coordinando entidades de dominio con los repositorios.

**`com.gudboy.controller` — Capa de Control**
Intermedia entre la vista y los servicios. Traduce acciones del usuario en llamadas de servicio y devuelve DTOs a la vista.

**`com.gudboy.view` — Capa de Presentación**
Interfaz gráfica desarrollada con Java Swing (`VentanaPrincipal`), con formularios, tablas y controles adaptativos.

**`com.gudboy.infrastructure` — Infraestructura**
Contiene `HibernateUtil` (fábrica de sesiones), `ConexionMySQL` (singleton JDBC de soporte) y `ActividadRegistry`.

---

## 🎨 Patrones de Diseño Aplicados

**State** — Los animales implementan dos máquinas de estado independientes: estado de salud (`EstadoSaludable`, `EstadoEnTratamiento`) y estado de adopción (`EstadoDisponible`, `EstadoAdoptado`). Los tratamientos también usan State: `Pendiente`, `EnCurso`, `Finalizado`, `Cancelado`.

**Factory** — `FabricaAnimal` y sus subclases (`FabricaAnimalDomestico`, `FabricaAnimalSalvaje`) centralizan la creación de animales a partir de DTOs.

**Observer** — El módulo de seguimiento notifica eventos mediante observadores de notificación (`EmailNotificacion`, `SMSNotificacion`, `WhatsAppNotificacion`), desacoplados a través de `IObservador`.

**Adapter** — Las integraciones de comunicación externas (JavaMail, Twilio SMS, Meta WhatsApp) están abstraídas detrás de interfaces (`IEmailAdapter`, `ISMSAdapter`, `IWhatsAppAdapter`).

**Repository** — Cada entidad de dominio tiene su interfaz de repositorio (`IAnimalRepository`, `IFichaMedicaRepository`, etc.) con implementaciones intercambiables: Hibernate, MySQL/JDBC y en memoria.

**Strategy** — El exportador de ficha médica (`Exportador`, `ExportadorPDF`, `ExportadorExcel`) implementa Strategy para variar el formato de salida sin modificar la lógica de la ficha.

---

## 📦 DTOs

Los DTOs desacoplan la vista y los controladores de las entidades de dominio. Cada DTO es una clase inmutable con constructor explícito y getters. El flujo estándar es:

```
Vista → DTO → Controller → Service → entidad de dominio → Repository (Hibernate)
```

---

## 🚀 Módulos Principales

**Gestión de Animales y Fichas Médicas**
Registro, actualización y control del historial clínico. Cada `AnimalDomestico` tiene una `FichaMedica` asociada que agrupa tratamientos y comentarios médicos.

**Gestión de Salud y Tratamientos**
Registra la evolución médica mediante el patrón State en `Tratamiento`. El sistema de alarmas notifica vencimientos y controles periódicos.

**Motor de Alarmas**
Notificación automática de tratamientos, vacunaciones y recordatorios. Las alarmas se persisten con Hibernate y se verifican contra la fecha actual.

**Gestión de Adoptantes y Adopciones**
Registro de visitadores (adoptantes) y veterinarios. Historial completo de adopciones con sus animales asociados.

**Seguimiento Post-Adopción**
Organiza visitas programadas, envía recordatorios por email/SMS/WhatsApp según la preferencia del adoptante, y procesa la encuesta final de cierre de seguimiento.

---

## 🛠️ Requisitos del Sistema

- **Java:** 17 o superior
- **Base de Datos:** MySQL Server (puerto `3306`)
- **ORM:** Hibernate 6 con anotaciones Jakarta Persistence
- **GUI:** Java Swing / AWT
- **Build:** Apache Maven

---

## ⚙️ Instalación y Ejecución

### 1. Levantar la Base de Datos

Con Docker (recomendado):

```bash
docker-compose up -d
```

Luego ejecutar el script de esquema para crear las tablas:

```bash
mysql -u root -p gudboy < src/main/resources/schema.sql
mysql -u root -p gudboy < src/main/resources/seed.sql
```

Verificar que las credenciales en `src/main/resources/db.properties` sean correctas:

```properties
db.url=jdbc:mysql://localhost:3306/gudboy
db.user=root
db.password=tu_password
```

### 2. Compilar

```bash
mvn clean compile
```

### 3. Ejecutar

```bash
mvn exec:java -Dexec.mainClass="com.gudboy.Main"
```

O directamente:

```bash
java -cp target/classes;libs/* com.gudboy.Main
```

### Ejecución de Tests

```bash
mvn test
```

Los tests unitarios usan implementaciones en memoria (`AnimalRepositoryEnMemoria`, `SeguimientoRepositoryEnMemoria`, etc.) para no requerir base de datos activa.
Así mismo, se dispone de una prueba de integración con BD mediante pipeline.

---

## 🤝 Contribuciones del Equipo

Este sistema fue desarrollado por el equipo para la cátedra de Procesos de Desarrollo de Software — UADE:

| Integrante | LU |
|---|---|
| Abate, Lautaro | 1184440 |
| Claa, Lucio | 1183945 |
| Montalvo, Gonzalo | 1142004 |
| Rizzi, Tomás | 1183493 |
| Zambrano, Miguel | 1174581 |