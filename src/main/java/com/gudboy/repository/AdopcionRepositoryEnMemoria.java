package com.gudboy.repository;

import com.gudboy.domain.animal.model.Adopcion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class AdopcionRepositoryEnMemoria implements IAdopcionRepository {

    private final List<Adopcion> store = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public void guardar(Adopcion adopcion) {
        if (adopcion.getId() == 0) adopcion.setId(counter.getAndIncrement());
        store.add(adopcion);
    }

    @Override
    public void actualizar(Adopcion adopcion) {
        for (int i = 0; i < store.size(); i++) {
            if (store.get(i).getId() == adopcion.getId()) {
                store.set(i, adopcion);
                return;
            }
        }
    }

    @Override
    public List<Adopcion> listarTodos() {
        return new ArrayList<>(store);
    }

    @Override
    public Optional<Adopcion> buscarPorId(int id) {
        return store.stream().filter(a -> a.getId() == id).findFirst();
    }
}
