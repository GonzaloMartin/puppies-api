package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

public class ExportadorExcel implements Exportador {
    private final String nombreHoja;

    public ExportadorExcel(String nombreHoja) { this.nombreHoja = nombreHoja; }

    @Override
    public void exportar(FichaMedica ficha) {
        System.out.println("[ExportadorExcel - hoja: " + nombreHoja + "] Exportando ficha de: "
                + ficha.getAnimal().getNombre());
        System.out.println("  → " + ficha.obtenerDatosTecnicos());
    }
}
