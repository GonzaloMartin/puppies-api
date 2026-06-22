package com.gudboy.domain.alarma.observer;

import com.gudboy.domain.alarma.model.Alarma;

public interface IAlarmaObserver {
    void actualizarEstado(Alarma alarma);
}