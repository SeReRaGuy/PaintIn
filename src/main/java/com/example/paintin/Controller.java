package com.example.paintin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;

import java.io.File;

public class Controller {
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
    private Button applyEffectButton;
    @FXML
    private Slider imageSlider;
    private Image selectedImage;
    private FileChooser chooser = new FileChooser();


    @FXML
    public void initialize()
    {
        effectComboBox.getItems().add("Негатив");
        effectComboBox.getItems().add("Гамма-коррекция");
        effectComboBox.getItems().add("Градиент Робертса");
        effectComboBox.getItems().add("Градиент Собела");

        gammaSlider.setMin(0.0);
        gammaSlider.setMax(25);

        imageSlider.setMin(-20);
        imageSlider.setMax(100);


        effectComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue != null)
                {
                    if (newValue.equals("Негатив"))
                    {
                        gammaSlider.setVisible(false);
                        gammaText.setVisible(false);
                    }
                    else if (newValue.equals("Гамма-коррекция"))
                    {
                        gammaSlider.setVisible(true);
                        gammaText.setVisible(true);
                    }
                    else if (newValue.equals("Градиент Робертса"))
                    {
                        gammaSlider.setVisible(false);
                        gammaText.setVisible(false);
                    }
                    else if (newValue.equals("Градиент Собела"))
                    {
                        gammaSlider.setVisible(false);
                        gammaText.setVisible(false);
                    }
                }
            }
        });


        //Изменение гамма-коррекции происходит и по кнопке, и при изменении ползунка
        gammaSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (selectedImage != null && gammaSlider.isVisible())
                {
                    applyGammaCorrection(gammaSlider.getValue());
                    helpText.setText(String.valueOf(gammaSlider.getValue()));
                    gammaText.setText(String.valueOf(gammaSlider.getValue()));
                }

            }
        });

        //Изменение смещения изображения при изменении ползунка
        imageSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (selectedImage != null)
                {
                    moveImage(imageSlider.getValue());
                    helpText.setText(String.valueOf(imageSlider.getValue()));
                }
            }
        });
    }
    @FXML
    public void onChooseImage()
    {
        chooser.setTitle("Выберите изображение");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg","*.jpeg","*.png"));
        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null)
        {
            selectedImage = new Image(selectedFile.toURI().toString());
            imageViewIn.setImage(selectedImage);
            helpText.setText(selectedImage.getHeight() + " / " + selectedImage.getWidth() + " = " + (selectedImage.getHeight()/selectedImage.getWidth()));
        }
    }

    @FXML
    public void onApplyEffect()
    {
        if (selectedImage != null)
        {
            String selectedEffect = effectComboBox.getValue();
            if (selectedEffect != null)
            {
                if (selectedEffect.equals("Негатив")) applyNegative();
                else if (selectedEffect.equals("Гамма-коррекция")) applyGammaCorrection(Double.parseDouble(gammaText.getText()));
                else if (selectedEffect.equals("Градиент Робертса")) applyRobertsOperator();
                else if (selectedEffect.equals("Градиент Собела")) applySobelOperator();
            }
        }
    }

    private void applyNegative()
    {
        if (selectedImage != null)
        {
            int width = (int) selectedImage.getWidth();
            int height = (int) selectedImage.getHeight();

            WritableImage negativeImage = new WritableImage(width, height);
            PixelReader reader = selectedImage.getPixelReader();
            PixelWriter writer = negativeImage.getPixelWriter();

            for (int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    Color color = reader.getColor(x,y);
                    Color negativeColor = Color.color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue(), color.getOpacity());
                    writer.setColor(x,y,negativeColor);
                }
            }
            imageViewOut.setImage(negativeImage);

        }
    }

    private void applyGammaCorrection(double gamma)
    {
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();

        WritableImage gammaImage = new WritableImage(width, height);
        PixelReader reader = selectedImage.getPixelReader();
        PixelWriter writer = gammaImage.getPixelWriter();

        for (int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                Color color = reader.getColor(x,y);
                Color gammaColor = Color.color(Math.pow(color.getRed(), gamma), Math.pow(color.getGreen(), gamma), Math.pow(color.getBlue(), gamma), color.getOpacity());
                writer.setColor(x,y,gammaColor);
            }
        }

        imageViewOut.setImage(gammaImage);
    }

    private void applyRobertsOperator()
    {
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

    private int getGrayScale(int argb) {
        int red = (argb >> 16) & 0xFF; // Побитовые операции, >> для сдвига и получения отдельных частей 4х битного argb, & 0xFF - умножение на маску
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        return (red + green + blue) / 3;
    }

    private void moveImage(double x)
    {
        imageViewIn.setX((selectedImage.getHeight()/selectedImage.getWidth())*x);
        imageViewOut.setX((selectedImage.getHeight()/selectedImage.getWidth())*x);
    }
}