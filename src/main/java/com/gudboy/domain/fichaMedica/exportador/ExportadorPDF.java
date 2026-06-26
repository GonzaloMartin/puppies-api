package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

public class ExportadorPDF implements Exportador {
    private final String tamanio;

    public ExportadorPDF(String tamanio) { this.tamanio = tamanio; }

    @Override
    public void exportar(FichaMedica ficha) {
        System.out.println("[ExportadorPDF - " + tamanio + "] Exportando ficha de: "
                + ficha.getAnimal().getNombre());
        System.out.println("  → " + ficha.obtenerDatosTecnicos());
    }
}
