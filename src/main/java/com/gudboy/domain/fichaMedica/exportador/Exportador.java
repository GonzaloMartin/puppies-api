package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.domain.fichaMedica.model.FichaMedica;

/** Strategy de exportación de ficha médica. */
public interface Exportador {
    void exportar(FichaMedica ficha);
}
