package access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import domain.EstadoPregunta;
import domain.Question;
import domain.QuestionDistractors;
import domain.QuestionRepository;

/**
 * Implementación sencilla del repositorio usando un mapa en memoria, tal
 * como lo permite el enunciado del taller (no se requiere base de datos).
 */
public class QuestionImplRepository implements QuestionRepository {

    private final Map<String, Question> preguntas = new LinkedHashMap<>();

    public QuestionImplRepository() {
        cargarDatosDemo();
    }

    @Override
    public List<Question> obtenerTodas() {
        return new ArrayList<>(preguntas.values());
    }

    @Override
    public Question obtenerPorId(String id) {
        return preguntas.get(id);
    }

    @Override
    public void actualizarEstado(String id, EstadoPregunta nuevoEstado) {
        Question pregunta = preguntas.get(id);
        if (pregunta != null) {
            pregunta.setEstado(nuevoEstado);
        }
    }

    private void agregar(String id, String nombre, String enunciado,
                          EstadoPregunta estado, int indiceCorrecta, String... opciones) {
        QuestionDistractors distractores = new QuestionDistractors(Arrays.asList(opciones), indiceCorrecta);
        preguntas.put(id, new Question(id, nombre, enunciado, distractores, estado));
    }

    private void cargarDatosDemo() {
        agregar("P-001", "Pregunta sobre DDD",
                "¿Cuál es el objetivo principal de DDD?",
                EstadoPregunta.BORRADOR, 1,
                "Diseñar bases de datos", "Modelar el dominio del negocio",
                "Eliminar UML", "Crear interfaces gráficas");

        agregar("P-002", "Lectura crítica - inferencias",
                "Al leer un texto argumentativo, ¿qué habilidad se evalúa al pedir una inferencia?",
                EstadoPregunta.PENDIENTE_REVISION, 2,
                "Copiar literalmente el texto", "Memorizar el autor",
                "Deducir información no explícita a partir del contexto", "Contar los párrafos");

        agregar("P-003", "Razonamiento cuantitativo - proporciones",
                "Si 3 obreros construyen un muro en 12 días, ¿en cuántos días lo construyen 6 obreros?",
                EstadoPregunta.BORRADOR, 1,
                "24 días", "6 días", "12 días", "3 días");

        agregar("P-004", "Competencias ciudadanas - normas",
                "¿Cuál principio explica que una ley posterior prevalece sobre una anterior en igual jerarquía?",
                EstadoPregunta.PENDIENTE_REVISION, 0,
                "Ley posterior deroga ley anterior", "Ley especial deroga ley general",
                "Retroactividad de la ley", "Supremacía constitucional");

        agregar("P-005", "Comunicación escrita - coherencia",
                "¿Qué recurso permite mantener la coherencia entre párrafos de un ensayo?",
                EstadoPregunta.ELIMINADA, 3,
                "Repetir la misma oración", "Cambiar de tema en cada párrafo",
                "Usar solo oraciones cortas", "Conectores lógicos entre ideas");

        agregar("P-006", "Inglés - comprensión lectora",
                "Choose the option that best completes: \"She has been working here ___ 2020.\"",
                EstadoPregunta.BORRADOR, 2,
                "for", "ago", "since", "during");

        agregar("P-007", "Lectura crítica - postura del autor",
                "¿Qué elemento del texto revela con mayor claridad la postura del autor?",
                EstadoPregunta.PENDIENTE_REVISION, 1,
                "El título", "Las expresiones valorativas y adjetivos usados",
                "El número de párrafos", "La fuente bibliográfica");
    }
}
