package com.gudboy;

import com.gudboy.controller.*;
import com.gudboy.repository.*;
import com.gudboy.service.*;
import com.gudboy.view.VentanaPrincipal;
import com.gudboy.domain.seguimiento.service.ServicioRecordatorios;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        try {
            // --- Repositorios ---
            IAnimalRepository animalRepository       = new AnimalRepositoryMySQL();
            IUsuarioRepository usuarioRepository     = new UsuarioRepositoryMySQL();
            IAdopcionRepository adopcionRepository   = new AdopcionRepositoryMySQL(animalRepository, usuarioRepository);
            IAlarmaRepository alarmaRepository       = new AlarmaRepositoryMySql();
            IFichaMedicaRepository fichaRepo         = new FichaMedicaRepositoryMySQL(animalRepository, usuarioRepository);

            // Repositorios en memoria para Tratamiento, Comentario e Historial
            TratamientoRepository tratamientoRepository       = new TratamientoRepository();
            ComentarioMedicoRepository comentarioRepository   = new ComentarioMedicoRepository();
            HistorialClinicoRepository historialRepository    = new HistorialClinicoRepository();

            // --- Servicios ---
            FichaMedicaService fichaService       = new FichaMedicaService(fichaRepo);
            AnimalService animalService           = new AnimalService(animalRepository);
            UsuarioService usuarioService         = new UsuarioService(usuarioRepository);
            AdopcionService adopcionService       = new AdopcionService(adopcionRepository);
            AlarmaService alarmaService           = new AlarmaService(alarmaRepository, fichaService);

            TratamientoService tratamientoService     = new TratamientoService(tratamientoRepository, historialRepository);
            ComentarioServices comentarioService      = new ComentarioServices(historialRepository, comentarioRepository);
            HistorialClinicoService historialService  = new HistorialClinicoService(historialRepository);

            // Seguimiento
            ISeguimientoRepository seguimientoRepository = new SeguimientoRepositoryMySQL(adopcionRepository, usuarioRepository);
            IVisitaRepository visitaRepository           = new VisitaRepositoryMySQL(seguimientoRepository);
            SeguimientoService seguimientoService        = new SeguimientoService(seguimientoRepository, fichaRepo);
            VisitaService visitaService                  = new VisitaService(visitaRepository, seguimientoRepository, fichaRepo);

            // --- Controllers ---
            AnimalController animalController               = new AnimalController(animalService);
            UsuarioController usuarioController             = new UsuarioController(usuarioService);
            AdopcionController adopcionController           = new AdopcionController(adopcionService);
            AlarmaController alarmaController               = new AlarmaController(alarmaService);
            FichaMedicaController fichaMedicaController     = new FichaMedicaController(fichaService);
            SeguimientoController seguimientoController     = new SeguimientoController(seguimientoService);
            VisitaController visitaController               = new VisitaController(visitaService);
            TratamientoController tratamientoController     = new TratamientoController(tratamientoService);
            ComentarioController comentarioController       = new ComentarioController(comentarioService, historialService);
            HistorialClinicoController historialController  = new HistorialClinicoController(historialService);
            ServicioRecordatorios recordatorios = new ServicioRecordatorios(2);

            SwingUtilities.invokeLater(() ->
                    new VentanaPrincipal(
                            animalController, usuarioController, adopcionController,
                            alarmaController, fichaMedicaController,
                            seguimientoController, visitaController,
                            tratamientoController, comentarioController, historialController,
                            recordatorios
                    ).setVisible(true)
            );
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Ocurrió un error al iniciar la aplicación:\n" + e.getMessage() +
                "\n\nVerifica tu conexión a la base de datos (MySQL).",
                "Error de Inicio", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
