package presentation;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.EstadoPregunta;
import domain.QuestionService;
import infra.Observer;

/**
 * Vista de estadísticas: cuántas preguntas hay por cada estado. Se
 * suscribe como Observer al QuestionService y se refresca sola cada vez
 * que alguien cambia el estado de una pregunta desde GUIQuestions.
 */
public class GUIObserver1 extends JFrame implements Observer {

    private final QuestionService service;

    private final JLabel lblBorrador = new JLabel();
    private final JLabel lblPendiente = new JLabel();
    private final JLabel lblEliminada = new JLabel();
    private final JLabel lblTotal = new JLabel();

    public GUIObserver1(QuestionService service) {
        super("Vista de estadísticas");
        this.service = service;
        construirInterfaz();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(320, 220);
        update();
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 4, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Preguntas por estado"),
                new EmptyBorder(6, 10, 6, 10)));

        for (JLabel etiqueta : new JLabel[] { lblBorrador, lblPendiente, lblEliminada }) {
            etiqueta.setFont(etiqueta.getFont().deriveFont(14f));
        }
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));

        panel.add(lblBorrador);
        panel.add(lblPendiente);
        panel.add(lblEliminada);
        panel.add(lblTotal);
        setContentPane(panel);
    }

    @Override
    public void update() {
        Map<EstadoPregunta, Long> conteo = service.contarPorEstado();
        long borrador = conteo.getOrDefault(EstadoPregunta.BORRADOR, 0L);
        long pendiente = conteo.getOrDefault(EstadoPregunta.PENDIENTE_REVISION, 0L);
        long eliminada = conteo.getOrDefault(EstadoPregunta.ELIMINADA, 0L);

        lblBorrador.setText("Borrador: " + borrador);
        lblPendiente.setText("Pendiente de revisión: " + pendiente);
        lblEliminada.setText("Eliminada: " + eliminada);
        lblTotal.setText("Total: " + (borrador + pendiente + eliminada));
    }
}
