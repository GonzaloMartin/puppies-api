package com.gudboy.domain.alarma;

import com.gudboy.domain.Usuario.Veterinario;

import java.util.UUID;

public interface IHistorialClinicoService {
    void registrarAtencion(UUID idAnimal, String detalle, Veterinario veterinario);
}