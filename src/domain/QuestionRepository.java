package domain;

import java.util.List;

/**
 * Puerto que el dominio necesita para persistir preguntas. La capa access
 * implementa esta interfaz (inversión de dependencias: el dominio no
 * depende de cómo se guardan los datos, sino al revés).
 */
public interface QuestionRepository {

    List<Question> obtenerTodas();

    Question obtenerPorId(String id);

    void actualizarEstado(String id, EstadoPregunta nuevoEstado);
}
