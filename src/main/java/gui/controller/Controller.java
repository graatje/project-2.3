package gui.controller;

import gui.MainWindow;
import gui.model.Model;

public abstract class Controller<T extends Model> {
    protected T model;
    protected MainWindow mainWindow;

    public void setModel(T model) {
        this.model = model;
    }

    //TODO: dit in constructor?
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}
