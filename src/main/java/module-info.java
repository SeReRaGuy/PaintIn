module com.example.paintin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.paintin to javafx.fxml;
    exports com.example.paintin;
}