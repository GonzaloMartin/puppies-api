package com.gudboy.repository;

import com.gudboy.domain.alarma.model.Alarma;
import java.util.List;

public interface IAlarmaRepository {
    void add(Alarma alarma);
    void remove(Alarma alarma);
    Alarma getById(int id);
    List<Alarma> getAll();
    void update(Alarma alarma);
}