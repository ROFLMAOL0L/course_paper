package com.example.course_paper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.Random;

/* В данном файле описан класс для запуска приложения с помощью JavaFX, а так же: методы для анимации
    - методы для работы с кнопками, анимациями и основной логикой программы
    -
 */

public class HelloApplication extends Application {
    // JavaFx запускается с помощью метода launch() наследуемого класса Application
    public static void main(String[] args) {
        launch();
    }

    // Объявление констант для работы программы
    // cell_height и cell_width - размеры "полей"
    private final int cell_height = 50;
    private final int cell_width = 50;
    // Размеры окна программы
    private final int stage_height = 750;
    private final int stage_width = 750;

    // Количество полей (данная константа будет далее использоваться много раз, поэтому имеет место быть её объявление)
    private final int cell_amount = stage_height * stage_width / cell_height / cell_width;
    // Количество столбцов и рядов "полей"
    private final int rows_amount = stage_height / cell_height;
    private final int columns_amount = stage_height / cell_width;
    // Значение порядкового номера серединного столбца и ряда
    private final int average_column_amount = columns_amount / 2;
    private final int average_rows_amount = rows_amount / 2;

    public void handle_click(Button button) {
        button.setDisable(true);
    }
    public void handle_field_click(Field field_sample) {
        field_sample.setText(Integer.toString(field_sample.getType()));
        field_sample.setDisable(true);
    }

    @Override
    public void start(Stage stage) {
        // Создание объект класса Random для генерации псевдо-случайных чисел
        Random random_generator = new Random();


        // Создание двумерного массива для хранения всех объектов класса Field
        Field[][] grid = new Field[this.rows_amount][this.columns_amount];

        int temp_integer;
        int delta_i, delta_j;
        // Заполнение двумерного массива объектов класса Field
        for (int i = 0; i < this.rows_amount; i++) {
            for (int j = 0; j < this.columns_amount; j++) {

                // Генерация случайного поля из возможных вариантов {рис; вода; пустое поле}
                grid[i][j] = new Field(random_generator.nextInt(4));

                // Присвоение количества требуемых жителей для захвата поля по математической модели
                delta_j = Math.abs(this.average_column_amount - j);
                delta_i = Math.abs(this.average_rows_amount - i);
                grid[i][j].setRequired_villagers_amount(Math.abs(this.columns_amount - (delta_i + delta_j)));

                // Присвоение координат x и y полю grid[i][j] для правильного отображения
                grid[i][j].setLayoutX(this.cell_width * i);
                grid[i][j].setLayoutY(this.cell_height * j);

                // Присвоение размера полю grid[i][j]
                grid[i][j].setPrefSize(this.cell_width, this.cell_height);

                // Изменяем надпись на значение "сложности" захвата каждого поля
                String temp_text = Integer.toString(grid[i][j].getRequired_villagers_amount());
                grid[i][j].setText(temp_text);

                // Создаем объект класса Field и храним его в двумерном массиве grid[][] под его индексами i и j
                Field temp_field = grid[i][j];

                // Указываем на функцию, которая будет выполняться при нажатии кнопки.
                grid[i][j].setOnAction(event -> {
                    handle_field_click(temp_field);
                });
            }
        }

        // Создаем объект класса Group для хранения всех кнопок, добавляем их в этот объект вложенным циклом
        Group group = new Group();
        for (int i = 0; i < this.rows_amount; i++) {
            for (int j = 0; j < this.columns_amount; j++) {
                group.getChildren().add(grid[i][j]);
            }
        }

        // Создаем окно интерфейса
        Scene scene = new Scene(group, this.stage_width, this.stage_height);

        // Меняем надпись окна
        stage.setTitle("Conquer Game");
        stage.setScene(scene);
        stage.show();
    }

}