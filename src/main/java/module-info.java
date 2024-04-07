module org.nathanvernet.gestion_reparation {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.nathanvernet.gestion_reparation to javafx.fxml;
    exports org.nathanvernet.gestion_reparation;
}