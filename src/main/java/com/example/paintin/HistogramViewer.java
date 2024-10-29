package com.example.paintin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;

public class HistogramViewer {

    private Image image;

    public HistogramViewer(Image image) {
        this.image = image;
    }

    public void showHistogram() {
        Stage stage = new Stage();
        stage.setTitle("Histogram");

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(512, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawHistogram(gc);

        root.setCenter(canvas);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void drawHistogram(GraphicsContext gc) {
        int[] histogram = new int[256];
        PixelReader reader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Подсчет гистограммы
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int brightness = (int) (0.299 * color.getRed() * 255 + 0.587 * color.getGreen() * 255 + 0.114 * color.getBlue() * 255);
                histogram[brightness]++;
            }
        }

        // Нормировка и рисование гистограммы
        int max = 0;
        for (int value : histogram) {
            if (value > max) {
                max = value;
            }
        }

        // Рисование гистограммы (поднята вверх, чтобы оставить место для градиента)
        for (int i = 0; i < histogram.length; i++) {
            gc.setFill(Color.BLACK);
            gc.fillRect(i * 2, 380 - (histogram[i] * 350 / max), 2, histogram[i] * 350 / max);
        }

        // Добавление черно-белого градиента под гистограммой (на несколько пикселей ниже)
        for (int i = 0; i < 256; i++) {
            gc.setFill(Color.grayRgb(i));
            gc.fillRect(i * 2, 390, 2, 20); // прямоугольники шириной 2 пикселя и высотой 20 пикселей
        }
    }
}
