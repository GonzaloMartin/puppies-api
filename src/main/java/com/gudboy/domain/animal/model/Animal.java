package com.gudboy.domain.animal.model;

import java.util.UUID;

public interface Animal {

    UUID getId();
    String getNombre();
    double getAltura();
    double getPeso();
    int getEdad();
    String getCondicionMedica();

    boolean esAdoptable();
    String getTipoAnimal();
}
