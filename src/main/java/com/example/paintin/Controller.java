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
                    if (newValue.equals("Гамма-коррекция"))
                    {
                        gammaSlider.setVisible(true);
                        gammaText.setVisible(true);
                    }
                    else
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

    private void moveImage(double x)
    {
        imageViewIn.setX((selectedImage.getHeight()/selectedImage.getWidth())*x);
        imageViewOut.setX((selectedImage.getHeight()/selectedImage.getWidth())*x);
    }
}