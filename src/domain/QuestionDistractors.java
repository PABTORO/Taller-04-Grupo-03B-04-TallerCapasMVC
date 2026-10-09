package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrupa las opciones de respuesta de una pregunta (los distractores más la
 * opción correcta) para no sobrecargar a Question con la lógica de
 * opciones/respuesta correcta (principio de responsabilidad única).
 */
public class QuestionDistractors {

    private final List<String> opciones;
    private final int indiceCorrecta;

    public QuestionDistractors(List<String> opciones, int indiceCorrecta) {
        if (opciones == null || opciones.isEmpty()) {
            throw new IllegalArgumentException("Una pregunta necesita al menos una opción");
        }
        if (indiceCorrecta < 0 || indiceCorrecta >= opciones.size()) {
            throw new IllegalArgumentException("El índice de la respuesta correcta no es válido");
        }
        this.opciones = new ArrayList<>(opciones);
        this.indiceCorrecta = indiceCorrecta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    /** Devuelve las opciones ya formateadas como "A. texto", "B. texto", ... */
    public List<String> getOpcionesConLetra() {
        List<String> resultado = new ArrayList<>();
        for (int i = 0; i < opciones.size(); i++) {
            resultado.add(letra(i) + ". " + opciones.get(i));
        }
        return resultado;
    }

    public String getLetraCorrecta() {
        return letra(indiceCorrecta);
    }

    public String getTextoCorrecta() {
        return opciones.get(indiceCorrecta);
    }

    private String letra(int indice) {
        return String.valueOf((char) ('A' + indice));
    }
}
