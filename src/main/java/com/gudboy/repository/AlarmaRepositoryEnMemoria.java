package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;

import java.util.ArrayList;
import java.util.List;

public class AlarmaRepositoryEnMemoria implements IAlarmaRepository {

    private final List<Alarma> alarmas = new ArrayList<>();
    // Simulamos un autoincremental para la base de datos en memoria
    private int contadorId = 1;

    @Override
    public void add(Alarma alarma) {
        alarma.setId(contadorId++);
        alarmas.add(alarma);
    }

    @Override
    public void update(Alarma alarma) {
        for (int i = 0; i < alarmas.size(); i++) {
            if (alarmas.get(i).getId() == alarma.getId()) {
                alarmas.set(i, alarma);
                return;
            }
        }
    }

    @Override
    public void remove(Alarma alarma) {
        alarmas.removeIf(a -> a.getId() == alarma.getId());
    }

    @Override
    public Alarma getById(int id) {
        return alarmas.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Alarma> getAll() {
        return new ArrayList<>(alarmas); // Retornamos una copia para encapsular la lista original
    }
}