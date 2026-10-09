package domain;

public enum EstadoPregunta {

    BORRADOR("Borrador"),
    PENDIENTE_REVISION("Pendiente de revisión"),
    ELIMINADA("Eliminada");

    private final String etiqueta;

    EstadoPregunta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
