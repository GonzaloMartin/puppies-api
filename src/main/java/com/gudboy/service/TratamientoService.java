package com.gudboy.service;

import com.gudboy.domain.historialClinico.HistorialClinico;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.repository.HistorialClinicoRepository;
import com.gudboy.repository.TratamientoRepository;

import java.util.ArrayList;
import java.util.UUID;

public class TratamientoService {

    private TratamientoRepository tratRepo;
    private HistorialClinicoRepository histCliRepo;

    public TratamientoService(TratamientoRepository tratRepo, HistorialClinicoRepository histCliRepo){
        this.tratRepo = tratRepo;
        this.histCliRepo = histCliRepo;
    }

    public Tratamiento registrarTratamiento(UUID animal, TipoTratamiento tipo){

        HistorialClinico historialClinico = histCliRepo.buscarPorAnimal(animal.getId());
        Tratamiento tratamiento = new Tratamiento(tipo);

        historialClinico.agregarTratamiento(tratamiento);

        tratRepo.guardar(tratamiento);

        return tratamiento;
    }

    public void finalizarTratamiento(UUID tratamientoId){

        Tratamiento tratamiento = tratRepo.buscarPorId(tratamientoId);
        tratamiento.finalizarTratamiento();

    }

    public void cancelarTratamiento(UUID tratamientoId){

        Tratamiento tratamiento = tratRepo.buscarPorId(tratamientoId);
        tratamiento.cancelarTratamiento();
    }

    public ArrayList<Tratamiento> buscarPorTipo(TipoTratamiento tipoTratamiento){

        ArrayList<Tratamiento> lisaTrat = new ArrayList<>();
        ArrayList<Tratamiento> lista = tratRepo.listarTodos();

        for (int i = 0; i< lista.size(); i++ ){
            if (tipoTratamiento == lista.get(i).getTipoTratamientoEnum()){
                lisaTrat.add(lista.get(i));
            }
        }
        return lisaTrat;

    }

}
