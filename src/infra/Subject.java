package infra;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto observable genérico (capa transversal): mantiene la lista de
 * observadores y sabe notificarlos, sin saber nada del dominio concreto
 * que lo extiende (QuestionService).
 */
public abstract class Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
