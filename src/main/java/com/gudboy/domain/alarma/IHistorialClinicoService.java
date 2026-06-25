package com.gudboy.domain.alarma;

import com.gudboy.domain.Usuario.Veterinario;
import java.util.List;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import java.util.UUID;

public interface IHistorialClinicoService {
    void registrarAtencion(UUID idAnimal, String detalle, Veterinario veterinario);

    // NUEVO MÉTODO PARA CAMBIAR EL ESTADO REAL DEL TRATAMIENTO
    void finalizarTratamientosActivos(UUID idAnimal, List<TipoTratamiento> accionesFinalizadas);
}
