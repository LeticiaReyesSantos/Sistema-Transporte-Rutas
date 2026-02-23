module com.rutas.redtransporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.rutas.redtransporte.controllers to javafx.fxml;
    //exports com.rutas.redtransporte;
    exports com.rutas.redtransporte.application;
    opens com.rutas.redtransporte.application to javafx.fxml;
    exports com.rutas.redtransporte.controllers;
}