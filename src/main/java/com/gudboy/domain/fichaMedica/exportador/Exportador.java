package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

/**
 * Strategy de exportación de la ficha médica.
 * Agregar un nuevo formato sólo requiere una nueva implementación (OCP).
 */

public interface Exportador {
    void exportar(FichaMedica ficha);
}
