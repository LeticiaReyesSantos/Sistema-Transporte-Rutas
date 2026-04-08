package com.rutas.redtransporte.application;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        System.setProperty("glass.win.uiScale", "1.25");
        Application.launch(MainVisual.class, args);
    }
}
