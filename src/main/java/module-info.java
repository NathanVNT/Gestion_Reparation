module org.nathanvernet.gestion_reparation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.nathanvernet.gestion_reparation to javafx.fxml;
    exports org.nathanvernet.gestion_reparation;
    exports org.nathanvernet.gestion_reparation.Controllers;
    opens org.nathanvernet.gestion_reparation.Controllers to javafx.fxml;
}