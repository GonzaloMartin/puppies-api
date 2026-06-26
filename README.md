# Gudboy — Refugio de Animales 🐾

Bienvenido a **Gudboy**, una aplicación Java de escritorio diseñada para automatizar y administrar las operaciones clave en refugios de animales domésticos y clínicas veterinarias. 

Este proyecto fue desarrollado utilizando una arquitectura en capas, patrones de diseño (como Repositorios, Servicios, Controladores, Observadores y Adaptadores) y una interfaz de usuario interactiva construida sobre **Java Swing**.

---

## 📌 Tabla de Contenidos

*   [🏗️ Arquitectura del Sistema](#️-arquitectura-del-sistema)
*   [🚀 Módulos Principales](#-módulos-principales)
*   [🛠️ Requisitos del Sistema y Tecnologías](#️-requisitos-del-sistema-y-tecnologías)
*   [⚙️ Instrucciones de Instalación y Ejecución](#️-instrucciones-de-instalación-y-ejecución)
    *   [1. Configurar Base de Datos MySQL](#1-configurar-base-de-datos-mysql)
    *   [2. Compilar el Proyecto](#2-compilar-el-proyecto)
    *   [3. Ejecutar la Aplicación](#3-ejecutar-la-aplicación)
*   [🤝 Contribuciones del Equipo](#-contribuciones-del-equipo-uade---análisis-y-diseño-oo)

---

## 🏗️ Arquitectura del Sistema

La solución sigue un patrón de diseño limpio y desacoplado, estructurado de la siguiente manera:

*   **`com.gudboy.domain` (Capa de Dominio):** Contiene las entidades esenciales de negocio (`AnimalDomestico`, `Usuario`, `Adopcion`, `Alarma`, `FichaMedica`, `Tratamiento`, `Seguimiento`, `Visita`), enumeraciones y contratos de interfaces de repositorios. No tiene dependencias de infraestructura.
*   **`com.gudboy.repository` (Capa de Persistencia):** Implementa los contratos de repositorio utilizando base de datos física **MySQL** (ej. `AnimalRepositoryMySQL`, `SeguimientoRepositoryMySQL`, `VisitaRepositoryMySQL`), administrando transacciones y consultas de manera segura.
*   **`com.gudboy.service` (Capa de Negocio/Servicios):** Orquesta la lógica del negocio, validaciones y reglas operativas, coordinando las entidades del dominio con la capa de persistencia.
*   **`com.gudboy.controller` (Capa de Control):** Intermediarios entre la vista y los servicios de negocio, abstrayendo las acciones del usuario.
*   **`com.gudboy.view` (Capa de Presentación - UI):** Interfaz gráfica interactiva desarrollada con **Java Swing** (`VentanaPrincipal`), provista de formularios, tablas, listados personalizados de diseño dinámico y controles adaptativos.

---

## 🚀 Módulos Principales

1. **Gestión de Animales & Fichas Médicas:** Registro, actualización y control de la historia clínica de los animales domésticos, tratamientos activos y estados de salud.
2. **Gestión de Salud y Tratamientos:** Registra las evoluciones médicas y los tratamientos.
3. **Motor de Alarmas y Avisos:** Notificación automática de tratamientos médicos, controles de vacunación y recordatorios del refugio. 
4. **Gestión de Adoptantes y Ventas:** Registro de adoptantes, personal del refugio e historial completo de adopciones realizadas.
5. **Seguimiento Post-Adopción, Visitas y Reportes:** Organiza las visitas, programa los recordatorios, procesa la encuesta final.

---

## 🛠️ Requisitos del Sistema y Tecnologías

*   **Lenguaje:** Java 17 o superior.
*   **Base de Datos:** MySQL Server (puerto estándar `3306`).
*   **Librerías de GUI:** Java Swing & AWT (`javax.swing`).
*   **Construcción y Dependencias:** Apache Maven.

---

## ⚙️ Instrucciones de Instalación y Ejecución

### 1. Configurar Base de Datos MySQL
Asegúrate de tener un servidor MySQL activo con el esquema del refugio. Puedes levantar el contenedor de base de datos directamente con Docker si lo deseas:
```bash
docker-compose up -d
```
Verifica que las credenciales en `ConexionMySQL.java` (o archivo de configuración equivalente en infraestructura) sean correctas para tu entorno local.

### 2. Compilar el Proyecto
Para descargar dependencias y compilar el código fuente utilizando Maven:
```bash
mvn clean compile
```

O, si utilizas el compilador nativo directamente desde la consola:
```bash
javac -d target -sourcepath src\main\java src\main\java\com\gudboy\Main.java
```

### 3. Ejecutar la Aplicación
Inicia la aplicación principal ejecutando la clase `Main`:
```bash
mvn exec:java -Dexec.mainClass="com.gudboy.Main"
```
O de manera directa:
```bash
java -cp target;libs/* com.gudboy.Main
```

---

## 🤝 Contribuciones del Equipo

Este sistema representa el trabajo integrado de nuestro equipo para la cátedra de Procesos de Desarrollo de Software:

* Abate, Lautaro – LU: 1184440
* Claa, Lucio – LU: 1183945
* Montalvo, Gonzalo – LU: 1142004
* Rizzi, Tomas – LU: 1183493
* Zambrano, Miguel – LU: 1174581