package com.gudboy.domain.animal.model;

/**
 * Interfaz base que representa a cualquier animal del refugio.
 * Tanto animales domésticos como salvajes implementan esta interfaz.
 */
public interface Animal {

    String getNombre();
    double getAltura();
    double getPeso();
    int getEdad();
    String getCondicionMedica();

    /**
     * Indica si el animal puede ser adoptado.
     * - Los animales salvajes NUNCA pueden ser adoptados.
     * - Los domésticos pueden adoptarse si no están en tratamiento médico.
     */
    boolean esAdoptable();

    /**
     * Retorna una descripción del tipo de animal (doméstico / salvaje).
     */
    String getTipoAnimal();
}
