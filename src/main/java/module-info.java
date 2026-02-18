module com.rutas.redtransporte {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rutas.redtransporte to javafx.fxml;
    exports com.rutas.redtransporte;
}