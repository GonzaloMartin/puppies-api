package com.gudboy;

import javax.swing.SwingUtilities;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.ComentarioController;
import com.gudboy.controller.FichaMedicaController;
import com.gudboy.controller.HistorialClinicoController;
import com.gudboy.controller.SeguimientoController;
import com.gudboy.controller.TratamientoController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.controller.VisitaController;
import com.gudboy.domain.seguimiento.service.ServicioRecordatorios;
import com.gudboy.repository.AdopcionRepositoryHibernate;
import com.gudboy.repository.AlarmaRepositoryHibernate;
import com.gudboy.repository.AnimalRepositoryHibernate;
import com.gudboy.repository.ComentarioMedicoRepository;
import com.gudboy.repository.FichaMedicaRepositoryHibernate;
import com.gudboy.repository.HistorialClinicoRepository;
import com.gudboy.repository.IAdopcionRepository;
import com.gudboy.repository.IAlarmaRepository;
import com.gudboy.repository.IAnimalRepository;
import com.gudboy.repository.IFichaMedicaRepository;
import com.gudboy.repository.ISeguimientoRepository;
import com.gudboy.repository.IUsuarioRepository;
import com.gudboy.repository.IVisitaRepository;
import com.gudboy.repository.SeguimientoRepositoryHibernate;
import com.gudboy.repository.TratamientoRepository;
import com.gudboy.repository.UsuarioRepositoryHibernate;
import com.gudboy.repository.VisitaRepositoryHibernate;
import com.gudboy.service.AdopcionService;
import com.gudboy.service.AlarmaService;
import com.gudboy.service.AnimalService;
import com.gudboy.service.ComentarioServices;
import com.gudboy.service.FichaMedicaService;
import com.gudboy.service.HistorialClinicoService;
import com.gudboy.service.SeguimientoService;
import com.gudboy.service.TratamientoService;
import com.gudboy.service.UsuarioService;
import com.gudboy.service.VisitaService;
import com.gudboy.view.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        try {
            // Nuevo Look & Feel
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // --- Repositorios ---
            IAnimalRepository animalRepository       = new AnimalRepositoryHibernate();
            IUsuarioRepository usuarioRepository     = new UsuarioRepositoryHibernate();
            IAdopcionRepository adopcionRepository   = new AdopcionRepositoryHibernate();
            IAlarmaRepository alarmaRepository       = new AlarmaRepositoryHibernate();
            IFichaMedicaRepository fichaRepo         = new FichaMedicaRepositoryHibernate(usuarioRepository);

            // Repositorios en memoria para Tratamiento, Comentario e Historial
            TratamientoRepository tratamientoRepository       = new TratamientoRepository();
            ComentarioMedicoRepository comentarioRepository   = new ComentarioMedicoRepository();
            HistorialClinicoRepository historialRepository    = new HistorialClinicoRepository();

            // --- Servicios ---
            FichaMedicaService fichaService       = new FichaMedicaService(fichaRepo);
            AnimalService animalService           = new AnimalService(animalRepository);
            UsuarioService usuarioService         = new UsuarioService(usuarioRepository);
            AdopcionService adopcionService       = new AdopcionService(adopcionRepository,animalRepository);
            AlarmaService alarmaService           = new AlarmaService(alarmaRepository, fichaService);

            TratamientoService tratamientoService     = new TratamientoService(tratamientoRepository, historialRepository);
            ComentarioServices comentarioService      = new ComentarioServices(historialRepository, comentarioRepository);
            HistorialClinicoService historialService  = new HistorialClinicoService(historialRepository);

            // Seguimiento
            ISeguimientoRepository seguimientoRepository = new SeguimientoRepositoryHibernate();
            IVisitaRepository visitaRepository           = new VisitaRepositoryHibernate();
            SeguimientoService seguimientoService        = new SeguimientoService(seguimientoRepository, fichaRepo, adopcionRepository, usuarioRepository);
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

            // Setear cantidad de días N para Notificar Recordatorio (Observer)
            ServicioRecordatorios recordatorios = new ServicioRecordatorios(7);

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
