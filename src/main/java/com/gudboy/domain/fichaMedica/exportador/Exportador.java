package com.gudboy.domain.fichaMedica.exportador;

import com.gudboy.dto.FichaMedicaDTO;

/** Strategy de exportación de ficha médica. */
public interface Exportador {
    void exportar(FichaMedicaDTO dto);
}
