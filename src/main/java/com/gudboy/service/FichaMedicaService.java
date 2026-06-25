package com.gudboy.service;

import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.alarma.IHistorialClinicoService;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.Exportador;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.repository.IFichaMedicaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FichaMedicaService implements IHistorialClinicoService {

    private final IFichaMedicaRepository repository;

    public FichaMedicaService(IFichaMedicaRepository repository) {
        this.repository = repository;
    }

    public FichaMedica crearFicha(Animal animal) {
        FichaMedica ficha = new FichaMedica(animal);
        repository.guardar(ficha);
        return ficha;
    }

    public void actualizarDatos(UUID id, double peso, float altura, int edad) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.actualizarDatos(peso, altura, edad);
        repository.actualizar(ficha);
    }

    public void exportarFicha(UUID id, Exportador estrategia) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.exportar(estrategia);
    }

    public void agregarTratamiento(UUID id, Tratamiento tratamiento) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.agregarTratamiento(tratamiento);
        repository.actualizar(ficha);
    }

    public void agregarComentarioMedico(UUID id, ComentarioMedico comentario) {
        FichaMedica ficha = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ficha no encontrada: " + id));
        ficha.agregarComentarioMedico(comentario);
        repository.actualizar(ficha);
    }

    public Optional<FichaMedica> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    public List<FichaMedica> listarTodas() {
        return repository.listarTodas();
    }

    @Override
    public void registrarAtencion(UUID idAnimal, String detalle, Veterinario veterinario) {
        // 1. Buscar la Ficha Médica correspondiente a ese animal en la base de datos
        FichaMedica ficha = repository.getByAnimalId(idAnimal);

        if (ficha != null) {
            // 2. Crear el objeto ComentarioMedico
            // (Asumo este constructor basado en las buenas prácticas, ajustalo a la clase real de tu compañero)
            ComentarioMedico nuevoComentario = new ComentarioMedico(
                    veterinario,
                    detalle,
                    LocalDateTime.now()
            );

            // 3. Inyectar el comentario en el historial interno de la Ficha Médica
            ficha.agregarComentarioMedico(nuevoComentario);

            // 4. Persistir los cambios en la base de datos
            repository.update(ficha);
        } else {
            System.err.println("Error: No se encontró una ficha médica para el animal con ID: " + idAnimal);
        }
    }

    @Override
    public void finalizarTratamientosActivos(UUID idAnimal, List<TipoTratamiento> accionesFinalizadas) {
        FichaMedica ficha = repository.getByAnimalId(idAnimal);
        if (ficha != null) {
            List<Tratamiento> tratamientos = ficha.getHistorial().getListaTratamiento();
            boolean actualizados = false;
            
            for (Tratamiento t : tratamientos) {
                if (accionesFinalizadas != null && accionesFinalizadas.contains(t.getTipoTratamientoEnum())) {
                    // El State Pattern se encarga de cambiar el estado internamente a Finalizado
                    t.finalizarTratamiento();
                    actualizados = true;
                }
            }
            
            if (actualizados) {
                repository.update(ficha);
            }
        }
    }
}
