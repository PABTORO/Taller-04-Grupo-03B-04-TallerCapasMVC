package domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import infra.Subject;

/**
 * Servicio de dominio: reglas de negocio sobre preguntas. Es además el
 * "sujeto" del patrón Observer (extiende Subject): cuando cambia el estado
 * de una pregunta, notifica a las vistas suscritas (estadísticas, gráfica).
 *
 * Depende de la interfaz QuestionRepository, no de una implementación
 * concreta, para poder cambiar el almacenamiento sin tocar esta clase.
 */
public class QuestionService extends Subject {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> listarPreguntas() {
        return repository.obtenerTodas();
    }

    public Question obtenerPregunta(String id) {
        return repository.obtenerPorId(id);
    }

    public void cambiarEstado(String id, EstadoPregunta nuevoEstado) {
        repository.actualizarEstado(id, nuevoEstado);
        notifyObservers();
    }

    /** Cuenta cuántas preguntas hay en cada estado, para las vistas de estadísticas. */
    public Map<EstadoPregunta, Long> contarPorEstado() {
        Map<EstadoPregunta, Long> conteo = new EnumMap<>(EstadoPregunta.class);
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            conteo.put(estado, 0L);
        }
        for (Question pregunta : repository.obtenerTodas()) {
            conteo.merge(pregunta.getEstado(), 1L, Long::sum);
        }
        return conteo;
    }
}
