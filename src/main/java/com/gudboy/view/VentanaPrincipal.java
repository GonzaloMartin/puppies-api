package com.gudboy.view;

import com.gudboy.controller.AdopcionController;
import com.gudboy.controller.AlarmaController;
import com.gudboy.controller.AnimalController;
import com.gudboy.controller.UsuarioController;
import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.alarma.model.Alarma;
import com.gudboy.domain.tratamiento.TipoTratamiento;


import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final AnimalController animalController;
    private final UsuarioController usuarioController;
    private final AdopcionController adopcionController;
    private final AlarmaController alarmaController;

    private final DefaultListModel<Animal> animalListModel = new DefaultListModel<>();
    private final DefaultListModel<Visitador> visitadorListModel = new DefaultListModel<>();
    private final DefaultListModel<Veterinario> veterinarioListModel = new DefaultListModel<>();
    private final DefaultListModel<Alarma> alarmaListModel = new DefaultListModel<>();

    public VentanaPrincipal(AnimalController animalController, UsuarioController usuarioController,
                             AdopcionController adopcionController,AlarmaController alarmaController) {
        super("Puppies - Refugio de animales");
        this.animalController = animalController;
        this.usuarioController = usuarioController;
        this.adopcionController = adopcionController;
        this.alarmaController = alarmaController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen estético para que no se pegue a los bordes

        JButton btnCrearAnimal = new JButton("Crear Animal");
        JButton btnCrearVisitador = new JButton("Crear Visitador");
        JButton btnCrearVeterinario = new JButton("Crear Veterinario");
        JButton btnCrearAdopcion = new JButton("Crear Adopción");
        JButton btnCambiarEstadoSalud = new JButton("Cambiar Estado de Salud");
        JButton btnCrearAlarma = new JButton("Crear Alarma");
        JButton btnCompletarAlarma = new JButton("Completar Alarma");

        btnCrearAnimal.addActionListener(e -> mostrarDialogoCrearAnimal());
        btnCrearVisitador.addActionListener(e -> mostrarDialogoCrearVisitador());
        btnCrearVeterinario.addActionListener(e -> mostrarDialogoCrearVeterinario());
        btnCrearAdopcion.addActionListener(e -> mostrarDialogoCrearAdopcion());
        btnCambiarEstadoSalud.addActionListener(e -> mostrarDialogoCambiarEstadoSalud());
        btnCrearAlarma.addActionListener(e -> mostrarDialogoCrearAlarma());
        btnCompletarAlarma.addActionListener(e -> mostrarDialogoCompletarAlarma());

        panelBotones.add(btnCrearAnimal);
        panelBotones.add(btnCrearVisitador);
        panelBotones.add(btnCrearVeterinario);
        panelBotones.add(btnCrearAdopcion);
        panelBotones.add(btnCambiarEstadoSalud);
        panelBotones.add(btnCrearAlarma);
        panelBotones.add(btnCompletarAlarma);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Animales", new JScrollPane(new JList<>(animalListModel)));
        tabs.addTab("Visitadores", new JScrollPane(new JList<>(visitadorListModel)));
        tabs.addTab("Veterinarios", new JScrollPane(new JList<>(veterinarioListModel)));
        tabs.addTab("Alarmas", new JScrollPane(new JList<>(alarmaListModel)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);

        setContentPane(panel);
        refrescarListas();
    }

    private void iniciarVerificadorAlarmas() {
        Timer timer = new Timer(60000, e -> alarmaController.verificarEstadoAlarmas());
        timer.setInitialDelay(5000); // Primera verificación a los 5 segundos de abrir
        timer.start();
    }

    private void refrescarListas() {
        animalListModel.clear();
        animalController.listarAnimales().forEach(animalListModel::addElement);

        visitadorListModel.clear();
        usuarioController.listarVisitadores().forEach(visitadorListModel::addElement);

        veterinarioListModel.clear();
        usuarioController.listarVeterinarios().forEach(veterinarioListModel::addElement);
    }

    private void mostrarDialogoCrearAnimal() {
        JTextField nombreField = new JTextField();
        JTextField especieField = new JTextField();
        JTextField alturaField = new JTextField();
        JTextField pesoField = new JTextField();
        JTextField edadField = new JTextField();
        JTextField condicionMedicaField = new JTextField();
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
        form.add(condicionMedicaField);
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
            String condicionMedica = condicionMedicaField.getText().trim();

            if (nombre.isEmpty() || especie.isEmpty()) {
                throw new IllegalArgumentException("Nombre y especie son obligatorios.");
            }

            FabricaAnimal fabrica = "Doméstico".equals(tipoCombo.getSelectedItem())
                    ? new FabricaAnimalDomestico()
                    : new FabricaAnimalSalvaje();

            Animal animal = animalController.registrarAnimal(fabrica, nombre, especie, altura, peso, edad, condicionMedica);
            refrescarListas();
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
            refrescarListas();
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
            refrescarListas();
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
        refrescarListas();
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
            refrescarListas();
            JOptionPane.showMessageDialog(this, "Adopción registrada con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        }

    private void mostrarDialogoCrearAlarma() {
        JTextField tituloField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField frecuenciaField = new JTextField();
        JTextField fechaField = new JTextField("yyyy-MM-dd HH:mm");
        JComboBox<TipoTratamiento> tipoCombo = new JComboBox<>(TipoTratamiento.values());

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Título:"));
        form.add(tituloField);
        form.add(new JLabel("Descripción:"));
        form.add(descripcionField);
        form.add(new JLabel("Frecuencia (Días):"));
        form.add(frecuenciaField);
        form.add(new JLabel("Fecha de Disparo:"));
        form.add(fechaField);
        form.add(new JLabel("Tipo Tratamiento:"));
        form.add(tipoCombo);

        int resultado = JOptionPane.showConfirmDialog(this, form, "Crear Alarma",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            String titulo = tituloField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            int frecuencia = Integer.parseInt(frecuenciaField.getText().trim());
            LocalDateTime fechaDisparo = LocalDateTime.parse(fechaField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            TipoTratamiento tipo = (TipoTratamiento) tipoCombo.getSelectedItem();

            if (titulo.isEmpty()) {
                throw new IllegalArgumentException("El título es obligatorio.");
            }

            Alarma nuevaAlarma = new Alarma(0, titulo, descripcion, frecuencia, fechaDisparo, tipo);
            alarmaController.create(nuevaAlarma);

            refrescarListas();
            JOptionPane.showMessageDialog(this, "Alarma creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La frecuencia debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa yyyy-MM-dd HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        private void mostrarDialogoCompletarAlarma() {
            // Filtrar solo las alarmas que no están completadas
            List<Alarma> alarmasActivas = alarmaController.getAll().stream()
                    .filter(a -> !a.isCompletada())
                    .toList();

            if (alarmasActivas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay alarmas pendientes por completar.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JComboBox<Alarma> alarmaCombo = new JComboBox<>(alarmasActivas.toArray(new Alarma[0]));

            JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
            form.add(new JLabel("Seleccionar Alarma:"));
            form.add(alarmaCombo);

            int resultado = JOptionPane.showConfirmDialog(this, form, "Completar Alarma",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (resultado == JOptionPane.OK_OPTION) {
                Alarma alarmaSeleccionada = (Alarma) alarmaCombo.getSelectedItem();
                if (alarmaSeleccionada != null) {
                    alarmaController.marcarCompletado(alarmaSeleccionada.getId());
                    refrescarListas();
                    JOptionPane.showMessageDialog(this, "Alarma marcada como completada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }



    }

