package com.gudboy.domain.historialClinico.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialClinico {

    private final List<String> entradas = new ArrayList<>();

    public void agregar(String descripcion) {
        entradas.add(LocalDateTime.now() + " — " + descripcion);
    }

    public List<String> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }
}
