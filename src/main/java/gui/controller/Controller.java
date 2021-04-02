package gui.controller;

import gui.MainWindow;
import gui.model.Model;

public abstract class Controller {
    protected Model model;
    protected MainWindow mainWindow;

    public void setModel(Model model) {
        this.model = model;
    }

    //TODO: dit in constructor?
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}
