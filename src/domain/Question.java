package domain;

/**
 * Entidad del dominio. No sabe nada de Swing ni de cómo se guarda: eso es
 * responsabilidad de la capa de presentación y de acceso, respectivamente.
 */
public class Question {

    private final String id;
    private final String nombre;
    private final String enunciado;
    private final QuestionDistractors distractores;
    private EstadoPregunta estado;

    public Question(String id, String nombre, String enunciado,
                     QuestionDistractors distractores, EstadoPregunta estado) {
        this.id = id;
        this.nombre = nombre;
        this.enunciado = enunciado;
        this.distractores = distractores;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public QuestionDistractors getDistractores() {
        return distractores;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public void setEstado(EstadoPregunta estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
