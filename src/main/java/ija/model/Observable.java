package ija.model;

public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();

    public interface Observer {
        void update(Observable observable);
    }
}
