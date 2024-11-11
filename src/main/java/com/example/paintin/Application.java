package com.example.paintin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;  // Импорт для работы с изображением

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1152, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Устанавливаем иконку приложения
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        stage.getIcons().add(icon);

        // Запрещаем изменение размера окна
        stage.setResizable(false);

        stage.setTitle("PaintIn");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
