package com.gudboy.domain.animal.factory;

import com.gudboy.domain.animal.model.Animal;

/**
 * Abstract Factory para la creación de animales.
 *
 * Define el contrato común que deben cumplir todas las fábricas de animales
 * del sistema. Cada implementación concreta se encargará de instanciar
 * el tipo correcto de animal con las reglas de negocio correspondientes.
 *
 * Patrón: Abstract Factory
 * Motivación: el sistema debe poder crear animales salvajes o domésticos sin
 * que el resto del código conozca los detalles de construcción de cada uno.
 * Agregar un nuevo tipo de animal (p.ej. "exótico") sólo requiere una nueva
 * implementación de esta interfaz, sin modificar nada existente (OCP).
 */
public interface FabricaAnimal {

    /**
     * Crea un nuevo animal con los datos de su ficha técnica básica.
     *
     * @param nombre         Nombre del animal.
     * @param especie        Especie concreta (perro, zorro, halcón, etc.).
     * @param altura         Altura en metros.
     * @param peso           Peso en kilogramos.
     * @param edad           Edad aproximada en años.
     * @param condicionMedica Descripción de su estado de salud.
     * @return               Instancia de {@link Animal} lista para usar.
     */
    Animal crearAnimal(String nombre, String especie,
                       double altura, double peso,
                       int edad, String condicionMedica);
}
