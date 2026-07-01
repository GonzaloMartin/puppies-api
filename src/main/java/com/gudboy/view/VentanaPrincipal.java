package com.gudboy.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
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
import com.gudboy.domain.Usuario.EstadoCivil;
import com.gudboy.domain.Usuario.Ocupacion;
import com.gudboy.domain.Usuario.Veterinario;
import com.gudboy.domain.Usuario.Visitador;
import com.gudboy.domain.animal.factory.FabricaAnimal;
import com.gudboy.domain.animal.factory.FabricaAnimalDomestico;
import com.gudboy.domain.animal.factory.FabricaAnimalSalvaje;
import com.gudboy.domain.animal.model.Adopcion;
import com.gudboy.domain.animal.model.Animal;
import com.gudboy.domain.animal.model.AnimalDomestico;
import com.gudboy.domain.comentarioMedico.ComentarioMedico;
import com.gudboy.domain.fichaMedica.exportador.ExportadorExcel;
import com.gudboy.domain.fichaMedica.exportador.ExportadorPDF;
import com.gudboy.domain.fichaMedica.model.FichaMedica;
import com.gudboy.domain.seguimiento.model.CalificacionEnum;
import com.gudboy.domain.seguimiento.model.DiaSemana;
import com.gudboy.domain.seguimiento.model.EstadoSeguimiento;
import com.gudboy.domain.seguimiento.model.PreferenciaRecordatorio;
import com.gudboy.domain.seguimiento.model.Visita;
import com.gudboy.domain.seguimiento.service.ServicioRecordatorios;
import com.gudboy.domain.tratamiento.Cancelado;
import com.gudboy.domain.tratamiento.Finalizado;
import com.gudboy.domain.tratamiento.TipoTratamiento;
import com.gudboy.domain.tratamiento.Tratamiento;
import com.gudboy.dto.AdopcionDTO;
import com.gudboy.dto.AlarmaDTO;
import com.gudboy.dto.AnimalDTO;
import com.gudboy.dto.EncuestaDTO;
import com.gudboy.dto.SeguimientoDTO;
import com.gudboy.dto.UsuarioDTO;
import com.gudboy.dto.VisitaDTO;
import com.gudboy.infrastructure.ActividadRegistry;

public class VentanaPrincipal extends JFrame {

    // Controllers
    private final AnimalController            animalCtrl;
    private final UsuarioController           usuarioCtrl;
    private final AdopcionController          adopcionCtrl;
    private final AlarmaController            alarmaCtrl;
    private final FichaMedicaController       fichaCtrl;
    private final SeguimientoController       segCtrl;
    private final VisitaController            visitaCtrl;
    private final TratamientoController       tratCtrl;
    private final ComentarioController        comenCtrl;
    private final HistorialClinicoController  histCtrl;
    private final ServicioRecordatorios       recordatorios;

    // List models
    private final DefaultListModel<Animal>         animalModel  = new DefaultListModel<>();
    private final DefaultListModel<Visitador>      visitModel   = new DefaultListModel<>();
    private final DefaultListModel<Veterinario>    vetModel     = new DefaultListModel<>();
    private final DefaultListModel<AlarmaDTO>      alarmaModel  = new DefaultListModel<>();
    private final DefaultListModel<SeguimientoDTO> segModel     = new DefaultListModel<>();
    private final DefaultListModel<VisitaDTO>      visitaModel  = new DefaultListModel<>();

    private JList<SeguimientoDTO> listSeg;
    private JList<VisitaDTO>      listVisita;
    private JLabel                lblStatus;


    public VentanaPrincipal(AnimalController animalCtrl,
                            UsuarioController usuarioCtrl,
                            AdopcionController adopcionCtrl,
                            AlarmaController alarmaCtrl,
                            FichaMedicaController fichaCtrl,
                            SeguimientoController segCtrl,
                            VisitaController visitaCtrl,
                            TratamientoController tratCtrl,
                            ComentarioController comenCtrl,
                            HistorialClinicoController histCtrl,
                            ServicioRecordatorios recordatorios) {
        super("Puppies — Refugio Gud Boy");
        this.animalCtrl   = animalCtrl;
        this.usuarioCtrl  = usuarioCtrl;
        this.adopcionCtrl = adopcionCtrl;
        this.alarmaCtrl   = alarmaCtrl;
        this.fichaCtrl    = fichaCtrl;
        this.segCtrl      = segCtrl;
        this.visitaCtrl   = visitaCtrl;
        this.tratCtrl     = tratCtrl;
        this.comenCtrl    = comenCtrl;
        this.histCtrl     = histCtrl;
        this.recordatorios = recordatorios;


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 700);
        setLocationRelativeTo(null);

        // ── Botones 4×4 ──────────────────────────────────────────────────────
        JPanel pBtns = new JPanel(new GridLayout(4, 4, 8, 8));
        pBtns.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));

        String[] labels = {
            "Crear Animal",       "Crear Visitador",     "Crear Veterinario",   "Crear Adopción",
            "Estado de Salud",    "Crear Alarma",        "Modificar Alarma",    "Atender Alarma",
            "Crear Ficha Médica", "Exportar Ficha",      "Crear Seguimiento",   "Registrar Visita",
            "Registrar Trat.",    "Cambiar Estado Trat.", "Agregar Comentario", "Ver Historial"
        };
        Runnable[] actions = {
            this::dlgCrearAnimal,       this::dlgCrearVisitador,  this::dlgCrearVeterinario, this::dlgCrearAdopcion,
            this::dlgEstadoSalud,       this::dlgCrearAlarma,     this::dlgModifAlarma,      this::dlgAtenderAlarma,
            this::dlgCrearFicha,        this::dlgExportarFicha,   this::dlgCrearSeguimiento, this::dlgRegistrarVisita,
            this::dlgRegistrarTrat,     this::dlgCambiarEstTrat,  this::dlgAgregarComentario, this::dlgVerHistorial
        };
        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            final Runnable act = actions[i];
            btn.addActionListener(e -> act.run());
            pBtns.add(btn);
        }

        // ── Panel seguimientos ────────────────────────────────────────────────
        listSeg    = new JList<>(segModel);
        listVisita = new JList<>(visitaModel);
        listSeg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listVisita.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listSeg.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SeguimientoDTO s = listSeg.getSelectedValue();
                visitaModel.clear();
                if (s != null)
                    visitaCtrl.listarPorSeguimiento(s.getId()).forEach(visitaModel::addElement);
            }
        });

        listSeg.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, sel, foc);
                if (v instanceof SeguimientoDTO s) {
                    String anim = s.getAdopcion().getAnimales().stream().map(Animal::getNombre).collect(Collectors.joining(", "));
                    lbl.setText(String.format("▸ %s %s  |  [%s]  |  %s %s-%s  |  %s",
                        s.getAdopcion().getAdoptante().getNombre(),
                        s.getAdopcion().getAdoptante().getApellido(),
                        anim, s.getDiaSemana(), s.getHorarioDesde(), s.getHorarioHasta(), s.getEstado()));
                }
                return lbl;
            }
        });

        listVisita.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, sel, foc);
                if (v instanceof VisitaDTO vi) {
                    String est;
                    if (vi.isCompletada()) {
                        est = "✓ Completada el " + vi.getFechaReal();
                    } else if (listSeg.getSelectedValue() != null && listSeg.getSelectedValue().getEstado() == EstadoSeguimiento.FINALIZADO) {
                        est = "❌ Sin efecto (Seguimiento Finalizado) — " + vi.getFechaProgramada();
                    } else {
                        est = "⏳ Pendiente — " + vi.getFechaProgramada();
                    }
                    String enc = "";
                    if (vi.isCompletada() && vi.getEncuesta() != null) {
                        var e = vi.getEncuesta();
                        enc = String.format("  [Animal: %s  Limpieza: %s  Ambiente: %s]",
                            e.getEstadoGeneralAnimal(), e.getLimpiezaLugar(), e.getAmbiente());
                    }
                    lbl.setText("  " + est + enc);
                }
                return lbl;
            }
        });

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            withTitle("Seguimientos:", new JScrollPane(listSeg)),
            withTitle("Visitas del seguimiento seleccionado (seleccioná una ⏳ para registrar resultado):",
                      new JScrollPane(listVisita)));
        split.setDividerLocation(200);

        // ── Tabs ─────────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Animales",      styledList(animalModel));
        tabs.addTab("Visitadores",   styledList(visitModel));
        tabs.addTab("Veterinarios",  styledList(vetModel));
        tabs.addTab("Alarmas",       styledList(alarmaModel));
        tabs.addTab("Seguimientos",  split);

        // Barra de estado con controles de evaluación manual
        JPanel pStatus = new JPanel(new BorderLayout());
        pStatus.setBackground(new java.awt.Color(0xf1, 0xf3, 0xf4));
        pStatus.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0xdc, 0xdf, 0xe1)));

        lblStatus = new JLabel("🟢 Sistema listo. Base de datos MySQL conectada.");
        lblStatus.setOpaque(false);
        lblStatus.setForeground(new java.awt.Color(0x3c, 0x40, 0x43));
        lblStatus.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JPanel pStatusControls = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 2));
        pStatusControls.setOpaque(false);

        JLabel lblDias = new JLabel("Días Previos:");
        lblDias.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        lblDias.setForeground(new java.awt.Color(0x3c, 0x40, 0x43));
        lblDias.setToolTipText("Seleccione N días y luego presione 'Evaluar Recordatorios'.");

        JSpinner spinnerDias = new JSpinner(new SpinnerNumberModel(recordatorios.getDiasPreviosConfigurable(), 0, 30, 1));
        spinnerDias.setPreferredSize(new java.awt.Dimension(45, 20));
        spinnerDias.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        spinnerDias.setToolTipText("Seleccione N días y luego presione 'Evaluar Recordatorios'.");
        spinnerDias.addChangeListener(ev -> {
            int val = (int) spinnerDias.getValue();
            recordatorios.setDiasPreviosConfigurable(val);
        });

        JButton btnEvaluar = new JButton("Evaluar Recordatorios");
        btnEvaluar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        btnEvaluar.setForeground(new java.awt.Color(0x1a, 0x73, 0xe8));
        btnEvaluar.setBackground(new java.awt.Color(0xf1, 0xf3, 0xf4));
        btnEvaluar.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        btnEvaluar.setFocusPainted(false);
        btnEvaluar.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btnEvaluar.addActionListener(e -> evaluarRecordatorios());

        pStatusControls.add(lblDias);
        pStatusControls.add(spinnerDias);
        pStatusControls.add(btnEvaluar);

        pStatus.add(lblStatus, BorderLayout.CENTER);
        pStatus.add(pStatusControls, BorderLayout.EAST);

        // Registro de listener para actualizaciones de actividad en tiempo real
        ActividadRegistry.registrarListener(msg -> {
            SwingUtilities.invokeLater(() -> lblStatus.setText("📡 " + msg));
        });

        JPanel root = new JPanel(new BorderLayout());
        root.add(pBtns, BorderLayout.NORTH);
        root.add(tabs,  BorderLayout.CENTER);
        root.add(pStatus, BorderLayout.SOUTH);
        setContentPane(root);

        refrescarTodo();
        iniciarTimers();
    }

    // ── Helpers UI ───────────────────────────────────────────────────────────

    private JPanel withTitle(String title, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("  " + title), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private <T> JScrollPane styledList(DefaultListModel<T> model) {
        JList<T> list = new JList<>(model);
        list.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        list.setFixedCellHeight(22);
        return new JScrollPane(list);
    }

    private DefaultListCellRenderer fichaRenderer() {
        return new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof FichaMedica fm)
                    lbl.setText(fm.getAnimal().getNombre() + " (" + fm.getAnimal().getEspecie() + " — " + fm.getAnimal().getTipoAnimal() + ")");
                return lbl;
            }
        };
    }

    // ── Observer / Timers ────────────────────────────────────────────────────

    private void iniciarTimers() {
        // Timer 1: verificar alarmas vencidas cada 60 seg
        javax.swing.Timer t1 = new javax.swing.Timer(60_000, e -> alarmaCtrl.verificarEstadoAlarmas());
        t1.setInitialDelay(5_000);
        t1.start();

        // Timer 2: ServicioRecordatorios — evalúa visitas próximas cada 24 h
        // Usa los canales configurados en cada Visita según PreferenciaRecordatorio
        javax.swing.Timer t2 = new javax.swing.Timer(86_400_000, e -> evaluarRecordatorios());
        t2.setInitialDelay(10_000); // primera evaluación a los 10 seg de abrir
        t2.start();
    }

    /**
     * Evalúa todas las visitas pendientes delegando en el controlador de seguimiento.
     */
    private void evaluarRecordatorios() {
        segCtrl.evaluarTodosLosRecordatorios(recordatorios);
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    private void refrescarTodo() {
        animalModel.clear();
        animalCtrl.listarAnimales().forEach(animalModel::addElement);
        visitModel.clear();
        usuarioCtrl.listarVisitadores().forEach(visitModel::addElement);
        vetModel.clear();
        usuarioCtrl.listarVeterinarios().forEach(vetModel::addElement);
        refrescarAlarmas();
        refrescarSeguimientos();
    }

    private void refrescarSeguimientos() {
        segModel.clear();
        visitaModel.clear();
        segCtrl.listarTodos().forEach(segModel::addElement);
    }

    private void refrescarAlarmas() {
        alarmaModel.clear();
        alarmaCtrl.getAll().forEach(alarmaModel::addElement);
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — ANIMAL
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearAnimal() {
        JTextField nombreF = new JTextField(), especieF = new JTextField(),
                   alturaF = new JTextField(), pesoF = new JTextField(), edadF = new JTextField();
        JComboBox<String> condCB = new JComboBox<>(new String[]{"Saludable", "En tratamiento"});
        JComboBox<String> tipoCB = new JComboBox<>(new String[]{"Doméstico", "Salvaje"});

        JPanel f = form(
            "Nombre:", nombreF, "Especie:", especieF,
            "Altura (m):", alturaF, "Peso (kg):", pesoF,
            "Edad:", edadF, "Condición:", condCB, "Tipo:", tipoCB);
        if (!confirm(f, "Crear Animal")) return;

        try {
            if (nombreF.getText().trim().isEmpty() || especieF.getText().trim().isEmpty())
                throw new IllegalArgumentException("Nombre y especie son obligatorios.");
            FabricaAnimal fab = "Doméstico".equals(tipoCB.getSelectedItem())
                ? new FabricaAnimalDomestico() : new FabricaAnimalSalvaje();
            AnimalDTO dto = new AnimalDTO(
                nombreF.getText().trim(), especieF.getText().trim(),
                Double.parseDouble(alturaF.getText().trim()),
                Double.parseDouble(pesoF.getText().trim()),
                Integer.parseInt(edadF.getText().trim()),
                (String) condCB.getSelectedItem());
            Animal a = animalCtrl.registrarAnimal(fab, dto);
            if ("En tratamiento".equals(condCB.getSelectedItem())) animalCtrl.ponerEnTratamiento(a);
            refrescarTodo();
            info("Animal creado:\n" + a);
        } catch (NumberFormatException ex) { error("Altura, peso y edad deben ser números."); }
          catch (Exception ex)            { error(ex.getMessage()); }
    }

    private void dlgEstadoSalud() {
        List<Animal> animales = animalCtrl.listarAnimales();
        if (animales.isEmpty()) { warn("No hay animales registrados."); return; }
        JComboBox<Animal> cb = new JComboBox<>(animales.toArray(new Animal[0]));
        JComboBox<String> ac = new JComboBox<>(new String[]{"Poner en tratamiento", "Disponibilizar (sano)"});
        if (!confirm(form("Animal:", cb, "Acción:", ac), "Cambiar Estado de Salud")) return;
        Animal a = (Animal) cb.getSelectedItem();
        if ("Poner en tratamiento".equals(ac.getSelectedItem())) animalCtrl.ponerEnTratamiento(a);
        else animalCtrl.disponibilizar(a);
        refrescarTodo();
        info("Estado actualizado:\n" + a);
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — USUARIOS
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearVisitador() {
        JTextField nF = new JTextField(), apF = new JTextField(),
                   emF = new JTextField(), telF = new JTextField(),
                   motF = new JTextField(), intF = new JTextField();
        JComboBox<EstadoCivil> ecCB = new JComboBox<>(EstadoCivil.values());
        JComboBox<Ocupacion>   ocCB = new JComboBox<>(Ocupacion.values());
        JCheckBox mascCK = new JCheckBox();
        JPanel f = form(
            "Nombre:", nF, "Apellido:", apF, "Email:", emF, "Teléfono:", telF,
            "Estado civil:", ecCB, "Ocupación:", ocCB,
            "¿Otras mascotas?", mascCK, "Motivo adopción:", motF, "Animales interés:", intF);
        if (!confirm(f, "Crear Visitador")) return;
        try {
            if (nF.getText().trim().isEmpty()) throw new IllegalArgumentException("Nombre obligatorio.");
            Visitador v = usuarioCtrl.registrarVisitador(
                nF.getText().trim(), apF.getText().trim(), emF.getText().trim(), telF.getText().trim(),
                (EstadoCivil) ecCB.getSelectedItem(), (Ocupacion) ocCB.getSelectedItem(),
                motF.getText().trim(), intF.getText().trim(), mascCK.isSelected());
            refrescarTodo(); info("Visitador creado:\n" + v);
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgCrearVeterinario() {
        JTextField nF = new JTextField(), apF = new JTextField(),
                   emF = new JTextField(), telF = new JTextField(),
                   matF = new JTextField(), espF = new JTextField();
        JPanel f = form("Nombre:", nF, "Apellido:", apF, "Email:", emF,
                        "Teléfono:", telF, "Matrícula:", matF, "Especialidad:", espF);
        if (!confirm(f, "Crear Veterinario")) return;
        try {
            if (nF.getText().trim().isEmpty()) throw new IllegalArgumentException("Nombre obligatorio.");
            Veterinario v = usuarioCtrl.registrarVeterinario(
                nF.getText().trim(), apF.getText().trim(), emF.getText().trim(), telF.getText().trim(),
                Integer.parseInt(matF.getText().trim()), espF.getText().trim());
            refrescarTodo(); info("Veterinario creado:\n" + v);
        } catch (NumberFormatException ex) { error("La matrícula debe ser un número."); }
          catch (Exception ex)            { error(ex.getMessage()); }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — ADOPCIÓN
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearAdopcion() {
        List<AnimalDomestico> anim = animalCtrl.listarAnimales().stream()
            .filter(Animal::esAdoptable).map(AnimalDomestico.class::cast).toList();
        List<Visitador>   vis = usuarioCtrl.listarVisitadores();
        List<Veterinario> vet = usuarioCtrl.listarVeterinarios();
        if (anim.isEmpty() || vis.isEmpty() || vet.isEmpty()) {
            warn("Necesitás al menos:\n• 1 animal adoptable (sano y no adoptado)\n• 1 visitador\n• 1 veterinario"); return;
        }
        JComboBox<AnimalDomestico> a1 = new JComboBox<>(anim.toArray(new AnimalDomestico[0]));
        JComboBox<AnimalDomestico> a2 = new JComboBox<>(anim.toArray(new AnimalDomestico[0]));
        a2.insertItemAt(null, 0); a2.setSelectedIndex(0);
        a2.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                return super.getListCellRendererComponent(l, v == null ? "Ninguno (adopción de 1 animal)" : v, i, s, f);
            }
        });
        JComboBox<Visitador>   adCB = new JComboBox<>(vis.toArray(new Visitador[0]));
        JComboBox<Veterinario> reCB = new JComboBox<>(vet.toArray(new Veterinario[0]));
        JPanel f = form("Animal 1:", a1, "Animal 2 (opcional):", a2, "Adoptante:", adCB, "Responsable (Vet):", reCB);
        if (!confirm(f, "Crear Adopción")) return;
        AnimalDomestico animal1 = (AnimalDomestico) a1.getSelectedItem();
        AnimalDomestico animal2 = (AnimalDomestico) a2.getSelectedItem();
        if (animal2 != null && animal2 == animal1) { error("Los dos animales no pueden ser el mismo."); return; }
        try {
            adopcionCtrl.registrarAdopcion(animal1, animal2, (Visitador) adCB.getSelectedItem(), (Veterinario) reCB.getSelectedItem());
            info("Adopción registrada con éxito.");
        } catch (Exception ex) { error(ex.getMessage()); }
        finally { refrescarTodo(); } // siempre refrescar: que la UI nunca quede desactualizada respecto al estado real
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — ALARMAS
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearAlarma() {
        List<Animal> anim = animalCtrl.listarAnimales();
        if (anim.isEmpty()) { warn("No hay animales registrados."); return; }
        JTextField titF = new JTextField(), descF = new JTextField();
        JSpinner frecSp = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));
        JSpinner dateSp = dateSpin();
        JComboBox<Animal> animCB = new JComboBox<>(anim.toArray(new Animal[0]));
        JList<TipoTratamiento> accList = new JList<>(TipoTratamiento.values());
        accList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel f = form("Animal:", animCB, "Título:", titF, "Descripción:", descF,
                        "Frecuencia (días):", frecSp, "Fecha disparo:", dateSp,
                        "Acciones (Ctrl=múltiple):", new JScrollPane(accList));
        if (!confirm(f, "Crear Alarma")) return;
        try {
            if (titF.getText().trim().isEmpty()) throw new IllegalArgumentException("Título obligatorio.");
            if (accList.getSelectedValuesList().isEmpty()) throw new IllegalArgumentException("Seleccioná al menos una acción.");
            LocalDateTime fecha = dateToLDT(dateSp);
            alarmaCtrl.create(new AlarmaDTO(0, ((Animal) animCB.getSelectedItem()).getId(),
                titF.getText().trim(), descF.getText().trim(),
                (int) frecSp.getValue(), fecha, "ACTIVA", false, null, accList.getSelectedValuesList()));
            refrescarAlarmas(); info("Alarma creada.");
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgModifAlarma() {
        List<AlarmaDTO> alarmas = alarmaCtrl.getAll();
        if (alarmas.isEmpty()) { info("No hay alarmas creadas."); return; }
        JComboBox<AlarmaDTO> selCB = new JComboBox<>(alarmas.toArray(new AlarmaDTO[0]));
        if (!confirm(selCB, "Seleccionar alarma a modificar")) return;
        AlarmaDTO sel = (AlarmaDTO) selCB.getSelectedItem(); if (sel == null) return;
        JTextField titF  = new JTextField(sel.getTitulo());
        JTextField descF = new JTextField(sel.getDescripcion());
        JSpinner frecSp  = new JSpinner(new SpinnerNumberModel(sel.getFrecuenciaDias(), 1, 365, 1));
        JSpinner dateSp  = dateSpin();
        dateSp.setValue(Date.from(sel.getFechaProximoDisparo().atZone(ZoneId.systemDefault()).toInstant()));
        JComboBox<TipoTratamiento> tipoCB = new JComboBox<>(TipoTratamiento.values());
        if (!sel.getAcciones().isEmpty()) tipoCB.setSelectedItem(sel.getAcciones().get(0));
        JPanel f = form("Título:", titF, "Descripción:", descF,
                        "Frecuencia (días):", frecSp, "Fecha disparo:", dateSp, "Acción principal:", tipoCB);
        if (!confirm(f, "Modificar Alarma")) return;
        try {
            AlarmaDTO actualizado = new AlarmaDTO(
                sel.getId(), sel.getIdAnimal(), titF.getText().trim(), descF.getText().trim(),
                (int) frecSp.getValue(), dateToLDT(dateSp), sel.getEstado(), sel.isCompletada(),
                sel.getFechaCompletado(), Collections.singletonList((TipoTratamiento) tipoCB.getSelectedItem())
            );
            alarmaCtrl.update(sel.getId(), actualizado);
            refrescarAlarmas(); info("Alarma modificada.");
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgAtenderAlarma() {
        List<AlarmaDTO> activas = alarmaCtrl.getAll().stream()
            .filter(a -> !a.isCompletada() && !"FINALIZADO".equals(a.getEstado())).toList();
        if (activas.isEmpty()) { info("No hay alarmas pendientes por atender."); return; }
        List<Veterinario> vets = usuarioCtrl.listarVeterinarios();
        if (vets.isEmpty()) { warn("No hay veterinarios registrados."); return; }
        JComboBox<AlarmaDTO>   alCB    = new JComboBox<>(activas.toArray(new AlarmaDTO[0]));
        JComboBox<Veterinario> vetCB   = new JComboBox<>(vets.toArray(new Veterinario[0]));
        JTextField             comF    = new JTextField();
        JCheckBox              finCK   = new JCheckBox("Marcar tratamiento como FINALIZADO");
        JPanel f = form("Alarma:", alCB, "Veterinario:", vetCB, "Comentario:", comF, "", finCK);
        if (!confirm(f, "Atender Alarma")) return;
        AlarmaDTO al  = (AlarmaDTO) alCB.getSelectedItem();
        Veterinario vet = (Veterinario) vetCB.getSelectedItem();
        if (al != null && vet != null) {
            alarmaCtrl.atenderAlarma(al.getId(), comF.getText().trim(), finCK.isSelected(), vet);
            refrescarAlarmas(); info("Alarma atendida correctamente.");
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — FICHA MÉDICA
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearFicha() {
        List<Animal> anim = animalCtrl.listarAnimales();
        if (anim.isEmpty()) { warn("No hay animales."); return; }
        JComboBox<Animal> cb = new JComboBox<>(anim.toArray(new Animal[0]));
        if (!confirm(form("Animal:", cb), "Crear Ficha Médica")) return;
        try {
            // También registramos el historial clínico para el animal via HistorialClinicoController
            Animal a = (Animal) cb.getSelectedItem();
            fichaCtrl.crearFicha(a);
            histCtrl.crearHistorial(a);   // HistorialClinicoController en uso
            info("Ficha médica creada para: " + a.getNombre());
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgExportarFicha() {
        List<FichaMedica> fichas = fichaCtrl.listarTodas();
        if (fichas.isEmpty()) { warn("No hay fichas médicas. Creá una primero."); return; }
        JComboBox<FichaMedica> fCB  = new JComboBox<>(fichas.toArray(new FichaMedica[0]));
        fCB.setRenderer(fichaRenderer());
        JComboBox<String>      fmCB = new JComboBox<>(new String[]{"PDF", "Excel"});
        if (!confirm(form("Animal:", fCB, "Formato:", fmCB), "Exportar Ficha Médica")) return;
        FichaMedica fm = (FichaMedica) fCB.getSelectedItem();
        try {
            String fmt = (String) fmCB.getSelectedItem();
            if ("PDF".equals(fmt)) fm.exportar(new ExportadorPDF("A4"));
            else fm.exportar(new ExportadorExcel(fm.getAnimal().getNombre()));
            mostrarFichaWindow(fm, fmt);
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void mostrarFichaWindow(FichaMedica fm, String fmt) {
        JDialog d = new JDialog(this, "Ficha — " + fmt, false);
        d.setSize(540, 320); d.setLocationRelativeTo(this);
        JTextArea ta = new JTextArea(
            "=== FICHA MÉDICA (" + fmt + ") ===\n\n"
            + fm.obtenerDatosTecnicos()
            + "\n\nTratamientos:        " + fm.getHistorial().getListaTratamiento().size()
            + "\nComentarios médicos: " + fm.getHistorial().getListaComentario().size()
            + "\nVisitas registradas: " + fm.getHistorial().getListaVisitas().size());
        ta.setEditable(false); ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        d.add(new JScrollPane(ta)); d.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — TRATAMIENTO  (usa TratamientoController)
    // ════════════════════════════════════════════════════════════════════════

    private void dlgRegistrarTrat() {
        List<FichaMedica> fichas = fichaCtrl.listarTodas();
        if (fichas.isEmpty()) { warn("Primero creá una Ficha Médica para el animal."); return; }
        JComboBox<FichaMedica>     fCB = new JComboBox<>(fichas.toArray(new FichaMedica[0]));
        fCB.setRenderer(fichaRenderer());
        JComboBox<TipoTratamiento> tCB = new JComboBox<>(TipoTratamiento.values());
        if (!confirm(form("Animal (Ficha Médica):", fCB, "Tipo de tratamiento:", tCB), "Registrar Tratamiento")) return;

        FichaMedica     fm   = (FichaMedica)     fCB.getSelectedItem();
        TipoTratamiento tipo = (TipoTratamiento) tCB.getSelectedItem();
        try {
            Tratamiento t = new Tratamiento(tipo);
            fm.agregarTratamiento(t);
            fichaCtrl.actualizar(fm);
            animalCtrl.ponerEnTratamiento(fm.getAnimal());
            refrescarTodo();
            info("Tratamiento registrado:\n• Animal: " + fm.getAnimal().getNombre()
                    + "\n• Tipo: " + tipo + "\n• Estado: Pendiente");
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgCambiarEstTrat() {
        List<FichaMedica> fichas = fichaCtrl.listarTodas();
        Map<Tratamiento, FichaMedica> mapa = new LinkedHashMap<>();
        for (FichaMedica fm : fichas)
            for (Tratamiento t : fm.getHistorial().getListaTratamiento())
                mapa.put(t, fm);

        if (mapa.isEmpty()) { warn("No hay tratamientos registrados.\nUsá 'Registrar Trat.' primero."); return; }

        JComboBox<Tratamiento> tCB = new JComboBox<>(mapa.keySet().toArray(new Tratamiento[0]));
        tCB.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Tratamiento t) {
                    FichaMedica fm = mapa.get(t);
                    lbl.setText((fm != null ? fm.getAnimal().getNombre() : "?")
                            + " — " + t.getTipoTratamientoEnum()
                            + "  [" + t.getEstado().getClass().getSimpleName() + "]");
                }
                return lbl;
            }
        });
        JComboBox<String> acCB = new JComboBox<>(new String[]{
                "Aplicar  (Pendiente → En Curso)",
                "Finalizar  (alta médica)",
                "Cancelar"
        });
        if (!confirm(form("Tratamiento:", tCB, "Acción:", acCB), "Cambiar Estado de Tratamiento")) return;

        Tratamiento t  = (Tratamiento) tCB.getSelectedItem();
        FichaMedica fm = mapa.get(t);
        String ac      = (String) acCB.getSelectedItem();
        try {
            if (ac.startsWith("Aplicar")) {
                t.aplicarTratamiento();
            } else if (ac.startsWith("Finalizar")) {
                t.finalizarTratamiento();
                boolean otrosActivos = fm.getHistorial().getListaTratamiento().stream()
                        .anyMatch(tr -> tr != t
                                && !(tr.getEstado() instanceof Finalizado)
                                && !(tr.getEstado() instanceof Cancelado));
                if (!otrosActivos) animalCtrl.disponibilizar(fm.getAnimal());
            } else {
                t.cancelarTratamiento();
            }

            fichaCtrl.actualizar(fm);

            refrescarTodo();
            info("Estado actualizado a: " + t.getEstado().getClass().getSimpleName());
        } catch (IllegalStateException ex) {
            error("Transición no válida desde " + t.getEstado().getClass().getSimpleName()
                    + ":\n" + ex.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — COMENTARIO  (usa ComentarioController)
    // ════════════════════════════════════════════════════════════════════════

    private void dlgAgregarComentario() {
        List<FichaMedica> fichas = fichaCtrl.listarTodas();
        if (fichas.isEmpty()) { warn("Primero creá una Ficha Médica para el animal."); return; }
        List<Veterinario> vets = usuarioCtrl.listarVeterinarios();
        if (vets.isEmpty()) { warn("No hay veterinarios registrados."); return; }

        JComboBox<FichaMedica> fCB  = new JComboBox<>(fichas.toArray(new FichaMedica[0]));
        fCB.setRenderer(fichaRenderer());
        JComboBox<Veterinario> vCB  = new JComboBox<>(vets.toArray(new Veterinario[0]));
        JTextArea texta = new JTextArea(4, 30);
        texta.setLineWrap(true); texta.setWrapStyleWord(true);

        JPanel f = form("Animal (Ficha Médica):", fCB, "Veterinario:", vCB,
                "Comentario:", new JScrollPane(texta));
        if (!confirm(f, "Agregar Comentario Médico")) return;

        FichaMedica fm  = (FichaMedica) fCB.getSelectedItem();
        Veterinario vet = (Veterinario) vCB.getSelectedItem();
        String texto    = texta.getText().trim();
        if (texto.isEmpty()) { error("El comentario no puede estar vacío."); return; }

        try {
            ComentarioMedico cm = new ComentarioMedico(vet, texto);
            fm.agregarComentarioMedico(cm);
            fichaCtrl.actualizar(fm);
            info("Comentario agregado:\n• Animal: " + fm.getAnimal().getNombre()
                    + "\n• Vet: " + vet + "\n• Texto: " + texto);
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — HISTORIAL  (usa HistorialClinicoController)
    // ════════════════════════════════════════════════════════════════════════

    private void dlgVerHistorial() {
        List<FichaMedica> fichas = fichaCtrl.listarTodas();
        if (fichas.isEmpty()) { warn("No hay fichas médicas creadas."); return; }
        JComboBox<FichaMedica> fCB = new JComboBox<>(fichas.toArray(new FichaMedica[0]));
        fCB.setRenderer(fichaRenderer());
        if (!confirm(fCB, "Ver historial de:")) return;

        FichaMedica fm = (FichaMedica) fCB.getSelectedItem();
        if (fm == null) return;

        // HistorialClinicoController obtiene el historial en memoria
        // La FichaMedica tiene el historial con los datos cargados desde MySQL
        var h = fm.getHistorial();

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════\n");
        sb.append("  HISTORIAL CLÍNICO — ").append(fm.getAnimal().getNombre().toUpperCase()).append("\n");
        sb.append("══════════════════════════════════════════════════\n");
        sb.append(fm.obtenerDatosTecnicos()).append("\n\n");

        sb.append("── TRATAMIENTOS (").append(h.getListaTratamiento().size()).append(") ─────────────────\n");
        if (h.getListaTratamiento().isEmpty()) sb.append("  (ninguno)\n");
        else for (Tratamiento t : h.getListaTratamiento()) {
            sb.append("  • ").append(t.getTipoTratamientoEnum())
              .append("  →  ").append(t.getEstado().getClass().getSimpleName());
            if (t.getFechaInicio() != null) sb.append("  |  Inicio: ").append(t.getFechaInicio());
            if (t.getFechaFin()   != null) sb.append("  |  Fin: ").append(t.getFechaFin());
            sb.append("\n");
        }

        sb.append("\n── COMENTARIOS MÉDICOS (").append(h.getListaComentario().size()).append(") ─────────\n");
        if (h.getListaComentario().isEmpty()) sb.append("  (ninguno)\n");
        else for (ComentarioMedico c : h.getListaComentario()) {
            sb.append("  • [").append(c.getFecha()).append("]  ")
              .append(c.getVeterinario() != null ? c.getVeterinario() : "Desconocido")
              .append(":  ").append(c.getCasillaComentario()).append("\n");
        }

        sb.append("\n── VISITAS DOMICILIARIAS (").append(h.getListaVisitas().size()).append(") ─────────\n");
        if (h.getListaVisitas().isEmpty()) sb.append("  (ninguna)\n");
        else for (Visita v : h.getListaVisitas()) {
            sb.append("  • ").append(v.getFechaProgramada());
            if (v.isCompletada()) sb.append("  ✓  real: ").append(v.getFechaReal());
            if (v.getComentarios() != null && !v.getComentarios().isEmpty())
                sb.append("  —  ").append(v.getComentarios());
            sb.append("\n");
        }

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false); ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane sc = new JScrollPane(ta); sc.setPreferredSize(new Dimension(620, 440));
        JDialog dlg = new JDialog(this, "Historial — " + fm.getAnimal().getNombre(), true);
        dlg.add(sc); dlg.pack(); dlg.setLocationRelativeTo(this); dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    // DIÁLOGOS — SEGUIMIENTO Y VISITAS
    // ════════════════════════════════════════════════════════════════════════

    private void dlgCrearSeguimiento() {
        List<Adopcion> adops = adopcionCtrl.listarTodos();
        if (adops.isEmpty()) { warn("No hay adopciones. Creá una adopción primero."); return; }
        List<Visitador> vis = usuarioCtrl.listarVisitadores();
        if (vis.isEmpty()) { warn("No hay visitadores."); return; }

        JComboBox<Adopcion> aCB = new JComboBox<>(adops.toArray(new Adopcion[0]));
        aCB.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, s, f);
                if (v instanceof Adopcion a) {
                    String anim = a.getAnimales().stream().map(Animal::getNombre).collect(Collectors.joining(", "));
                    lbl.setText(a.getAdoptante().getNombre() + " " + a.getAdoptante().getApellido() + "  |  [" + anim + "]");
                }
                return lbl;
            }
        });
        JComboBox<Visitador>              rCB  = new JComboBox<>(vis.toArray(new Visitador[0]));
        JComboBox<DiaSemana>              dCB  = new JComboBox<>(DiaSemana.values());
        JTextField                        deF  = new JTextField("09:00");
        JTextField                        haF  = new JTextField("12:00");
        JComboBox<PreferenciaRecordatorio> pCB = new JComboBox<>(PreferenciaRecordatorio.values());
        JSpinner                          cantSp = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));

        JPanel f = form("Adopción:", aCB, "Responsable visitas (Visitador):", rCB,
                        "Día:", dCB, "Desde (hh:mm):", deF, "Hasta (hh:mm):", haF,
                        "Canal recordatorio:", pCB, "Cantidad de visitas:", cantSp);
        if (!confirm(f, "Crear Seguimiento Post-Adopción")) return;

        try {
            int cant = (int) cantSp.getValue();
            Adopcion selectedAdopcion = (Adopcion) aCB.getSelectedItem();
            AdopcionDTO adopcionDto = selectedAdopcion != null ? new AdopcionDTO(
                selectedAdopcion.getId(),
                selectedAdopcion.getAnimales(),
                selectedAdopcion.getResponsable(),
                selectedAdopcion.getAdoptante()
            ) : null;

            Visitador selectedVisitador = (Visitador) rCB.getSelectedItem();
            UsuarioDTO visitadorDto = selectedVisitador != null ? new UsuarioDTO(
                selectedVisitador.getNombre(),
                selectedVisitador.getApellido(),
                selectedVisitador.getEmail(),
                selectedVisitador.getTelefono(),
                selectedVisitador.getEstadoCivil(),
                selectedVisitador.getOcupacion(),
                selectedVisitador.getMotivoAdopcion(),
                selectedVisitador.getAnimalesInteres(),
                selectedVisitador.tieneOtrasMascotas()
            ) : null;

            segCtrl.crearSeguimiento(
                adopcionDto, visitadorDto,
                (DiaSemana) dCB.getSelectedItem(), deF.getText().trim(), haF.getText().trim(),
                (PreferenciaRecordatorio) pCB.getSelectedItem(), cant);
            refrescarSeguimientos();
            info("Seguimiento creado con " + cant + " visitas programadas.\n\n"
               + "→ Ir a pestaña 'Seguimientos'\n"
               + "→ Click en el seguimiento para ver sus visitas\n"
               + "→ Seleccionar una visita ⏳ y click en 'Registrar Visita'");
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    private void dlgRegistrarVisita() {
        VisitaDTO sel = listVisita != null ? listVisita.getSelectedValue() : null;
        if (sel == null) {
            info("¿Cómo registrar una visita?\n\n"
               + "1. Ir a la pestaña 'Seguimientos'\n"
               + "2. Click en un seguimiento de la lista superior\n"
               + "3. Aparecen las visitas abajo (⏳ = pendiente)\n"
               + "4. Seleccionar una visita pendiente\n"
               + "5. Volver aquí y hacer click en 'Registrar Visita'");
            return;
        }
        if (sel.isCompletada()) {
            warn("Esta visita ya fue completada el " + sel.getFechaReal() + "."); return;
        }

        JComboBox<CalificacionEnum> estCB  = new JComboBox<>(CalificacionEnum.values());
        JComboBox<CalificacionEnum> limCB  = new JComboBox<>(CalificacionEnum.values());
        JComboBox<CalificacionEnum> ambCB  = new JComboBox<>(CalificacionEnum.values());
        estCB.setSelectedItem(CalificacionEnum.BUENO);
        limCB.setSelectedItem(CalificacionEnum.BUENO);
        ambCB.setSelectedItem(CalificacionEnum.BUENO);
        JTextField comF  = new JTextField();
        JCheckBox  contCK = new JCheckBox("¿Programar más visitas?", true);

        JPanel f = form("Estado general mascota:", estCB, "Limpieza del lugar:", limCB,
                        "Ambiente:", ambCB, "Comentarios:", comF, "Continuación:", contCK);
        if (!confirm(f, "Registrar Resultado de Visita")) return;

        try {
            EncuestaDTO enc = new EncuestaDTO(
                (CalificacionEnum) estCB.getSelectedItem(),
                (CalificacionEnum) limCB.getSelectedItem(),
                (CalificacionEnum) ambCB.getSelectedItem());
            segCtrl.registrarResultadoVisita(sel.getId(), enc, comF.getText().trim(), contCK.isSelected());
            refrescarSeguimientos();
            info("Visita registrada con éxito." + (!contCK.isSelected()
                ? "\nEl seguimiento ha finalizado." : ""));
        } catch (Exception ex) { error(ex.getMessage()); }
    }

    // ════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════════════

    /** Crea un JPanel de GridBagLayout con pares (label, componente) alineados de forma limpia. */
    private JPanel form(Object... pairs) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < pairs.length; i += 2) {
            Object labelObj = pairs[i];
            Object compObj = (i + 1 < pairs.length) ? pairs[i + 1] : null;

            int row = i / 2;

            // Columna 1: Etiqueta (Label) o componente izquierdo
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;

            if (labelObj instanceof String s) {
                JLabel lbl = new JLabel(s);
                p.add(lbl, gbc);
            } else if (labelObj instanceof Component c) {
                p.add(c, gbc);
            }

            // Columna 2: Componente de entrada derecho
            if (compObj != null) {
                gbc.gridx = 1;
                gbc.gridy = row;
                gbc.weightx = 1.0;
                gbc.gridwidth = 1;
                gbc.anchor = GridBagConstraints.WEST;

                if (compObj instanceof Component c) {
                    if (c instanceof JScrollPane || c instanceof JList || c instanceof JTextArea) {
                        gbc.fill = GridBagConstraints.BOTH;
                        gbc.weighty = 1.0;
                    } else {
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.weighty = 0.0;
                    }
                    p.add(c, gbc);
                }
            }
        }
        return p;
    }

    private boolean confirm(Component comp, String title) {
        return JOptionPane.showConfirmDialog(this, comp, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    private void info(String msg)  { JOptionPane.showMessageDialog(this, msg, "Éxito",      JOptionPane.INFORMATION_MESSAGE); }
    private void warn(String msg)  { JOptionPane.showMessageDialog(this, msg, "Advertencia",JOptionPane.WARNING_MESSAGE); }
    private void error(String msg) { JOptionPane.showMessageDialog(this, msg, "Error",      JOptionPane.ERROR_MESSAGE); }

    private JSpinner dateSpin() {
        JSpinner sp = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        sp.setEditor(new JSpinner.DateEditor(sp, "dd/MM/yyyy HH:mm"));
        return sp;
    }

    private LocalDateTime dateToLDT(JSpinner sp) {
        return ((Date) sp.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}