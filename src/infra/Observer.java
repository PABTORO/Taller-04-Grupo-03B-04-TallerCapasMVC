package infra;

/**
 * Contrato del patrón Observer. Cualquier vista que deba enterarse de un
 * cambio de estado en el sujeto que observa implementa esta interfaz.
 */
public interface Observer {

    void update();
}
