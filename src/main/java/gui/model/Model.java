package gui.model;

import gui.view.View;

import java.util.ArrayList;

public abstract class Model {
    ArrayList<View> observers;

    public Model() {
        this.observers = new ArrayList<>();
    }

    public void registerView(View view) {
        observers.add(view);

        //TODO: dit niet hier uitvoeren?
        updateView();
    }

    public void updateView() {
        for(View view : observers) {
            view.update(this);
        }
    }
}
