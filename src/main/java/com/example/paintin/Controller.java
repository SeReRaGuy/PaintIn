package com.example.paintin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    ToggleGroup laplasianToggleGroup = new ToggleGroup();
    @FXML
    private ImageView imageViewIn;
    @FXML
    private ImageView imageViewOut;
    @FXML
    private TextArea helpText;
    @FXML
    private ComboBox<String> effectComboBox;
    @FXML
    private Slider gammaSlider;
    @FXML
    private TextField gammaText;
    @FXML
    private TextField thresholdText;
    @FXML
    private Slider thresholdSlider;
    @FXML
    private Button applyEffectButton;
    @FXML
    private Slider imageSlider;
    @FXML
    private RadioButton laplasianRadio;
    @FXML
    private RadioButton laplasianOrigRadio;
    @FXML
    private Button histogramButton;
    private Image selectedImage;
    private FileChooser chooser = new FileChooser();
    private double[][] inhomogeneousAveragingFilter = {
            {1 / 16.0, 2 / 16.0, 1 / 16.0},
            {2 / 16.0, 4 / 16.0, 2 / 16.0},
            {1 / 16.0, 2 / 16.0, 1 / 16.0}
    };


    @FXML
    public void initialize() {
        effectComboBox.getItems().add("Негатив");
        effectComboBox.getItems().add("Гамма-коррекция");
        effectComboBox.getItems().add("Градиент Робертса");
        effectComboBox.getItems().add("Градиент Собела");
        effectComboBox.getItems().add("Метод Лапласиана");
        effectComboBox.getItems().add("Эквализация гистограммы (ЧБ)");
        effectComboBox.getItems().add("Порог бинаризации");
        effectComboBox.getItems().add("Пороговый фильтр методом Оцу");
        effectComboBox.getItems().add("Однородный усредняющий фильтр");
        effectComboBox.getItems().add("Неоднородный усредняющий фильтр");
        effectComboBox.getItems().add("Медианный фильтр");
        effectComboBox.getItems().add("Фильтр максимума");
        effectComboBox.getItems().add("Фильтр минимума");
        effectComboBox.getItems().add("Фильтр срединной точки");

        gammaSlider.setMin(0.0);
        gammaSlider.setMax(25);

        thresholdSlider.setMin(0.0);
        thresholdSlider.setMax(1.0);

        imageSlider.setMin(-20);
        imageSlider.setMax(100);

        laplasianRadio.setToggleGroup(laplasianToggleGroup);
        laplasianOrigRadio.setToggleGroup(laplasianToggleGroup);

        effectComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    gammaSlider.setVisible(false);
                    gammaText.setVisible(false);
                    laplasianRadio.setVisible(false);
                    laplasianOrigRadio.setVisible(false);
                    thresholdSlider.setVisible(false);
                    thresholdText.setVisible(false);

                    if (newValue.equals("Гамма-коррекция")) {
                        gammaSlider.setVisible(true);
                        gammaText.setVisible(true);
                    }

                    if (newValue.equals("Метод Лапласиана")) {
                        laplasianRadio.setVisible(true);
                        laplasianOrigRadio.setVisible(true);
                    }

                    if (newValue.equals("Порог бинаризации")) {
                        thresholdSlider.setVisible(true);
                        thresholdText.setVisible(true);
                    }
                }
            }
        });

        //Изменение гамма-коррекции происходит и по кнопке, и при изменении ползунка
        gammaSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (selectedImage != null && gammaSlider.isVisible()) {
                    applyGammaCorrection(gammaSlider.getValue());
                    helpText.setText(String.valueOf(gammaSlider.getValue()));
                    gammaText.setText(String.valueOf(gammaSlider.getValue()));
                }

            }
        });

        //Изменение смещения изображения при изменении ползунка
        imageSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (selectedImage != null) {
                    moveImage(imageSlider.getValue());
                    helpText.setText(String.valueOf(imageSlider.getValue()));
                }
            }
        });

        thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (selectedImage != null) {
                    applyThresholdFilter(thresholdSlider.getValue());
                    helpText.setText(String.valueOf(thresholdSlider.getValue()));
                }
            }
        });
    }

    @FXML
    public void onChooseImage() {
        chooser.setTitle("Выберите изображение");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            imageViewIn.setImage(selectedImage);
            helpText.setText(selectedImage.getHeight() + " / " + selectedImage.getWidth() + " = " + (selectedImage.getHeight() / selectedImage.getWidth()));
        }
    }

    @FXML
    public void onApplyEffect() {
        if (selectedImage != null) {
            String selectedEffect = effectComboBox.getValue();
            if (selectedEffect != null) {
                if (selectedEffect.equals("Негатив")) applyNegative();
                else if (selectedEffect.equals("Гамма-коррекция"))
                    applyGammaCorrection(Double.parseDouble(gammaText.getText()));
                else if (selectedEffect.equals("Градиент Робертса")) applyRobertsOperator();
                else if (selectedEffect.equals("Градиент Собела")) applySobelOperator();
                else if (selectedEffect.equals("Метод Лапласиана")) applyLaplacianOperator();
                else if (selectedEffect.equals("Эквализация гистограммы (ЧБ)")) applyEqualizeHistogram();
                else if (selectedEffect.equals("Порог бинаризации")) applyThresholdFilter(Double.parseDouble(thresholdText.getText()));
                else if (selectedEffect.equals("Пороговый фильтр методом Оцу")) applyOtsuThresholdFilter();
                else if (selectedEffect.equals("Однородный усредняющий фильтр")) applyHomogeneousAveragingFilter(5); //Если нужно - организовать выбор размера
                else if (selectedEffect.equals("Неоднородный усредняющий фильтр")) applyInhomogeneousAveragingFilter(inhomogeneousAveragingFilter); //Если нужно - организовать выбор размера
                else if (selectedEffect.equals("Медианный фильтр")) applyMedianFilter(3); //Если нужно - организовать выбор размера
                else if (selectedEffect.equals("Фильтр максимума")) applyMaxFilter(3); //Если нужно - организовать выбор размера
                else if (selectedEffect.equals("Фильтр минимума")) applyMinFilter(3); //Если нужно - организовать выбор размера
                else if (selectedEffect.equals("Фильтр срединной точки")) applyMidpointFilter(3); //Если нужно - организовать выбор размера
            }

        }
    }

    private void applyNegative() {
        if (selectedImage != null) {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage negativeImage = new WritableImage(width, height);
            PixelReader reader = selectedImage.getPixelReader();
            PixelWriter writer = negativeImage.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    Color negativeColor = Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue(), color.getOpacity());
                    writer.setColor(x, y, negativeColor);
                }
            }
            imageViewOut.setImage(negativeImage);

        }
    }
    private void applyGammaCorrection(double gamma) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage gammaImage = new WritableImage(width, height);
        PixelReader reader = selectedImage.getPixelReader();
        PixelWriter writer = gammaImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                Color gammaColor = Color.color(Math.pow(color.getRed(), gamma), Math.pow(color.getGreen(), gamma), Math.pow(color.getBlue(), gamma), color.getOpacity());
                writer.setColor(x, y, gammaColor);
            }
        }

        imageViewOut.setImage(gammaImage);
    }
    private void applyRobertsOperator() {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage robertsImage = new WritableImage(width, height);
        PixelReader reader = selectedImage.getPixelReader();
        PixelWriter writer = robertsImage.getPixelWriter();

        int[][] Gx = {
                {1, 0},
                {0, -1}
        };

        int[][] Gy = {
                {0, 1},
                {-1, 0}
        };

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {

                int pixel1 = getGrayScale(reader.getArgb(x, y));
                int pixel2 = getGrayScale(reader.getArgb(x + 1, y));
                int pixel3 = getGrayScale(reader.getArgb(x, y + 1));
                int pixel4 = getGrayScale(reader.getArgb(x + 1, y + 1));


                int gx = (Gx[0][0] * pixel1) + (Gx[0][1] * pixel2) + (Gx[1][0] * pixel3) + (Gx[1][1] * pixel4);
                int gy = (Gy[0][0] * pixel1) + (Gy[0][1] * pixel2) + (Gy[1][0] * pixel3) + (Gy[1][1] * pixel4);


                int gradient = (int) Math.sqrt((gx * gx) + (gy * gy));

                // Ограничиваем значения градиента от 0 до 255
                gradient = Math.min(255, Math.max(0, gradient));

                // Преобразуем градиент в цвет (все каналы RGB одинаковые для серого)
                int newPixelValue = (gradient << 16) | (gradient << 8) | gradient | (0xFF << 24);  // Добавляем альфа-канал

                writer.setArgb(x, y, newPixelValue);
            }
        }

        imageViewOut.setImage(robertsImage);
    }
    private void applySobelOperator() {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage sobelImage = new WritableImage(width, height);
        PixelReader reader = selectedImage.getPixelReader();
        PixelWriter writer = sobelImage.getPixelWriter();

        int[][] Gx = {
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}
        };

        int[][] Gy = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        // Проходим по каждому пикселю, начиная с 1 и до (width - 1) и (height - 1)
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                // Получаем значения яркости для 9 соседних пикселей
                int pixel1 = getGrayScale(reader.getArgb(x - 1, y - 1));
                int pixel2 = getGrayScale(reader.getArgb(x, y - 1));
                int pixel3 = getGrayScale(reader.getArgb(x + 1, y - 1));
                int pixel4 = getGrayScale(reader.getArgb(x - 1, y));
                int pixel5 = getGrayScale(reader.getArgb(x, y));
                int pixel6 = getGrayScale(reader.getArgb(x + 1, y));
                int pixel7 = getGrayScale(reader.getArgb(x - 1, y + 1));
                int pixel8 = getGrayScale(reader.getArgb(x, y + 1));
                int pixel9 = getGrayScale(reader.getArgb(x + 1, y + 1));

                int gx = (Gx[0][0] * pixel1) + (Gx[0][1] * pixel2) + (Gx[0][2] * pixel3) +
                        (Gx[1][0] * pixel4) + (Gx[1][1] * pixel5) + (Gx[1][2] * pixel6) +
                        (Gx[2][0] * pixel7) + (Gx[2][1] * pixel8) + (Gx[2][2] * pixel9);

                int gy = (Gy[0][0] * pixel1) + (Gy[0][1] * pixel2) + (Gy[0][2] * pixel3) +
                        (Gy[1][0] * pixel4) + (Gy[1][1] * pixel5) + (Gy[1][2] * pixel6) +
                        (Gy[2][0] * pixel7) + (Gy[2][1] * pixel8) + (Gy[2][2] * pixel9);

                int gradient = (int) Math.sqrt((gx * gx) + (gy * gy));

                // Ограничиваем значения градиента от 0 до 255
                gradient = Math.min(255, Math.max(0, gradient));

                // Преобразуем градиент в цвет (все каналы RGB одинаковые для серого)
                int newPixelValue = (gradient << 16) | (gradient << 8) | gradient | (0xFF << 24); // Добавляем альфа-канал

                writer.setArgb(x, y, newPixelValue);
            }
        }
        imageViewOut.setImage(sobelImage);
    }
    private void applyLaplacianOperator() {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage laplacianImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = laplacianImage.getPixelWriter();

        // Преобразование изображения в градации серого
        double[][] grayscaleValues = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double gray = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
                grayscaleValues[x][y] = gray;
            }
        }

        // Лапласиан: фильтр 3x3 для каждого пикселя
        int[][] laplacianKernel = {
                {0, 1, 0},
                {1, -4, 1},
                {0, 1, 0}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double laplacianValue = 0;

                // Применение ядра Лапласа
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        laplacianValue += grayscaleValues[x + kx][y + ky] * laplacianKernel[ky + 1][kx + 1];
                    }
                }

                // Нормализация и ограничение значений для диапазона [0, 1]
                laplacianValue = Math.min(Math.max(laplacianValue + 0.5, 0), 1);

                // Установка значения пикселя в выводе
                Color laplacianColor = new Color(laplacianValue, laplacianValue, laplacianValue, 1.0);
                pixelWriter.setColor(x, y, laplacianColor);
            }
        }

        if (laplasianRadio.isSelected()) imageViewOut.setImage(laplacianImage);
        else if (laplasianOrigRadio.isSelected()) {

            // Создание итогового изображения с оригинальными цветами и контурами
            WritableImage combinedImage = new WritableImage(width, height);
            PixelWriter combinedPixelWriter = combinedImage.getPixelWriter();

            // Сложение оригинального изображения с лапласом
            PixelReader originalPixelReader = selectedImage.getPixelReader();
            double alpha = 0.5; // Коэффициент для усиления контуров

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color originalColor = originalPixelReader.getColor(x, y);
                    Color laplacianColor = pixelReader.getColor(x, y);

                    // Сложение цветовых значений
                    double combinedRed = Math.min(originalColor.getRed() + alpha * laplacianColor.getRed(), 1.0);
                    double combinedGreen = Math.min(originalColor.getGreen() + alpha * laplacianColor.getGreen(), 1.0);
                    double combinedBlue = Math.min(originalColor.getBlue() + alpha * laplacianColor.getBlue(), 1.0);

                    // Установка итогового цвета
                    combinedPixelWriter.setColor(x, y, new Color(combinedRed, combinedGreen, combinedBlue, 1.0));
                }
            }

            // Вывод комбинированного изображения
            imageViewOut.setImage(combinedImage);
        }
    }
    public void applyEqualizeHistogram() {
        int[] histogram = new int[256];
        PixelReader reader = selectedImage.getPixelReader();
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        // Шаг 1: Подсчет гистограммы яркости
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int brightness = (int) (0.299 * color.getRed() * 255 +
                        0.587 * color.getGreen() * 255 +
                        0.114 * color.getBlue() * 255);
                histogram[brightness]++;
            }
        }

        // Шаг 2: Вычисление кумулятивного распределения
        int[] cumulativeDistribution = new int[256];
        cumulativeDistribution[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeDistribution[i] = cumulativeDistribution[i - 1] + histogram[i];
        }

        // Нормализация кумулятивного распределения для использования в преобразовании
        int totalPixels = width * height;
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = (int) ((cumulativeDistribution[i] - cumulativeDistribution[0]) * 255.0 / (totalPixels - cumulativeDistribution[0]));
            lut[i] = Math.max(0, Math.min(255, lut[i])); // Ограничиваем значения от 0 до 255
        }

        // Шаг 3: Создание нового изображения с эквализированной гистограммой
        WritableImage equalizedImage = new WritableImage(width, height);
        PixelWriter writer = equalizedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int brightness = (int) (0.299 * color.getRed() * 255 +
                        0.587 * color.getGreen() * 255 +
                        0.114 * color.getBlue() * 255);
                int newBrightness = lut[brightness];

                // Создаем новый цвет с эквализированной яркостью, сохраняя исходный оттенок
                Color newColor = Color.grayRgb(newBrightness);
                writer.setColor(x, y, newColor);
            }
        }

        imageViewOut.setImage(equalizedImage);
    }
    public void applyThresholdFilter(double threshold) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage thresholdImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = thresholdImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Читаем цвет текущего пикселя
                Color color = pixelReader.getColor(x, y);

                // Преобразуем цвет в оттенок серого для расчета яркости
                double brightness = color.getBrightness();

                // Сравниваем яркость с порогом и устанавливаем белый или черный цвет
                Color binaryColor = brightness >= threshold ? Color.WHITE : Color.BLACK;

                // Записываем бинаризованный цвет в выходное изображение
                pixelWriter.setColor(x, y, binaryColor);
            }
        }

        imageViewOut.setImage(thresholdImage);
    }
    public void applyOtsuThresholdFilter() {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage OtsuThresholdImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = OtsuThresholdImage.getPixelWriter();

        int[] histogram = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double brightness = pixelReader.getColor(x, y).getBrightness();
                int grayLevel = (int) (brightness * 255);
                histogram[grayLevel]++;
            }
        }

        int totalPixels = width * height;
        double sumAll = 0;
        for (int i = 0; i < 256; i++) {
            sumAll += i * histogram[i];
        }

        int bestThreshold = 0;
        double maxBetweenClassVariance = 0;
        double sumBackground = 0;
        int weightBackground = 0;

        for (int t = 0; t < 256; t++) {
            // Обновляем вес и сумму яркости фона
            weightBackground += histogram[t];
            if (weightBackground == 0) continue;

            int weightForeground = totalPixels - weightBackground;
            if (weightForeground == 0) break;

            sumBackground += t * histogram[t];

            double meanBackground = sumBackground / weightBackground;
            double meanForeground = (sumAll - sumBackground) / weightForeground;

            // Межклассовая дисперсия
            double betweenClassVariance = weightBackground * weightForeground * Math.pow(meanBackground - meanForeground, 2);

            // Проверяем, является ли текущее значение максимальным
            if (betweenClassVariance > maxBetweenClassVariance) {
                maxBetweenClassVariance = betweenClassVariance;
                bestThreshold = t;
            }
        }

        double threshold = bestThreshold / 255.0;  // переводим порог в диапазон 0-1 для удобства

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double brightness = color.getBrightness();
                Color binaryColor = brightness >= threshold ? Color.WHITE : Color.BLACK;
                pixelWriter.setColor(x, y, binaryColor);
            }
        }
        imageViewOut.setImage(OtsuThresholdImage);
    }
    private void applyHomogeneousAveragingFilter(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage homogeneousAveragingImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = homogeneousAveragingImage.getPixelWriter();

        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int redSum = 0;
                int greenSum = 0;
                int blueSum = 0;
                int count = 0;

                // Проходим по окну фильтра
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за пределы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redSum += color.getRed() * 255;
                            greenSum += color.getGreen() * 255;
                            blueSum += color.getBlue() * 255;
                            count++;
                        }
                    }
                }

                // Усредняем сумму и записываем цвет в результирующее изображение
                int avgRed = redSum / count;
                int avgGreen = greenSum / count;
                int avgBlue = blueSum / count;

                Color avgColor = Color.rgb(avgRed, avgGreen, avgBlue);
                pixelWriter.setColor(x, y, avgColor);
            }
        }
        imageViewOut.setImage(homogeneousAveragingImage);
    }
    private void applyInhomogeneousAveragingFilter(double[][] kernel) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage inhomogeneousAveragingImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = inhomogeneousAveragingImage.getPixelWriter();

        int filterSize = kernel.length;
        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double redSum = 0;
                double greenSum = 0;
                double blueSum = 0;
                double weightSum = 0;

                // Применение ядра фильтра
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за пределы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            double weight = kernel[dy + radius][dx + radius];

                            redSum += color.getRed() * weight;
                            greenSum += color.getGreen() * weight;
                            blueSum += color.getBlue() * weight;
                            weightSum += weight;
                        }
                    }
                }

                // Нормализация и установка нового цвета
                int avgRed = (int) Math.min(255, (redSum / weightSum) * 255);
                int avgGreen = (int) Math.min(255, (greenSum / weightSum) * 255);
                int avgBlue = (int) Math.min(255, (blueSum / weightSum) * 255);

                Color avgColor = Color.rgb(avgRed, avgGreen, avgBlue);
                pixelWriter.setColor(x, y, avgColor);
            }
        }
        imageViewOut.setImage(inhomogeneousAveragingImage);
    }
    private void applyMedianFilter(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage medianFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = medianFilterImage.getPixelWriter();

        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Списки для хранения значений цветов в окне фильтра
                List<Integer> redValues = new ArrayList<>();
                List<Integer> greenValues = new ArrayList<>();
                List<Integer> blueValues = new ArrayList<>();

                // Сбор значений из окна фильтра
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за границы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redValues.add((int) (color.getRed() * 255));
                            greenValues.add((int) (color.getGreen() * 255));
                            blueValues.add((int) (color.getBlue() * 255));
                        }
                    }
                }

                // Поиск медианы для каждого канала
                int medianRed = getMedian(redValues);
                int medianGreen = getMedian(greenValues);
                int medianBlue = getMedian(blueValues);

                // Установка медианного цвета в результирующее изображение
                Color medianColor = Color.rgb(medianRed, medianGreen, medianBlue);
                pixelWriter.setColor(x, y, medianColor);
            }
        }
        imageViewOut.setImage(medianFilterImage);
    }
    private void applyMaxFilter(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage maxFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = maxFilterImage.getPixelWriter();

        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int maxRed = 0;
                int maxGreen = 0;
                int maxBlue = 0;

                // Проходим по окну фильтра вокруг текущего пикселя
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за границы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            maxRed = Math.max(maxRed, (int) (color.getRed() * 255));
                            maxGreen = Math.max(maxGreen, (int) (color.getGreen() * 255));
                            maxBlue = Math.max(maxBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                // Установка максимального цвета в результирующее изображение
                Color maxColor = Color.rgb(maxRed, maxGreen, maxBlue);
                pixelWriter.setColor(x, y, maxColor);
            }
        }
        imageViewOut.setImage(maxFilterImage);
    }
    private void applyMinFilter(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage minFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = minFilterImage.getPixelWriter();
        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minRed = 255;
                int minGreen = 255;
                int minBlue = 255;

                // Проход по окну фильтра вокруг текущего пикселя
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за границы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            minRed = Math.min(minRed, (int) (color.getRed() * 255));
                            minGreen = Math.min(minGreen, (int) (color.getGreen() * 255));
                            minBlue = Math.min(minBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                // Установка минимального цвета в результирующее изображение
                Color minColor = Color.rgb(minRed, minGreen, minBlue);
                pixelWriter.setColor(x, y, minColor);
            }
        }
        imageViewOut.setImage(minFilterImage);
    }
    private void applyMidpointFilter(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage midpointFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = midpointFilterImage.getPixelWriter();
        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minRed = 255, minGreen = 255, minBlue = 255;
                int maxRed = 0, maxGreen = 0, maxBlue = 0;

                // Проход по окну фильтра вокруг текущего пикселя
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        // Проверка на выход за границы изображения
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);

                            // Обновление минимальных значений
                            minRed = Math.min(minRed, (int) (color.getRed() * 255));
                            minGreen = Math.min(minGreen, (int) (color.getGreen() * 255));
                            minBlue = Math.min(minBlue, (int) (color.getBlue() * 255));

                            // Обновление максимальных значений
                            maxRed = Math.max(maxRed, (int) (color.getRed() * 255));
                            maxGreen = Math.max(maxGreen, (int) (color.getGreen() * 255));
                            maxBlue = Math.max(maxBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                // Вычисление срединного значения
                int midRed = (minRed + maxRed) / 2;
                int midGreen = (minGreen + maxGreen) / 2;
                int midBlue = (minBlue + maxBlue) / 2;

                // Установка цвета срединной точки в результирующее изображение
                Color midColor = Color.rgb(midRed, midGreen, midBlue);
                pixelWriter.setColor(x, y, midColor);
            }
        }
        imageViewOut.setImage(midpointFilterImage);
    }







    private int getGrayScale(int argb) {
        int red = (argb >> 16) & 0xFF; // Побитовые операции, >> для сдвига и получения отдельных частей 4-х битного argb, & 0xFF - умножение на маску
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        return (red + green + blue) / 3;
    }
    @FXML
    public void onShowHistogram() {
        if (selectedImage != null) {
            HistogramViewer histogramViewer = new HistogramViewer(selectedImage);
            histogramViewer.showHistogram(); //showEqualizedImage showHistogram
        } else {
            // Добавьте уведомление для пользователя, если изображение не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Изображение не выбрано");
            alert.setContentText("Пожалуйста, выберите изображение перед открытием гистограммы.");
            alert.showAndWait();
        }
    }
    private void moveImage(double x) {
        imageViewIn.setX((selectedImage.getHeight() / selectedImage.getWidth()) * x);
        imageViewOut.setX((selectedImage.getHeight() / selectedImage.getWidth()) * x);
    }
    private int getMedian(List<Integer> values) {
        Collections.sort(values);
        int middle = values.size() / 2;
        if (values.size() % 2 == 0) {
            return (values.get(middle - 1) + values.get(middle)) / 2;
        } else {
            return values.get(middle);
        }
    }
}