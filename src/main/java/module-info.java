module com.example.paintin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.paintin to javafx.fxml;
    exports com.example.paintin;
}