package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AlarmaRepositoryEnMemoria implements IAlarmaRepository {

    private final Map<Integer, Alarma> store = new LinkedHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public void add(Alarma alarma) {
        if (alarma.getId() == 0) alarma.setId(counter.getAndIncrement());
        store.put(alarma.getId(), alarma);
    }

    @Override
    public void remove(Alarma alarma) {
        store.remove(alarma.getId());
    }

    @Override
    public Alarma getById(int id) {
        return store.get(id);
    }

    @Override
    public List<Alarma> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Alarma alarma) {
        store.put(alarma.getId(), alarma);
    }
}
