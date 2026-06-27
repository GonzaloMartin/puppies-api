package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.dto.FichaMedicaDTO;

public class ExportadorPDF implements Exportador {

    private final String tamanio;

    public ExportadorPDF(String tamanio) { this.tamanio = tamanio; }

    @Override
    public void exportar(FichaMedicaDTO dto) {
        System.out.println("[ExportadorPDF - " + tamanio + "] Exportando ficha de: "
                + dto.getNombreAnimal());
        System.out.println("  → " + dto.resumen());
    }
}
