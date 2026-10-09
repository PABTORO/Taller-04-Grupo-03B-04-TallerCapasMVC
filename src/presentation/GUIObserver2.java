package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.EstadoPregunta;
import domain.QuestionService;
import infra.Observer;

/**
 * Vista gráfica: gráfica de pastel con el porcentaje de preguntas por
 * estado. También es Observer del QuestionService, igual que
 * GUIObserver1, mostrando la misma información desde otro ángulo (esa es
 * la idea del patrón Observer: varias vistas independientes reaccionando
 * al mismo cambio de estado).
 */
public class GUIObserver2 extends JFrame implements Observer {

    private final QuestionService service;
    private final PieChartPanel grafico = new PieChartPanel();
    private final JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));

    public GUIObserver2(QuestionService service) {
        super("Vista gráfica");
        this.service = service;
        construirInterfaz();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(340, 380);
        update();
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Distribución de preguntas"));
        panel.add(grafico, BorderLayout.CENTER);
        panel.add(leyenda, BorderLayout.SOUTH);
        setContentPane(panel);
    }

    @Override
    public void update() {
        Map<EstadoPregunta, Long> conteo = service.contarPorEstado();
        grafico.setDatos(conteo);
        reconstruirLeyenda(conteo);
        grafico.repaint();
    }

    private void reconstruirLeyenda(Map<EstadoPregunta, Long> conteo) {
        leyenda.removeAll();
        long total = conteo.values().stream().mapToLong(Long::longValue).sum();
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            long cantidad = conteo.getOrDefault(estado, 0L);
            double porcentaje = total == 0 ? 0 : (cantidad * 100.0 / total);
            JLabel etiqueta = new JLabel(String.format("%s: %.0f%%", estado, porcentaje));
            etiqueta.setIcon(new ColorSwatch(PieChartPanel.colorDe(estado)));
            leyenda.add(etiqueta);
        }
        leyenda.revalidate();
        leyenda.repaint();
    }

    /** Pequeño ícono cuadrado de color para acompañar cada texto de la leyenda. */
    private static class ColorSwatch implements javax.swing.Icon {
        private final Color color;

        ColorSwatch(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y + 2, 10, 10);
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }
    }

    /** Panel que dibuja el pastel con Graphics2D a partir del conteo por estado. */
    private static class PieChartPanel extends JPanel {

        private Map<EstadoPregunta, Long> datos = new LinkedHashMap<>();

        static Color colorDe(EstadoPregunta estado) {
            switch (estado) {
                case BORRADOR: return new Color(66, 133, 244);
                case PENDIENTE_REVISION: return new Color(251, 173, 51);
                case ELIMINADA: return new Color(219, 68, 55);
                default: return Color.GRAY;
            }
        }

        void setDatos(Map<EstadoPregunta, Long> datos) {
            this.datos = datos;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(260, 260);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            long total = datos.values().stream().mapToLong(Long::longValue).sum();
            int diametro = Math.min(getWidth(), getHeight()) - 30;
            int x = (getWidth() - diametro) / 2;
            int y = (getHeight() - diametro) / 2;

            if (total == 0) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(x, y, diametro, diametro);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Sin datos", x + diametro / 2 - 25, y + diametro / 2);
                return;
            }

            double anguloInicio = 90;
            for (EstadoPregunta estado : EstadoPregunta.values()) {
                long cantidad = datos.getOrDefault(estado, 0L);
                double angulo = cantidad * 360.0 / total;
                g2.setColor(colorDe(estado));
                g2.fillArc(x, y, diametro, diametro, (int) Math.round(anguloInicio), -(int) Math.round(angulo));
                anguloInicio -= angulo;
            }

            g2.setColor(Color.WHITE);
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawOval(x, y, diametro, diametro);
        }
    }
}
