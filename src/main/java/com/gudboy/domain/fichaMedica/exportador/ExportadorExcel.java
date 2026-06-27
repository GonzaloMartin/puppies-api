package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.dto.FichaMedicaDTO;

public class ExportadorExcel implements Exportador {

    private final String nombreHoja;

    public ExportadorExcel(String nombreHoja) { this.nombreHoja = nombreHoja; }

    @Override
    public void exportar(FichaMedicaDTO dto) {
        System.out.println("[ExportadorExcel - hoja: " + nombreHoja + "] Exportando ficha de: "
                + dto.getNombreAnimal());
        System.out.println("  → " + dto.resumen());
    }
}
