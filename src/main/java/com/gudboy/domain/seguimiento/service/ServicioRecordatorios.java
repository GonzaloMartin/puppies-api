package com.gudboy.domain.seguimiento.service;

import com.gudboy.domain.seguimiento.model.Visita;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
public class ServicioRecordatorios {
    private int diasPreviosConfigurable;

    public ServicioRecordatorios(int diasPreviosConfigurable) {
        this.diasPreviosConfigurable = diasPreviosConfigurable;
    }

    public void setDiasPreviosConfigurable(int n) {
        this.diasPreviosConfigurable = n;
    }

    public int getDiasPreviosConfigurable() {
        return diasPreviosConfigurable;
    }

    public void evaluarVisita(Visita visita, LocalDate fechaActual) {
        if (visita.correspondeRecordatorio(fechaActual, diasPreviosConfigurable)) {
            visita.notificarRecordatorio();
        }
    }

    public void evaluarVisitas(List<Visita> visitas, LocalDate fechaActual) {
        for (Visita visita : visitas) {
            evaluarVisita(visita, fechaActual);
        }
    }
}
