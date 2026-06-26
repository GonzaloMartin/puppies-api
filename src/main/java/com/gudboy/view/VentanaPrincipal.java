package com.gudboy.view;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.FichaMedicaController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.alarma.observer.PushNotificationObserver;
import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.alarma.observer.IAlarmaObserver;
import com.gudboy.domain.fichaMedica.exportador.ExportadorExcel;
import com.gudboy.domain.fichaMedica.exportador.ExportadorPDF;
import com.gudboy.domain.fichaMedica.model.FichaMedica;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class VentanaPrincipal extends JFrame implements  IAlarmaObserver {

    private final AnimalController animalController;
    private final UsuarioController usuarioController;
    private final AdopcionController adopcionController;
    private final AlarmaController alarmaController;
    private final FichaMedicaController fichaMedicaController;

    private final DefaultListModel<Animal> animalListModel = new DefaultListModel<>();
    private final DefaultListModel<Visitador> visitadorListModel = new DefaultListModel<>();
    private final DefaultListModel<Veterinario> veterinarioListModel = new DefaultListModel<>();
    private final DefaultListModel<Alarma> alarmaListModel = new DefaultListModel<>();

    public VentanaPrincipal(AnimalController animalController, UsuarioController usuarioController,
                            AdopcionController adopcionController, AlarmaController alarmaController,
                            FichaMedicaController fichaMedicaController) {
        super("Puppies - Refugio de animales");
        this.animalController = animalController;
        this.usuarioController = usuarioController;
        this.adopcionController = adopcionController;
        this.alarmaController = alarmaController;
        this.fichaMedicaController = fichaMedicaController;
        this.alarmaController.suscribirVista(this);


        // 2. NUEVO: Instancias el Observer de notificaciones y lo suscribes
        PushNotificationObserver pushObserver = new PushNotificationObserver(usuarioController);
        this.alarmaController.suscribirVista(pushObserver);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel(new GridLayout(3, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen estético para que no se pegue a los bordes

        JButton btnCrearAnimal = new JButton("Crear Animal");
        JButton btnCrearVisitador = new JButton("Crear Visitador");
        JButton btnCrearVeterinario = new JButton("Crear Veterinario");
        JButton btnCrearAdopcion = new JButton("Crear Adopción");
        JButton btnCambiarEstadoSalud = new JButton("Cambiar Estado de Salud");
        JButton btnCrearAlarma = new JButton("Crear Alarma");
        JButton btnModificarAlarma = new JButton("Modificar Alarma");
        JButton btnCompletarAlarma = new JButton("Completar Alarma");
        JButton btnCrearFicha = new JButton("Crear Ficha Médica");
        JButton btnExportarFicha = new JButton("Exportar Ficha Médica");

        btnCrearAnimal.addActionListener(e -> mostrarDialogoCrearAnimal());
        btnCrearVisitador.addActionListener(e -> mostrarDialogoCrearVisitador());
        btnCrearVeterinario.addActionListener(e -> mostrarDialogoCrearVeterinario());
        btnCrearAdopcion.addActionListener(e -> mostrarDialogoCrearAdopcion());
        btnCambiarEstadoSalud.addActionListener(e -> mostrarDialogoCambiarEstadoSalud());
        btnCrearAlarma.addActionListener(e -> mostrarDialogoCrearAlarma());
        btnModificarAlarma.addActionListener(e -> mostrarDialogoModificarAlarma());
        btnCompletarAlarma.addActionListener(e -> mostrarDialogoCompletarAlarma());
        btnCrearFicha.addActionListener(e -> mostrarDialogoCrearFichaMedica());
        btnExportarFicha.addActionListener(e -> mostrarDialogoExportarFichaMedica());

        panelBotones.add(btnCrearAnimal);
        panelBotones.add(btnCrearVisitador);
        panelBotones.add(btnCrearVeterinario);
        panelBotones.add(btnCrearAdopcion);
        panelBotones.add(btnCambiarEstadoSalud);
        panelBotones.add(btnCrearAlarma);
        panelBotones.add(btnModificarAlarma);
        panelBotones.add(btnCompletarAlarma);
        panelBotones.add(btnCrearFicha);
        panelBotones.add(btnExportarFicha);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Animales", new JScrollPane(new JList<>(animalListModel)));
        tabs.addTab("Visitadores", new JScrollPane(new JList<>(visitadorListModel)));
        tabs.addTab("Veterinarios", new JScrollPane(new JList<>(veterinarioListModel)));
        tabs.addTab("Alarmas", new JScrollPane(new JList<>(alarmaListModel)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);

        setContentPane(panel);


        refrescarListasEstaticas();
        refrescarListaAlarmas();
        iniciarVerificadorAlarmas();
    }

    //@Override
    public void actualizarEstado(Alarma alarma) {
        // Aseguramos que la actualización de la UI ocurra en el hilo de Swing (EDT)
        SwingUtilities.invokeLater(this::refrescarListaAlarmas);
    }


    private void iniciarVerificadorAlarmas() {
        Timer timer = new Timer(60000, e -> alarmaController.verificarEstadoAlarmas());
        timer.setInitialDelay(5000); // Primera verificación a los 5 segundos de abrir
        timer.start();
    }


    private void refrescarListasEstaticas() {
        animalListModel.clear();
        animalController.listarAnimales().forEach(animalListModel::addElement);

        visitadorListModel.clear();
        usuarioController.listarVisitadores().forEach(visitadorListModel::addElement);

        veterinarioListModel.clear();
        usuarioController.listarVeterinarios().forEach(veterinarioListModel::addElement);
    }

    // --- CAMBIO: Método específico para refrescar las alarmas (usado por el Observer) ---
    private void refrescarListaAlarmas() {
        alarmaListModel.clear();
        alarmaController.getAll().forEach(alarmaListModel::addElement);
    }

    private void mostrarDialogoCrearAnimal() {
        JTextField nombreField = new JTextField();
        JTextField especieField = new JTextField();
        JTextField alturaField = new JTextField();
        JTextField pesoField = new JTextField();
        JTextField edadField = new JTextField();
        JComboBox<String> condicionCombo = new JComboBox<>(new String[]{"Saludable", "En tratamiento"});
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Doméstico", "Salvaje"});

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(nombreField);
        form.add(new JLabel("Especie:"));
        form.add(especieField);
        form.add(new JLabel("Altura (m):"));
        form.add(alturaField);
        form.add(new JLabel("Peso (kg):"));
        form.add(pesoField);
        form.add(new JLabel("Edad:"));
        form.add(edadField);
        form.add(new JLabel("Condición médica:"));
        form.add(condicionCombo);
        form.add(new JLabel("Tipo:"));
        form.add(tipoCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Animal",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String nombre = nombreField.getText().trim();
            String especie = especieField.getText().trim();
            double altura = Double.parseDouble(alturaField.getText().trim());
            double peso = Double.parseDouble(pesoField.getText().trim());
            int edad = Integer.parseInt(edadField.getText().trim());
            String condicionMedica = (String) condicionCombo.getSelectedItem();

            if (nombre.isEmpty() || especie.isEmpty()) {
                throw new IllegalArgumentException("Nombre y especie son obligatorios.");
            }

            FabricaAnimal fabrica = "Doméstico".equals(tipoCombo.getSelectedItem())
                    ? new FabricaAnimalDomestico()
                    : new FabricaAnimalSalvaje();

            Animal animal = animalController.registrarAnimal(fabrica, nombre, especie, altura, peso, edad, condicionMedica);
            if ("En tratamiento".equals(condicionMedica)) {
                animalController.ponerEnTratamiento(animal);
            }
            refrescarListasEstaticas();
            JOptionPane.showMessageDialog(this, "Animal creado:\n" + animal,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Altura, peso y edad deben ser números válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCrearVisitador() {
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField telefonoField = new JTextField();
        JComboBox<EstadoCivil> estadoCivilCombo = new JComboBox<>(EstadoCivil.values());
        JComboBox<Ocupacion> ocupacionCombo = new JComboBox<>(Ocupacion.values());
        JCheckBox otrasMascotasCheck = new JCheckBox();
        JTextField motivoAdopcionField = new JTextField();
        JTextField animalesInteresField = new JTextField();

        JPanel form = new JPanel(new GridLayout(9, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(nombreField);
        form.add(new JLabel("Apellido:"));
        form.add(apellidoField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Teléfono:"));
        form.add(telefonoField);
        form.add(new JLabel("Estado civil:"));
        form.add(estadoCivilCombo);
        form.add(new JLabel("Ocupación:"));
        form.add(ocupacionCombo);
        form.add(new JLabel("¿Tiene otras mascotas?"));
        form.add(otrasMascotasCheck);
        form.add(new JLabel("Motivo de adopción:"));
        form.add(motivoAdopcionField);
        form.add(new JLabel("Animales de interés:"));
        form.add(animalesInteresField);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Visitador",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String email = emailField.getText().trim();
            String telefono = telefonoField.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                throw new IllegalArgumentException("Nombre y apellido son obligatorios.");
            }

            Visitador visitador = usuarioController.registrarVisitador(nombre, apellido, email, telefono,
                    (EstadoCivil) estadoCivilCombo.getSelectedItem(), (Ocupacion) ocupacionCombo.getSelectedItem(),
                    motivoAdopcionField.getText().trim(), animalesInteresField.getText().trim(),
                    otrasMascotasCheck.isSelected());
            refrescarListasEstaticas();
            JOptionPane.showMessageDialog(this, "Visitador creado:\n" + visitador,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCrearVeterinario() {
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField matriculaField = new JTextField();
        JTextField especialidadField = new JTextField();

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(nombreField);
        form.add(new JLabel("Apellido:"));
        form.add(apellidoField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Teléfono:"));
        form.add(telefonoField);
        form.add(new JLabel("Matrícula profesional:"));
        form.add(matriculaField);
        form.add(new JLabel("Especialidad:"));
        form.add(especialidadField);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Veterinario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String email = emailField.getText().trim();
            String telefono = telefonoField.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                throw new IllegalArgumentException("Nombre y apellido son obligatorios.");
            }

            int matricula = Integer.parseInt(matriculaField.getText().trim());
            String especialidad = especialidadField.getText().trim();
            Veterinario veterinario = usuarioController.registrarVeterinario(nombre, apellido, email, telefono,
                    matricula, especialidad);
            refrescarListasEstaticas();
            JOptionPane.showMessageDialog(this, "Veterinario creado:\n" + veterinario,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La matrícula profesional debe ser un número entero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCambiarEstadoSalud() {
        List<Animal> animales = animalController.listarAnimales();
        if (animales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todavía no hay animales registrados.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Animal> animalCombo = new JComboBox<>(animales.toArray(new Animal[0]));
        JComboBox<String> accionCombo = new JComboBox<>(
                new String[]{"Poner en tratamiento", "Disponibilizar (marcar sano)"});

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Animal:"));
        form.add(animalCombo);
        form.add(new JLabel("Acción:"));
        form.add(accionCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Cambiar Estado de Salud",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        Animal animal = (Animal) animalCombo.getSelectedItem();
        if ("Poner en tratamiento".equals(accionCombo.getSelectedItem())) {
            animalController.ponerEnTratamiento(animal);
        } else {
            animalController.disponibilizar(animal);
        }
        refrescarListasEstaticas();
        JOptionPane.showMessageDialog(this, "Estado de salud actualizado:\n" + animal,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarDialogoCrearAdopcion() {
        List<AnimalDomestico> animales = animalController.listarAnimales().stream()
                .filter(Animal::esAdoptable)
                .map(AnimalDomestico.class::cast)
                .toList();
        List<Visitador> visitadores = usuarioController.listarVisitadores();
        List<Veterinario> veterinarios = usuarioController.listarVeterinarios();

        if (animales.isEmpty() || visitadores.isEmpty() || veterinarios.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Para crear una adopción primero necesitás al menos un animal adoptable,\n"
                            + "un Visitador y un Veterinario.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JComboBox<AnimalDomestico> animal1Combo = new JComboBox<>(animales.toArray(new AnimalDomestico[0]));
        JComboBox<AnimalDomestico> animal2Combo = new JComboBox<>(animales.toArray(new AnimalDomestico[0]));
        animal2Combo.insertItemAt(null, 0);
        animal2Combo.setSelectedIndex(0);
        animal2Combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, value == null ? "Ninguno" : value, index,
                        isSelected, cellHasFocus);
            }
        });

        JComboBox<Visitador> adoptanteCombo = new JComboBox<>(visitadores.toArray(new Visitador[0]));
        JComboBox<Veterinario> responsableCombo = new JComboBox<>(veterinarios.toArray(new Veterinario[0]));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Animal 1:"));
        form.add(animal1Combo);
        form.add(new JLabel("Animal 2 (opcional):"));
        form.add(animal2Combo);
        form.add(new JLabel("Adoptante (Visitador):"));
        form.add(adoptanteCombo);
        form.add(new JLabel("Responsable (Veterinario):"));
        form.add(responsableCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Adopción",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        AnimalDomestico animal1 = (AnimalDomestico) animal1Combo.getSelectedItem();
        AnimalDomestico animal2 = (AnimalDomestico) animal2Combo.getSelectedItem();
        Visitador adoptante = (Visitador) adoptanteCombo.getSelectedItem();
        Veterinario responsable = (Veterinario) responsableCombo.getSelectedItem();

        if (animal2 != null && animal2 == animal1) {
            JOptionPane.showMessageDialog(this, "Animal 1 y Animal 2 no pueden ser el mismo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            adopcionController.registrarAdopcion(animal1, animal2, adoptante, responsable);
            refrescarListasEstaticas();
            JOptionPane.showMessageDialog(this, "Adopción registrada con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCrearAlarma() {
        JTextField tituloField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField frecuenciaField = new JTextField("1"); // Default 1 día

        // 1. Configuración del Selector de Fecha (JSpinner) - Restaurado
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 150);
        Date latestDate = calendar.getTime();
        SpinnerDateModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
        JSpinner datetimeSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datetimeSpinner, "dd/MM/yyyy HH:mm:ss");
        datetimeSpinner.setEditor(dateEditor);

        // 2. Selector de Animal
        List<Animal> animales = animalController.listarAnimales();
        if (animales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay animales registrados. Debe crear un animal antes de asignar una alarma.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JComboBox<Animal> animalCombo = new JComboBox<>(animales.toArray(new Animal[0]));

        // 3. Selector múltiple de Acciones (Tratamientos)
        JList<TipoTratamiento> accionesList = new JList<>(TipoTratamiento.values());
        accionesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollAcciones = new JScrollPane(accionesList);
        scrollAcciones.setPreferredSize(new Dimension(150, 60));

        // 4. Armado del Formulario
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Animal a tratar:"));
        form.add(animalCombo);
        form.add(new JLabel("Título:"));
        form.add(tituloField);
        form.add(new JLabel("Descripción:"));
        form.add(descripcionField);
        form.add(new JLabel("Frecuencia (Días):"));
        form.add(frecuenciaField);
        form.add(new JLabel("Fecha Disparo:"));
        form.add(datetimeSpinner);
        form.add(new JLabel("Acciones (Ctrl/Cmd para múltiple):"));
        form.add(scrollAcciones);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Alarma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            String titulo = tituloField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            int frecuencia = Integer.parseInt(frecuenciaField.getText().trim());

            Animal animalSeleccionado = (Animal) animalCombo.getSelectedItem();
            List<TipoTratamiento> accionesSeleccionadas = accionesList.getSelectedValuesList();

            if (titulo.isEmpty()) {
                throw new IllegalArgumentException("El título es obligatorio.");
            }

            if (animalSeleccionado == null || accionesSeleccionadas.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar un animal y al menos una acción.");
            }

            // --- Convertir java.util.Date (del Spinner) a LocalDateTime ---
            Date spinnerDate = (Date) datetimeSpinner.getValue();
            LocalDateTime fechaDisparo = spinnerDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Usamos el constructor actualizado (id=0 autoincremental, idAnimal, titulo, desc, frec, fecha, lista_acciones)
            Alarma nuevaAlarma = new Alarma(0, animalSeleccionado.getId(), titulo, descripcion, frecuencia, fechaDisparo, accionesSeleccionadas);
            alarmaController.create(nuevaAlarma);

            JOptionPane.showMessageDialog(this, "Alarma creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La frecuencia debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCompletarAlarma() {
        // 1. Obtener alarmas activas
        List<Alarma> alarmasActivas = alarmaController.getAll().stream()
                .filter(a -> !a.isCompletada() && !a.getEstado().equals("FINALIZADO"))
                .toList();

        if (alarmasActivas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay alarmas pendientes por atender.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Obtener veterinarios registrados para la trazabilidad
        List<Veterinario> veterinarios = usuarioController.listarVeterinarios();
        if (veterinarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay veterinarios registrados. Es obligatorio registrar al menos un veterinario para atender una alarma.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Configurar componentes de la UI
        JComboBox<Alarma> alarmaCombo = new JComboBox<>(alarmasActivas.toArray(new Alarma[0]));
        JComboBox<Veterinario> vetCombo = new JComboBox<>(veterinarios.toArray(new Veterinario[0]));
        JTextField comentarioField = new JTextField();
        JCheckBox finalizadoCheck = new JCheckBox("Marcar tratamiento como FINALIZADO");

        // 4. Armar el formulario
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5)); // Modificado a 4 filas
        form.add(new JLabel("Seleccionar Alarma:"));
        form.add(alarmaCombo);
        form.add(new JLabel("Atendido por (Veterinario):"));
        form.add(vetCombo);
        form.add(new JLabel("Comentario/Registro:"));
        form.add(comentarioField);
        form.add(new JLabel("Estado del tratamiento:"));
        form.add(finalizadoCheck);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Atender Alarma",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            Alarma alarmaSeleccionada = (Alarma) alarmaCombo.getSelectedItem();
            Veterinario vetSeleccionado = (Veterinario) vetCombo.getSelectedItem();

            if (alarmaSeleccionada != null && vetSeleccionado != null) {
                String comentario = comentarioField.getText().trim();
                boolean finalizado = finalizadoCheck.isSelected();

                // Llamamos al controller inyectando al veterinario responsable
                alarmaController.atenderAlarma(alarmaSeleccionada.getId(), comentario, finalizado, vetSeleccionado);

                JOptionPane.showMessageDialog(this, "Alarma atendida y registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una alarma y un veterinario válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoCrearFichaMedica() {
        List<Animal> animales = animalController.listarAnimales();
        if (animales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay animales registrados.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Animal> animalCombo = new JComboBox<>(animales.toArray(new Animal[0]));

        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        form.add(new JLabel("Animal:"));
        form.add(animalCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Ficha Médica",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) return;

        Animal animal = (Animal) animalCombo.getSelectedItem();
        try {
            fichaMedicaController.crearFicha(animal);
            JOptionPane.showMessageDialog(this,
                    "Ficha médica creada para: " + animal.getNombre(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear ficha: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoExportarFichaMedica() {
        List<Animal> animales = animalController.listarAnimales();
        if (animales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay animales registrados.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Animal> animalCombo = new JComboBox<>(animales.toArray(new Animal[0]));
        JComboBox<String> formatoCombo = new JComboBox<>(new String[]{"PDF", "Excel"});

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Animal:"));
        form.add(animalCombo);
        form.add(new JLabel("Formato:"));
        form.add(formatoCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Exportar Ficha Médica",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) return;

        Animal animal = (Animal) animalCombo.getSelectedItem();
        try {
            FichaMedica ficha = fichaMedicaController.listarTodas().stream()
                    .filter(f -> f.getAnimal().getId().equals(animal.getId()))
                    .findFirst()
                    .orElse(null);

            if (ficha == null) {
                JOptionPane.showMessageDialog(this,
                        "El animal no tiene ficha médica. Creá una primero.",
                        "Sin ficha", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String formato = (String) formatoCombo.getSelectedItem();
            if ("PDF".equals(formato)) {
                ficha.exportar(new ExportadorPDF("A4"));
            } else {
                ficha.exportar(new ExportadorExcel(animal.getNombre()));
            }

            mostrarVentanaFicha(ficha, formato);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarVentanaFicha(FichaMedica ficha, String formato) {
        JDialog ventana = new JDialog(this, "Ficha Médica — " + formato, false);
        ventana.setSize(500, 300);
        ventana.setLocationRelativeTo(this);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setText(
            "=== FICHA MÉDICA (" + formato + ") ===\n\n" +
            ficha.obtenerDatosTecnicos() + "\n\n" +
            "Tratamientos: " + ficha.getHistorial().getListaTratamiento().size() + "\n" +
            "Comentarios médicos: " + ficha.getHistorial().getListaComentario().size()
        );

        ventana.add(new JScrollPane(area));
        ventana.setVisible(true);
    }

    private void mostrarDialogoModificarAlarma() {
        List<Alarma> alarmas = alarmaController.getAll();
        if (alarmas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay alarmas creadas para modificar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Paso 1: Elegir qué alarma modificar
        JComboBox<Alarma> seleccionCombo = new JComboBox<>(alarmas.toArray(new Alarma[0]));
        int eleccion = JOptionPane.showConfirmDialog(this, seleccionCombo, "Seleccione la alarma a modificar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (eleccion != JOptionPane.OK_OPTION) return;

        Alarma alarmaSeleccionada = (Alarma) seleccionCombo.getSelectedItem();
        if (alarmaSeleccionada == null) return;

        // Paso 2: Mostrar el formulario con los datos cargados
        JTextField tituloField = new JTextField(alarmaSeleccionada.getTitulo());
        JTextField descripcionField = new JTextField(alarmaSeleccionada.getDescripcion());
        JTextField frecuenciaField = new JTextField(String.valueOf(alarmaSeleccionada.getFrecuenciaDias()));

        // Cargar la fecha actual de la alarma en el JSpinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner datetimeSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datetimeSpinner, "dd/MM/yyyy HH:mm:ss");
        datetimeSpinner.setEditor(dateEditor);

        Date fechaOriginal = Date.from(alarmaSeleccionada.getFechaProximoDisparoOriginal().atZone(ZoneId.systemDefault()).toInstant());
        datetimeSpinner.setValue(fechaOriginal);

        JComboBox<TipoTratamiento> tipoCombo = new JComboBox<>(TipoTratamiento.values());
        tipoCombo.setSelectedItem(alarmaSeleccionada.getAcciones());

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Título:"));
        form.add(tituloField);
        form.add(new JLabel("Descripción:"));
        form.add(descripcionField);
        form.add(new JLabel("Frecuencia (Días):"));
        form.add(frecuenciaField);
        form.add(new JLabel("Fecha de Disparo:"));
        form.add(datetimeSpinner);
        form.add(new JLabel("Tipo Tratamiento:"));
        form.add(tipoCombo);

        int resultadoEdicion = JOptionPane.showConfirmDialog(this, form, "Modificar Alarma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultadoEdicion == JOptionPane.OK_OPTION) {
            try {
                // Actualizamos el objeto seleccionado
                alarmaSeleccionada.setTitulo(tituloField.getText().trim());
                alarmaSeleccionada.setDescripcion(descripcionField.getText().trim());
                alarmaSeleccionada.setFrecuenciaDias(Integer.parseInt(frecuenciaField.getText().trim()));
                alarmaSeleccionada.setAcciones(Collections.singletonList((TipoTratamiento) tipoCombo.getSelectedItem()));

                Date spinnerDate = (Date) datetimeSpinner.getValue();
                alarmaSeleccionada.setFechaProximoDisparo(spinnerDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                // Guardamos en el controlador
                alarmaController.update(alarmaSeleccionada.getId(), alarmaSeleccionada);

                // NOTA: Como la clase VentanaPrincipal es un Observer, no hace falta recargar la lista manualmente.
                // Sin embargo, debes asegurarte de que AlarmaService.actualizarAlarma llame a notificarObservadores()

                JOptionPane.showMessageDialog(this, "Alarma modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La frecuencia debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }

}