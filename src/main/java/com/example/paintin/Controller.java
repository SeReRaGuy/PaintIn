package com.example.paintin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Controller {
    ToggleGroup laplasianToggleGroup = new ToggleGroup();
    ToggleGroup WBToggleGroup = new ToggleGroup();
    @FXML
    private ImageView imageViewIn;
    @FXML
    private ImageView imageViewOut;
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
    private Slider maskSizeSlider;
    @FXML
    private CheckBox onOriginalCheck;
    @FXML
    private Button saveButton;
    @FXML
    private Slider imageSlider;
    @FXML
    private RadioButton laplasianRadio;
    @FXML
    private RadioButton laplasianOrigRadio;
    @FXML
    private RadioButton whiteRadio;
    @FXML
    private RadioButton blackRadio;
    @FXML
    private RadioButton dilationWhiteRadio;
    @FXML
    private RadioButton dilationBlackRadio;
    @FXML
    private Button histogramButton;
    @FXML
    private TextField maskSizeText;
    @FXML
    private TextField moveImageField;
    @FXML
    private TextField firstBorderText;
    @FXML
    private TextField secondBorderText;
    @FXML
    private CheckBox onInvertCheck;
    @FXML
    private Button firstBorderPlusButton;
    @FXML
    private Button firstBorderMinusButton;
    @FXML
    private Button secondBorderMinusButton;
    @FXML
    private Button secondBorderPlusButton;
    @FXML
    private CheckBox onNowCheck;
    @FXML
    private Slider percentageFillHSlider;
    @FXML
    private Slider percentageFillVSlider;
    @FXML
    private TextField percentageFillHText;
    @FXML
    private TextField percentageFillVText;
    @FXML
    private CheckBox changeSideVCheck;
    @FXML
    private CheckBox changeSideHCheck;
    private Image selectedImage;
    private Image outputImage;
    private FileChooser chooser = new FileChooser();
    private int maskSize = 3;
    private double[][] inhomogeneousAveragingFilter = {
            {1 / 16.0, 2 / 16.0, 1 / 16.0},
            {2 / 16.0, 4 / 16.0, 2 / 16.0},
            {1 / 16.0, 2 / 16.0, 1 / 16.0}
    };
    private Color averageColor;


    @FXML
    public void initialize() {
        effectComboBox.getItems().add("Негатив");
        effectComboBox.getItems().add("Вырезание диапазона яркостей");
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
        effectComboBox.getItems().add("Дилатация");
        effectComboBox.getItems().add("Эрозия");
        effectComboBox.getItems().add("Замыкание");
        effectComboBox.getItems().add("Размыкание");
        effectComboBox.getItems().add("Выделение границ");
        effectComboBox.getItems().add("Остов");
        effectComboBox.getItems().add("Горизонтальная заливка");
        effectComboBox.getItems().add("Вертикальная заливка");

        moveImageField.setText("Сместить изображения");

        gammaSlider.setMin(0.0);
        gammaSlider.setMax(25);
        gammaText.setText("0");

        thresholdSlider.setMin(0.0);
        thresholdSlider.setMax(1.0);
        thresholdText.setText("0");

        imageSlider.setMin(-20);
        imageSlider.setMax(100);

        maskSizeSlider.setMin(1);
        maskSizeSlider.setMax(6);
        maskSizeSlider.setBlockIncrement(1);

        firstBorderText.setText("50");
        secondBorderText.setText("100");

        laplasianRadio.setToggleGroup(laplasianToggleGroup);
        laplasianOrigRadio.setToggleGroup(laplasianToggleGroup);

        whiteRadio.setToggleGroup(WBToggleGroup);
        blackRadio.setToggleGroup(WBToggleGroup);

        percentageFillHSlider.setMin(0);
        percentageFillHSlider.setMax(100);

        percentageFillVSlider.setMin(0);
        percentageFillVSlider.setMax(100);

        effectComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    gammaSlider.setVisible(false);
                    gammaText.setVisible(false);
                    laplasianRadio.setVisible(false);
                    laplasianOrigRadio.setVisible(false);
                    whiteRadio.setVisible(false);
                    blackRadio.setVisible(false);
                    thresholdSlider.setVisible(false);
                    thresholdText.setVisible(false);
                    maskSizeSlider.setVisible(false);
                    maskSizeText.setVisible(false);
                    firstBorderText.setVisible(false);
                    secondBorderText.setVisible(false);
                    onInvertCheck.setVisible(false);
                    firstBorderMinusButton.setVisible(false);
                    firstBorderPlusButton.setVisible(false);
                    secondBorderMinusButton.setVisible(false);
                    secondBorderPlusButton.setVisible(false);
                    onNowCheck.setVisible(false);
                    percentageFillHSlider.setVisible(false);
                    percentageFillVSlider.setVisible(false);
                    percentageFillHText.setVisible(false);
                    percentageFillVText.setVisible(false);
                    changeSideVCheck.setVisible(false);
                    changeSideHCheck.setVisible(false);

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

                    if (newValue.equals("Однородный усредняющий фильтр") || newValue.equals("Медианный фильтр") || newValue.equals("Фильтр максимума") || newValue.equals("Фильтр минимума") || newValue.equals("Фильтр срединной точки") || newValue.equals("Дилатация") || newValue.equals("Эрозия") || newValue.equals("Замыкание") || newValue.equals("Размыкание") || newValue.equals("Выделение границ")) {
                        maskSizeSlider.setVisible(true);
                        maskSizeText.setVisible(true);
                    }

                    if (newValue.equals("Эрозия") || newValue.equals("Дилатация") || newValue.equals("Замыкание") || newValue.equals("Размыкание") || newValue.equals("Выделение границ") || newValue.equals("Остов")) {
                        whiteRadio.setVisible(true);
                        blackRadio.setVisible(true);
                    }

                    if (newValue.equals("Вырезание диапазона яркостей")) {
                        firstBorderText.setVisible(true);
                        secondBorderText.setVisible(true);
                        onInvertCheck.setVisible(true);
                        firstBorderMinusButton.setVisible(true);
                        firstBorderPlusButton.setVisible(true);
                        secondBorderMinusButton.setVisible(true);
                        secondBorderPlusButton.setVisible(true);
                        onNowCheck.setVisible(true);
                    }

                    if (newValue.equals("Горизонтальная заливка")) {
                        percentageFillHSlider.setVisible(true);
                        percentageFillHText.setVisible(true);
                        changeSideHCheck.setVisible(true);
                    }

                    if (newValue.equals("Вертикальная заливка")) {
                        percentageFillVSlider.setVisible(true);
                        percentageFillVText.setVisible(true);
                        changeSideVCheck.setVisible(true);
                    }
                }
            }
        });

        gammaSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (selectedImage != null && gammaSlider.isVisible()) {
                    applyGammaCorrection(gammaSlider.getValue());
                    gammaText.setText(String.valueOf(gammaSlider.getValue()));
                }
            }
        });

        imageSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (selectedImage != null) {
                    moveImage(imageSlider.getValue());
                }
            }
        });

        thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (selectedImage != null) {
                    applyThresholdFilter(thresholdSlider.getValue());
                    thresholdText.setText(String.valueOf(thresholdSlider.getValue()));
                }
            }
        });

        maskSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                maskSize = (int) maskSizeSlider.getValue();
                maskSizeText.setText("Текущий размер маски : " + maskSize);
            }
        });

        firstBorderText.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                firstBorderText.setText(oldValue);
            } else {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value < 0 || value > 255) {
                        firstBorderText.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    firstBorderText.setText(oldValue);
                }
            }
            if (Integer.parseInt(newValue) > Integer.parseInt(secondBorderText.getText())) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Конфликт границ");
                alert.setContentText("Первая граница не должна быть больше второй. Пожалуйста, снизьте числовое значение первой границы или увеличьте значение второй границы.");
                firstBorderText.setText(oldValue);

                alert.showAndWait();
            }
        }));

        secondBorderText.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                secondBorderText.setText(oldValue);
            } else {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value < 0 || value > 255) {
                        secondBorderText.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    secondBorderText.setText(oldValue);
                }
            }
            if (Integer.parseInt(newValue) < Integer.parseInt(firstBorderText.getText())) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Конфликт границ");
                alert.setContentText("Вторая граница не должна быть меньше второй. Пожалуйста, увеличьте значение второй границы или снизьте числовое значение первой границы.");
                secondBorderText.setText(oldValue);

                alert.showAndWait();
            }
        }));

        percentageFillHSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (selectedImage != null) {
                    int roundedValue = (int) Math.round(percentageFillHSlider.getValue());
                    applyFillWithAverageColorHorizontal(roundedValue);
                    percentageFillHText.setText(String.valueOf(roundedValue));
                }
            }
        });

        percentageFillVSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (selectedImage != null) {
                    int roundedValue = (int) Math.round(percentageFillVSlider.getValue());
                    applyFillWithAverageColorVertical(roundedValue);
                    percentageFillVText.setText(String.valueOf(roundedValue));
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

            PixelReader pixelReader = selectedImage.getPixelReader();
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            averageColor = calculateAverageColor(pixelReader, width, height);
        }
    }

    @FXML
    public void onSaveImage() {
        if (selectedImage != null) {
            if (outputImage != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Сохранить изображение");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));

                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    if (!file.getName().toLowerCase().endsWith(".png")) {
                        file = new File(file.getAbsolutePath() + ".png");
                    }

                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(outputImage, null);
                    try {
                        ImageIO.write(bufferedImage, "png", file);
                        System.out.println("Изображение сохранено: " + file.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("Ошибка сохранения изображения: " + e.getMessage());
                    }
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Нет преобразованного изображения");
                alert.setContentText("Пожалуйста, примените какой-нибудь эффект, чтобы получить преобразованное изображение.");

                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Нет преобразованного изображения");
            alert.setContentText("Пожалуйста, загрузите изображение и примените какой-нибудь эффект, чтобы получить преобразованное изображение.");

            alert.showAndWait();
        }
    }

    @FXML
    public void onSwapImages() {
        if (selectedImage != null) {
            if (outputImage != null) {
                Image bufferImage;
                bufferImage = selectedImage;
                selectedImage = outputImage;
                outputImage = bufferImage;
                imageViewIn.setImage(selectedImage);
                imageViewOut.setImage(outputImage);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Нет второго изображения");
                alert.setContentText("Пожалуйста, примените какой-нибудь эффект, чтобы появилось второе изображение.");

                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не загружено изображение");
            alert.setContentText("Пожалуйста, загрузите изображение перед применением.");

            alert.showAndWait();
        }
    }

    @FXML
    public void onApplyEffect() {
        if (selectedImage != null) {
            String selectedEffect = effectComboBox.getValue();
            if (selectedEffect != null) {
                switch (selectedEffect) {
                    case "Негатив" -> applyNegative();
                    case "Вырезание диапазона яркостей" ->
                            applyBrightness(Integer.parseInt(firstBorderText.getText()), Integer.parseInt(secondBorderText.getText()), onInvertCheck.isSelected());
                    case "Гамма-коррекция" -> applyGammaCorrection(Double.parseDouble(gammaText.getText()));
                    case "Градиент Робертса" -> applyRobertsOperator();
                    case "Градиент Собела" -> applySobelOperator();
                    case "Метод Лапласиана" -> applyLaplacianOperator();
                    case "Эквализация гистограммы (ЧБ)" -> applyEqualizeHistogram();
                    case "Порог бинаризации" -> applyThresholdFilter(Double.parseDouble(thresholdText.getText()));
                    case "Пороговый фильтр методом Оцу" -> applyOtsuThresholdFilter();
                    case "Однородный усредняющий фильтр" -> applyHomogeneousAveragingFilter(maskSize);
                    case "Неоднородный усредняющий фильтр" ->
                            applyInhomogeneousAveragingFilter(inhomogeneousAveragingFilter);
                    case "Медианный фильтр" -> applyMedianFilter(maskSize);
                    case "Фильтр максимума" -> applyMaxFilter(maskSize);
                    case "Фильтр минимума" -> applyMinFilter(maskSize);
                    case "Фильтр срединной точки" -> applyMidpointFilter(maskSize);
                    case "Дилатация" -> applyDilation(maskSize);
                    case "Эрозия" -> applyErosion(maskSize);
                    case "Замыкание" -> applyClosing(maskSize);
                    case "Размыкание" -> applyOpening(maskSize);
                    case "Выделение границ" -> applyEdgeDetection(maskSize);
                    case "Остов" -> applySkeletonization();
                    case "Горизонтальная заливка" -> applyFillWithAverageColorHorizontal(Integer.parseInt(percentageFillHText.getText()));
                    case "Вертикальная заливка" -> applyFillWithAverageColorVertical(Integer.parseInt(percentageFillVText.getText()));
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Не выбран эффект");
                alert.setContentText("Пожалуйста, выберите эффект из списка перед применением.");

                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбрано изображение");
            alert.setContentText("Пожалуйста, выберите изображение перед применением.");

            alert.showAndWait();
        }
    }

    public void applyFillWithAverageColorHorizontal(double percentage) {
        if (selectedImage != null) {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage resultImage = new WritableImage(width, height);
            PixelReader pixelReader = selectedImage.getPixelReader();
            PixelWriter pixelWriter = resultImage.getPixelWriter();

            int fillHeight = (int) (height * (percentage / 100.0));

            if (changeSideHCheck.isSelected()) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (y >= height - fillHeight) {
                            pixelWriter.setColor(x, y, averageColor);
                        } else {
                            pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                        }
                    }
                }
            } else {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (y < fillHeight) {
                            pixelWriter.setColor(x, y, averageColor);
                        } else {
                            pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                        }
                    }
                }
            }

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(resultImage);
                selectedImage = resultImage;
            } else {
                imageViewOut.setImage(resultImage);
                outputImage = resultImage;
            }
        }
    }


    public void applyFillWithAverageColorVertical(double percentage) {
        if (selectedImage != null) {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage resultImage = new WritableImage(width, height);
            PixelReader pixelReader = selectedImage.getPixelReader();
            PixelWriter pixelWriter = resultImage.getPixelWriter();

            int fillWidth = (int) (width * (percentage / 100.0));

            if (changeSideVCheck.isSelected()) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (x >= width - fillWidth) {
                            pixelWriter.setColor(x, y, averageColor);
                        } else {
                            pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                        }
                    }
                }
            } else {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (x < fillWidth) {
                            pixelWriter.setColor(x, y, averageColor);
                        } else {
                            pixelWriter.setColor(x, y, pixelReader.getColor(x, y));
                        }
                    }
                }
            }

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(resultImage);
                selectedImage = resultImage;
            } else {
                imageViewOut.setImage(resultImage);
                outputImage = resultImage;
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
            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(negativeImage);
                selectedImage = negativeImage;
            } else {
                imageViewOut.setImage(negativeImage);
                outputImage = negativeImage;
            }

        }
    }

    public void applyBrightness(int low, int high, boolean invert) {
        if (selectedImage != null) {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage resultImage = new WritableImage(width, height);
            PixelReader pixelReader = selectedImage.getPixelReader();
            PixelWriter pixelWriter = resultImage.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = pixelReader.getColor(x, y);
                    double brightness = color.getBrightness();

                    int brightnessValue = (int) (brightness * 255);

                    if (invert) {
                        if (brightnessValue < low || brightnessValue > high) {
                            pixelWriter.setColor(x, y, color);
                        } else {
                            pixelWriter.setColor(x, y, Color.BLACK);
                        }
                    } else {
                        if (brightnessValue >= low && brightnessValue <= high) {
                            pixelWriter.setColor(x, y, color);
                        } else {
                            pixelWriter.setColor(x, y, Color.BLACK);
                        }
                    }
                }
            }

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(resultImage);
                selectedImage = resultImage;
            } else {
                imageViewOut.setImage(resultImage);
                outputImage = resultImage;
            }
        }
    }


    private void applyGammaCorrection(double gamma) {
        if (gamma >= 0) {
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

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(gammaImage);
                selectedImage = gammaImage;
            } else {
                imageViewOut.setImage(gammaImage);
                outputImage = gammaImage;
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверное значение");
            alert.setContentText("Пожалуйста, введите значение, не меньшее 0.");

            alert.showAndWait();
        }
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

                gradient = Math.min(255, Math.max(0, gradient));

                int newPixelValue = (gradient << 16) | (gradient << 8) | gradient | (0xFF << 24);

                writer.setArgb(x, y, newPixelValue);
            }
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(robertsImage);
            selectedImage = robertsImage;
        } else {
            imageViewOut.setImage(robertsImage);
            outputImage = robertsImage;
        }
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

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
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

                gradient = Math.min(255, Math.max(0, gradient));

                int newPixelValue = (gradient << 16) | (gradient << 8) | gradient | (0xFF << 24);

                writer.setArgb(x, y, newPixelValue);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(sobelImage);
            selectedImage = sobelImage;
        } else {
            imageViewOut.setImage(sobelImage);
            outputImage = sobelImage;
        }
    }

    private void applyLaplacianOperator() {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage laplacianImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = laplacianImage.getPixelWriter();

        double[][] grayscaleValues = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double gray = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
                grayscaleValues[x][y] = gray;
            }
        }

        int[][] laplacianKernel = {
                {0, 1, 0},
                {1, -4, 1},
                {0, 1, 0}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double laplacianValue = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        laplacianValue += grayscaleValues[x + kx][y + ky] * laplacianKernel[ky + 1][kx + 1];
                    }
                }

                laplacianValue = Math.min(Math.max(laplacianValue + 0.5, 0), 1);

                Color laplacianColor = new Color(laplacianValue, laplacianValue, laplacianValue, 1.0);
                pixelWriter.setColor(x, y, laplacianColor);
            }
        }

        if (laplasianRadio.isSelected()) imageViewOut.setImage(laplacianImage);
        else if (laplasianOrigRadio.isSelected()) {

            WritableImage combinedImage = new WritableImage(width, height);
            PixelWriter combinedPixelWriter = combinedImage.getPixelWriter();

            PixelReader originalPixelReader = selectedImage.getPixelReader();
            double alpha = 0.5;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color originalColor = originalPixelReader.getColor(x, y);
                    Color laplacianColor = pixelReader.getColor(x, y);

                    double combinedRed = Math.min(originalColor.getRed() + alpha * laplacianColor.getRed(), 1.0);
                    double combinedGreen = Math.min(originalColor.getGreen() + alpha * laplacianColor.getGreen(), 1.0);
                    double combinedBlue = Math.min(originalColor.getBlue() + alpha * laplacianColor.getBlue(), 1.0);

                    combinedPixelWriter.setColor(x, y, new Color(combinedRed, combinedGreen, combinedBlue, 1.0));
                }
            }

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(combinedImage);
                selectedImage = combinedImage;
            } else {
                imageViewOut.setImage(combinedImage);
                outputImage = combinedImage;
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите тип выводимого изображения.");

            alert.showAndWait();
        }
    }

    public void applyEqualizeHistogram() {
        int[] histogram = new int[256];
        PixelReader reader = selectedImage.getPixelReader();
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int brightness = (int) (0.299 * color.getRed() * 255 +
                        0.587 * color.getGreen() * 255 +
                        0.114 * color.getBlue() * 255);
                histogram[brightness]++;
            }
        }

        int[] cumulativeDistribution = new int[256];
        cumulativeDistribution[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeDistribution[i] = cumulativeDistribution[i - 1] + histogram[i];
        }

        int totalPixels = width * height;
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = (int) ((cumulativeDistribution[i] - cumulativeDistribution[0]) * 255.0 / (totalPixels - cumulativeDistribution[0]));
            lut[i] = Math.max(0, Math.min(255, lut[i]));
        }

        WritableImage equalizedImage = new WritableImage(width, height);
        PixelWriter writer = equalizedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                int brightness = (int) (0.299 * color.getRed() * 255 +
                        0.587 * color.getGreen() * 255 +
                        0.114 * color.getBlue() * 255);
                int newBrightness = lut[brightness];

                Color newColor = Color.grayRgb(newBrightness);
                writer.setColor(x, y, newColor);
            }
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(equalizedImage);
            selectedImage = equalizedImage;
        } else {
            imageViewOut.setImage(equalizedImage);
            outputImage = equalizedImage;
        }
    }

    public void applyThresholdFilter(double threshold) {
        if (threshold >= 0) {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage thresholdImage = new WritableImage(width, height);
            PixelReader pixelReader = selectedImage.getPixelReader();
            PixelWriter pixelWriter = thresholdImage.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = pixelReader.getColor(x, y);

                    double brightness = color.getBrightness();

                    Color binaryColor = brightness >= threshold ? Color.WHITE : Color.BLACK;

                    pixelWriter.setColor(x, y, binaryColor);
                }
            }

            if (onOriginalCheck.isSelected()) {
                imageViewIn.setImage(thresholdImage);
                selectedImage = thresholdImage;
            } else {
                imageViewOut.setImage(thresholdImage);
                outputImage = thresholdImage;
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверное значение");
            alert.setContentText("Пожалуйста, введите значение, не меньшее 0.");

            alert.showAndWait();
        }
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
            weightBackground += histogram[t];
            if (weightBackground == 0) continue;

            int weightForeground = totalPixels - weightBackground;
            if (weightForeground == 0) break;

            sumBackground += t * histogram[t];

            double meanBackground = sumBackground / weightBackground;
            double meanForeground = (sumAll - sumBackground) / weightForeground;

            double betweenClassVariance = weightBackground * weightForeground * Math.pow(meanBackground - meanForeground, 2);

            if (betweenClassVariance > maxBetweenClassVariance) {
                maxBetweenClassVariance = betweenClassVariance;
                bestThreshold = t;
            }
        }

        double threshold = bestThreshold / 255.0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double brightness = color.getBrightness();
                Color binaryColor = brightness >= threshold ? Color.WHITE : Color.BLACK;
                pixelWriter.setColor(x, y, binaryColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(OtsuThresholdImage);
            selectedImage = OtsuThresholdImage;
        } else {
            imageViewOut.setImage(OtsuThresholdImage);
            outputImage = OtsuThresholdImage;
        }
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

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redSum += color.getRed() * 255;
                            greenSum += color.getGreen() * 255;
                            blueSum += color.getBlue() * 255;
                            count++;
                        }
                    }
                }

                int avgRed = redSum / count;
                int avgGreen = greenSum / count;
                int avgBlue = blueSum / count;

                Color avgColor = Color.rgb(avgRed, avgGreen, avgBlue);
                pixelWriter.setColor(x, y, avgColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(homogeneousAveragingImage);
            selectedImage = homogeneousAveragingImage;
        } else {
            imageViewOut.setImage(homogeneousAveragingImage);
            outputImage = homogeneousAveragingImage;
        }
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

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

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

                int avgRed = (int) Math.min(255, (redSum / weightSum) * 255);
                int avgGreen = (int) Math.min(255, (greenSum / weightSum) * 255);
                int avgBlue = (int) Math.min(255, (blueSum / weightSum) * 255);

                Color avgColor = Color.rgb(avgRed, avgGreen, avgBlue);
                pixelWriter.setColor(x, y, avgColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(inhomogeneousAveragingImage);
            selectedImage = inhomogeneousAveragingImage;
        } else {
            imageViewOut.setImage(inhomogeneousAveragingImage);
            outputImage = inhomogeneousAveragingImage;
        }
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
                List<Integer> redValues = new ArrayList<>();
                List<Integer> greenValues = new ArrayList<>();
                List<Integer> blueValues = new ArrayList<>();

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            redValues.add((int) (color.getRed() * 255));
                            greenValues.add((int) (color.getGreen() * 255));
                            blueValues.add((int) (color.getBlue() * 255));
                        }
                    }
                }

                int medianRed = getMedian(redValues);
                int medianGreen = getMedian(greenValues);
                int medianBlue = getMedian(blueValues);

                Color medianColor = Color.rgb(medianRed, medianGreen, medianBlue);
                pixelWriter.setColor(x, y, medianColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(medianFilterImage);
            selectedImage = medianFilterImage;
        } else {
            imageViewOut.setImage(medianFilterImage);
            outputImage = medianFilterImage;
        }
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

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            maxRed = Math.max(maxRed, (int) (color.getRed() * 255));
                            maxGreen = Math.max(maxGreen, (int) (color.getGreen() * 255));
                            maxBlue = Math.max(maxBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                Color maxColor = Color.rgb(maxRed, maxGreen, maxBlue);
                pixelWriter.setColor(x, y, maxColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(maxFilterImage);
            selectedImage = maxFilterImage;
        } else {
            imageViewOut.setImage(maxFilterImage);
            outputImage = maxFilterImage;
        }
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

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);
                            minRed = Math.min(minRed, (int) (color.getRed() * 255));
                            minGreen = Math.min(minGreen, (int) (color.getGreen() * 255));
                            minBlue = Math.min(minBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                Color minColor = Color.rgb(minRed, minGreen, minBlue);
                pixelWriter.setColor(x, y, minColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(minFilterImage);
            selectedImage = minFilterImage;
        } else {
            imageViewOut.setImage(minFilterImage);
            outputImage = minFilterImage;
        }
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

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = pixelReader.getColor(nx, ny);

                            minRed = Math.min(minRed, (int) (color.getRed() * 255));
                            minGreen = Math.min(minGreen, (int) (color.getGreen() * 255));
                            minBlue = Math.min(minBlue, (int) (color.getBlue() * 255));

                            maxRed = Math.max(maxRed, (int) (color.getRed() * 255));
                            maxGreen = Math.max(maxGreen, (int) (color.getGreen() * 255));
                            maxBlue = Math.max(maxBlue, (int) (color.getBlue() * 255));
                        }
                    }
                }

                int midRed = (minRed + maxRed) / 2;
                int midGreen = (minGreen + maxGreen) / 2;
                int midBlue = (minBlue + maxBlue) / 2;

                Color midColor = Color.rgb(midRed, midGreen, midBlue);
                pixelWriter.setColor(x, y, midColor);
            }
        }
        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(midpointFilterImage);
            selectedImage = midpointFilterImage;
        } else {
            imageViewOut.setImage(midpointFilterImage);
            outputImage = midpointFilterImage;
        }
    }

    private void applyDilation(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage dilatationFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = dilatationFilterImage.getPixelWriter();

        if (whiteRadio.isSelected()) performDilation(filterSize, pixelReader, pixelWriter, true);
        else if (blackRadio.isSelected()) performDilation(filterSize, pixelReader, pixelWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(dilatationFilterImage);
            selectedImage = dilatationFilterImage;
        } else {
            imageViewOut.setImage(dilatationFilterImage);
            outputImage = dilatationFilterImage;
        }
    }

    private void applyErosion(int filterSize) {

        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage erosionFilterImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter pixelWriter = erosionFilterImage.getPixelWriter();

        if (whiteRadio.isSelected()) performErosion(filterSize, pixelReader, pixelWriter, true);
        else if (blackRadio.isSelected()) performErosion(filterSize, pixelReader, pixelWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(erosionFilterImage);
            selectedImage = erosionFilterImage;
        } else {
            imageViewOut.setImage(erosionFilterImage);
            outputImage = erosionFilterImage;
        }
    }

    private void applyClosing(int filterSize) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage dilatedImage = new WritableImage(width, height);
        WritableImage closingImage = new WritableImage(width, height);

        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter dilatedWriter = dilatedImage.getPixelWriter();

        if (whiteRadio.isSelected()) performDilation(filterSize, pixelReader, dilatedWriter, true);
        else if (blackRadio.isSelected()) performDilation(filterSize, pixelReader, dilatedWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
            return;
        }

        PixelReader dilatedReader = dilatedImage.getPixelReader();
        PixelWriter closingWriter = closingImage.getPixelWriter();

        if (whiteRadio.isSelected()) performErosion(filterSize, dilatedReader, closingWriter, true);
        else if (blackRadio.isSelected()) performErosion(filterSize, dilatedReader, closingWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(closingImage);
            selectedImage = closingImage;
        } else {
            imageViewOut.setImage(closingImage);
            outputImage = closingImage;
        }
    }

    private void applyOpening(int filterSize) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage erodedImage = new WritableImage(width, height);
        WritableImage openedImage = new WritableImage(width, height);

        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter erodedWriter = erodedImage.getPixelWriter();

        if (whiteRadio.isSelected()) performErosion(filterSize, pixelReader, erodedWriter, true);
        else if (blackRadio.isSelected()) performErosion(filterSize, pixelReader, erodedWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
            return;
        }

        PixelReader erodedReader = erodedImage.getPixelReader();
        PixelWriter openingWriter = openedImage.getPixelWriter();

        if (whiteRadio.isSelected()) performDilation(filterSize, erodedReader, openingWriter, true);
        else if (blackRadio.isSelected()) performDilation(filterSize, erodedReader, openingWriter, false);
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");

            alert.showAndWait();
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(openedImage);
            selectedImage = openedImage;
        } else {
            imageViewOut.setImage(openedImage);
            outputImage = openedImage;
        }
    }

    private void applyEdgeDetection(int filterSize) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage erodedImage = new WritableImage(width, height);
        PixelReader pixelReader = selectedImage.getPixelReader();
        PixelWriter erodedWriter = erodedImage.getPixelWriter();

        boolean isObjectWhite;
        if (whiteRadio.isSelected()) {
            isObjectWhite = true;
            performErosion(filterSize, pixelReader, erodedWriter, true);
        } else if (blackRadio.isSelected()) {
            isObjectWhite = false;
            performErosion(filterSize, pixelReader, erodedWriter, false);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");
            alert.showAndWait();
            return;
        }

        WritableImage edgeImage = new WritableImage(width, height);
        PixelReader erodedReader = erodedImage.getPixelReader();
        PixelWriter edgeWriter = edgeImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = pixelReader.getColor(x, y);
                Color erodedColor = erodedReader.getColor(x, y);

                double red, green, blue;
                if (isObjectWhite) {
                    red = Math.max(0, originalColor.getRed() - erodedColor.getRed());
                    green = Math.max(0, originalColor.getGreen() - erodedColor.getGreen());
                    blue = Math.max(0, originalColor.getBlue() - erodedColor.getBlue());
                } else {
                    red = Math.max(0, erodedColor.getRed() - originalColor.getRed());
                    green = Math.max(0, erodedColor.getGreen() - originalColor.getGreen());
                    blue = Math.max(0, erodedColor.getBlue() - originalColor.getBlue());
                }

                Color edgeColor = new Color(red, green, blue, 1.0);
                edgeWriter.setColor(x, y, edgeColor);
            }
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(edgeImage);
            selectedImage = edgeImage;
        } else {
            imageViewOut.setImage(edgeImage);
            outputImage = edgeImage;
        }
    }

    public void applySkeletonization() {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        PixelReader pixelReader = selectedImage.getPixelReader();
        WritableImage skeletonImage = new WritableImage(width, height);
        PixelWriter pixelWriter = skeletonImage.getPixelWriter();

        boolean isObjectWhite;
        if (whiteRadio.isSelected()) {
            isObjectWhite = true;
        } else if (blackRadio.isSelected()) {
            isObjectWhite = false;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не выбран ни один вариант");
            alert.setContentText("Пожалуйста, выберите цвет объекта.");
            alert.showAndWait();
            return;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(x, y);
                pixelWriter.setArgb(x, y, pixel);
            }
        }

        boolean[][] binaryData = new boolean[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                binaryData[x][y] = (pixelReader.getArgb(x, y) & 0xFFFFFF) == 0xFFFFFF;
            }
        }

        if (!isObjectWhite) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    binaryData[x][y] = (pixelReader.getArgb(x, y) & 0xFFFFFF) == 0x000000;
                }
            }
        }

        boolean changed;
        do {
            changed = false;
            boolean[][] tempData = new boolean[width][height];

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    if (binaryData[x][y]) {
                        int whiteNeighborCount = countWhiteNeighbors(binaryData, x, y);
                        int transitions = countTransitions(binaryData, x, y);

                        if (whiteNeighborCount >= 2 && whiteNeighborCount <= 6 && transitions == 1 &&
                                (!binaryData[x][y - 1] || !binaryData[x + 1][y] || !binaryData[x][y + 1] || !binaryData[x - 1][y])) {
                            tempData[x][y] = false;
                            changed = true;
                        } else {
                            tempData[x][y] = true;
                        }
                    }
                }
            }

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    binaryData[x][y] = tempData[x][y];
                }
            }

        } while (changed);


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (binaryData[x][y]) {
                    pixelWriter.setArgb(x, y, 0xFFFFFFFF);
                } else {
                    pixelWriter.setArgb(x, y, 0xFF000000);
                }
            }
        }

        if (!isObjectWhite) {
            WritableImage skeletonNegativeImage = new WritableImage(width, height);
            PixelReader reader = skeletonImage.getPixelReader();
            PixelWriter writer = skeletonNegativeImage.getPixelWriter();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    Color negativeColor = Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue(), color.getOpacity());
                    writer.setColor(x, y, negativeColor);
                }
            }
            skeletonImage = skeletonNegativeImage;
        }

        if (onOriginalCheck.isSelected()) {
            imageViewIn.setImage(skeletonImage);
            selectedImage = skeletonImage;
        } else {
            imageViewOut.setImage(skeletonImage);
            outputImage = skeletonImage;
        }
    }

    private int countWhiteNeighbors(boolean[][] data, int x, int y) {
        int count = 0;
        if (data[x - 1][y]) count++;
        if (data[x + 1][y]) count++;
        if (data[x][y - 1]) count++;
        if (data[x][y + 1]) count++;
        if (data[x - 1][y - 1]) count++;
        if (data[x + 1][y - 1]) count++;
        if (data[x - 1][y + 1]) count++;
        if (data[x + 1][y + 1]) count++;
        return count;
    }

    private int countTransitions(boolean[][] data, int x, int y) {
        int transitions = 0;
        int[][] neighbors = {{x - 1, y}, {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1},
                {x + 1, y}, {x + 1, y - 1}, {x, y - 1}, {x - 1, y - 1}};

        for (int i = 0; i < neighbors.length - 1; i++) {
            if (!data[neighbors[i][0]][neighbors[i][1]] && data[neighbors[i + 1][0]][neighbors[i + 1][1]]) {
                transitions++;
            }
        }
        return transitions;
    }

    private int getGrayScale(int argb) {
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        return (red + green + blue) / 3;
    }

    @FXML
    public void onShowHistogram() {
        if (selectedImage != null) {
            HistogramViewer histogramViewer = new HistogramViewer(selectedImage);
            histogramViewer.showHistogram();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
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

    private void performErosion(int filterSize, PixelReader reader, PixelWriter writer, boolean isObjectWhite) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();
        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double extremeValue = isObjectWhite ? 1.0 : 0.0;

                for (int j = -radius; j <= radius; j++) {
                    for (int i = -radius; i <= radius; i++) {
                        int nx = Math.min(width - 1, Math.max(0, x + i));
                        int ny = Math.min(height - 1, Math.max(0, y + j));

                        Color color = reader.getColor(nx, ny);
                        double gray = color.getRed() * 0.2989 + color.getGreen() * 0.5870 + color.getBlue() * 0.1140;

                        if (isObjectWhite) {
                            extremeValue = Math.min(extremeValue, gray);
                        } else {
                            extremeValue = Math.max(extremeValue, gray);
                        }
                    }
                }
                Color newColor = Color.gray(extremeValue);
                writer.setColor(x, y, newColor);
            }
        }
    }

    private void performDilation(int filterSize, PixelReader reader, PixelWriter writer, boolean isObjectWhite) {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();
        int radius = filterSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double extremeValue = isObjectWhite ? 0.0 : 1.0;

                for (int j = -radius; j <= radius; j++) {
                    for (int i = -radius; i <= radius; i++) {
                        int nx = Math.min(width - 1, Math.max(0, x + i));
                        int ny = Math.min(height - 1, Math.max(0, y + j));

                        Color color = reader.getColor(nx, ny);
                        double gray = color.getRed() * 0.2989 + color.getGreen() * 0.5870 + color.getBlue() * 0.1140;

                        if (isObjectWhite) {
                            extremeValue = Math.max(extremeValue, gray);
                        } else {
                            extremeValue = Math.min(extremeValue, gray);
                        }
                    }
                }
                Color newColor = Color.gray(extremeValue);
                writer.setColor(x, y, newColor);
            }
        }
    }

    private Color calculateAverageColor(PixelReader pixelReader, int width, int height) {
        double totalRed = 0;
        double totalGreen = 0;
        double totalBlue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                totalRed += color.getRed();
                totalGreen += color.getGreen();
                totalBlue += color.getBlue();
            }
        }

        int totalPixels = width * height;
        return Color.color(totalRed / totalPixels, totalGreen / totalPixels, totalBlue / totalPixels);
    }

    @FXML
    private void onFirstBorderPlus() {
        firstBorderText.setText(Integer.toString(Integer.parseInt(firstBorderText.getText()) + 1));
        if (onNowCheck.isSelected())
            applyBrightness(Integer.parseInt(firstBorderText.getText()), Integer.parseInt(secondBorderText.getText()), onInvertCheck.isSelected());
    }

    @FXML
    private void onSecondBorderPlus() {
        secondBorderText.setText(Integer.toString(Integer.parseInt(secondBorderText.getText()) + 1));
        if (onNowCheck.isSelected())
            applyBrightness(Integer.parseInt(firstBorderText.getText()), Integer.parseInt(secondBorderText.getText()), onInvertCheck.isSelected());
    }

    @FXML
    private void onFirstBorderMinus() {
        firstBorderText.setText(Integer.toString(Integer.parseInt(firstBorderText.getText()) - 1));
        if (onNowCheck.isSelected())
            applyBrightness(Integer.parseInt(firstBorderText.getText()), Integer.parseInt(secondBorderText.getText()), onInvertCheck.isSelected());
    }

    @FXML
    private void onSecondBorderMinus() {
        secondBorderText.setText(Integer.toString(Integer.parseInt(secondBorderText.getText()) - 1));
        if (onNowCheck.isSelected())
            applyBrightness(Integer.parseInt(firstBorderText.getText()), Integer.parseInt(secondBorderText.getText()), onInvertCheck.isSelected());
    }

}