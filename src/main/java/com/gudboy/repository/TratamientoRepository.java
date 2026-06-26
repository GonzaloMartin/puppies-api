package com.gudboy.repository;

import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;

import java.util.ArrayList;
import java.util.UUID;

public class TratamientoRepository implements IRepositoryHistTratComen<Tratamiento> {


    @Override
    public void guardar(Tratamiento entidad) {

    }

    @Override
    public void actualizar(UUID id) {

    }

    @Override
    public void eliminar(UUID id) {

    }

    @Override
    public Tratamiento buscarPorId(UUID id) {
        return null;
    }

    @Override
    public ArrayList<Tratamiento> listarTodos() {
        return null;
    }
}
