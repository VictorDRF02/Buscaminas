module com.example.buscaminas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.buscaminas to javafx.fxml;
    exports com.example.buscaminas;
}