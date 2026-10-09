package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import access.QuestionImplRepository;
import domain.EstadoPregunta;
import domain.Question;
import domain.QuestionRepository;
import domain.QuestionService;

/**
 * Ventana principal (vista + parte del "controlador" del micro patrón MVC:
 * traduce acciones del usuario en llamadas al QuestionService). Permite
 * elegir una pregunta, ver su formulario y cambiar su estado; ese cambio es
 * lo que notifica a GUIObserver1 y GUIObserver2.
 *
 * Módulo de gestión del banco de preguntas: pensado para los roles Autor
 * (redacta preguntas en estado Borrador) y Revisor (las mueve a Pendiente
 * de revisión o las elimina). No es para el rol Estudiante, que no debe ver
 * la respuesta correcta antes de resolver la prueba.
 */
public class GUIQuestions extends JFrame {

    private final QuestionService service;

    private final JComboBox<Question> comboPreguntas = new JComboBox<>();
    private final JButton btnCargar = new JButton("Cargar pregunta");

    private final JTextField txtId = campoSoloLectura();
    private final JTextField txtNombre = campoSoloLectura();
    private final JTextArea txtEnunciado = areaSoloLectura();
    private final JTextArea txtOpciones = areaSoloLectura();
    private final JTextField txtRespuestaCorrecta = campoSoloLectura();
    private final JLabel lblEstadoActual = new JLabel("-");
    private final JComboBox<EstadoPregunta> comboNuevoEstado = new JComboBox<>(EstadoPregunta.values());
    private final JButton btnActualizar = new JButton("Actualizar estado");

    private Question preguntaActual;

    public GUIQuestions(QuestionService service) {
        super("Banco de Preguntas Saber PRO - Gestión de preguntas");
        this.service = service;
        construirInterfaz();
        cargarCombo();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 640);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(contenido);

        JLabel lblRol = new JLabel("Panel de gestión — rol: Autor / Revisor (no visible para Estudiante)");
        lblRol.setFont(lblRol.getFont().deriveFont(Font.ITALIC, 11f));
        lblRol.setForeground(new Color(90, 90, 90));
        contenido.add(lblRol, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setLayout(new javax.swing.BoxLayout(centro, javax.swing.BoxLayout.Y_AXIS));
        centro.add(panelSelector());
        centro.add(javax.swing.Box.createVerticalStrut(10));
        centro.add(panelFormulario());
        contenido.add(new JScrollPane(centro), BorderLayout.CENTER);
    }

    private JPanel panelSelector() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Seleccionar pregunta"));

        JPanel fila = new JPanel(new BorderLayout(8, 8));
        fila.add(comboPreguntas, BorderLayout.CENTER);
        fila.add(btnCargar, BorderLayout.EAST);
        panel.add(fila, BorderLayout.CENTER);

        btnCargar.addActionListener(e -> cargarPreguntaSeleccionada());
        return panel;
    }

    private JPanel panelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de pregunta"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        fila = agregarFila(panel, c, fila, "Id:", txtId);
        fila = agregarFila(panel, c, fila, "Nombre:", txtNombre);

        c.gridx = 0; c.gridy = fila; c.weightx = 0;
        panel.add(new JLabel("Pregunta:"), c);
        c.gridx = 1; c.gridy = fila; c.weightx = 1; c.gridwidth = 2;
        panel.add(new JScrollPane(txtEnunciado), c);
        c.gridwidth = 1;
        fila++;

        c.gridx = 0; c.gridy = fila; c.weightx = 0;
        panel.add(new JLabel("Opciones:"), c);
        c.gridx = 1; c.gridy = fila; c.weightx = 1; c.gridwidth = 2;
        panel.add(new JScrollPane(txtOpciones), c);
        c.gridwidth = 1;
        fila++;

        fila = agregarFila(panel, c, fila, "Respuesta correcta:", txtRespuestaCorrecta);

        c.gridx = 0; c.gridy = fila; c.weightx = 0;
        panel.add(new JLabel("Estado actual:"), c);
        c.gridx = 1; c.gridy = fila; c.weightx = 1; c.gridwidth = 2;
        lblEstadoActual.setFont(lblEstadoActual.getFont().deriveFont(Font.BOLD));
        panel.add(lblEstadoActual, c);
        c.gridwidth = 1;
        fila++;

        c.gridx = 0; c.gridy = fila; c.weightx = 0;
        panel.add(new JLabel("Nuevo estado:"), c);
        c.gridx = 1; c.gridy = fila; c.weightx = 1; c.gridwidth = 2;
        panel.add(comboNuevoEstado, c);
        c.gridwidth = 1;
        fila++;

        c.gridx = 1; c.gridy = fila; c.weightx = 0; c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        panel.add(btnActualizar, c);

        btnActualizar.addActionListener(e -> actualizarEstado());
        btnActualizar.setEnabled(false);
        return panel;
    }

    private int agregarFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta, JTextField campo) {
        c.gridx = 0; c.gridy = fila; c.weightx = 0; c.gridwidth = 1;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1; c.gridy = fila; c.weightx = 1; c.gridwidth = 2;
        panel.add(campo, c);
        c.gridwidth = 1;
        return fila + 1;
    }

    private void cargarCombo() {
        comboPreguntas.removeAllItems();
        for (Question pregunta : service.listarPreguntas()) {
            comboPreguntas.addItem(pregunta);
        }
    }

    private void cargarPreguntaSeleccionada() {
        Question seleccionada = (Question) comboPreguntas.getSelectedItem();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una pregunta de la lista.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        preguntaActual = service.obtenerPregunta(seleccionada.getId());
        txtId.setText(preguntaActual.getId());
        txtNombre.setText(preguntaActual.getNombre());
        txtEnunciado.setText(preguntaActual.getEnunciado());
        txtOpciones.setText(String.join("\n", preguntaActual.getDistractores().getOpcionesConLetra()));
        txtRespuestaCorrecta.setText(preguntaActual.getDistractores().getLetraCorrecta());
        lblEstadoActual.setText(preguntaActual.getEstado().toString());
        comboNuevoEstado.setSelectedItem(preguntaActual.getEstado());
        btnActualizar.setEnabled(true);
    }

    private void actualizarEstado() {
        if (preguntaActual == null) {
            return;
        }
        EstadoPregunta nuevoEstado = (EstadoPregunta) comboNuevoEstado.getSelectedItem();
        service.cambiarEstado(preguntaActual.getId(), nuevoEstado);
        lblEstadoActual.setText(nuevoEstado.toString());
        JOptionPane.showMessageDialog(this,
                "Estado actualizado a: " + nuevoEstado,
                "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    private static JTextField campoSoloLectura() {
        JTextField campo = new JTextField();
        campo.setEditable(false);
        return campo;
    }

    private static JTextArea areaSoloLectura() {
        JTextArea area = new JTextArea(3, 20);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorada) {
            // Si el look and feel del sistema no está disponible, se usa el por defecto.
        }

        QuestionRepository repository = new QuestionImplRepository();
        QuestionService service = new QuestionService(repository);

        SwingUtilities.invokeLater(() -> {
            GUIObserver1 vistaEstadisticas = new GUIObserver1(service);
            GUIObserver2 vistaGrafica = new GUIObserver2(service);
            service.addObserver(vistaEstadisticas);
            service.addObserver(vistaGrafica);

            GUIQuestions ventanaPrincipal = new GUIQuestions(service);

            ventanaPrincipal.setLocation(60, 60);
            vistaEstadisticas.setLocation(650, 60);
            vistaGrafica.setLocation(650, 330);

            ventanaPrincipal.setVisible(true);
            vistaEstadisticas.setVisible(true);
            vistaGrafica.setVisible(true);
        });
    }
}
