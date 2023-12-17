package com.example.course_paper;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/* В данном файле описан класс для запуска приложения с помощью JavaFX, а так же: методы для анимации
    - методы для работы с кнопками, анимациями и основной логикой программы
    -
 */

public class ConquerGame extends Application {
    // JavaFx запускается с помощью метода launch() наследуемого класса Application
    public static void main(String[] args) {
        launch();
    }

    // Объявление констант для работы программы
    // cell_height и cell_width - размеры "полей"
    public static final int cell_height = 50;
    public static final int cell_width = 50;
    // Размеры окна программы
    private final int stage_height = 750;
    private final int stage_width = 750;
    private final int user_menu_width = 150;   // Ширина пользовательского меню

    // Количество полей (данная константа будет далее использоваться много раз, поэтому целесообразно её объявление)
    private final int cell_amount = stage_height * stage_width / cell_height / cell_width;
    // Количество столбцов и рядов "полей"
    private final int rows_amount = stage_height / cell_height;
    private final int columns_amount = stage_height / cell_width;

    public static Player player = new Player();
    public static Player AIplayer = new Player();

    public static Button rice_amount_indicator = new Button();
    public static Button water_amount_indicator = new Button();
    public static Button villagers_amount_indicator = new Button();
    public static Button game_message_indicator = new Button();
    public Button save_game_button = new Button();
    private World world;
    static public Group group = new Group();
    public static Stage stage_reference;

    // Создает кнопки-индикаторы (это по сути элемент графики, относящиеся к игровому процессу лишь количеством
    // ресурсов игроков, а так как объекты Player - статические, можно объявить этот метод в главном классе)
    public void createIndicators(){
        // Создание кнопок-индикаторов количества ресурсов
        rice_amount_indicator.setLayoutX(stage_width);
        rice_amount_indicator.setLayoutY(100);
        rice_amount_indicator.setText("Рис: " + player.rice_amount);
        water_amount_indicator.setLayoutX(stage_width);
        water_amount_indicator.setLayoutY(200);
        water_amount_indicator.setText("Вода: " + player.water_amount);
        villagers_amount_indicator.setLayoutX(stage_width);
        villagers_amount_indicator.setLayoutY(300);
        villagers_amount_indicator.setText("Крестьяне: " + player.villagers_amount);
        game_message_indicator.setLayoutX(stage_width);
        game_message_indicator.setLayoutY(400);
        game_message_indicator.setText("Ваш ход!");
        game_message_indicator.setPrefSize(this.user_menu_width, 100);

        save_game_button.setLayoutX(stage_width + user_menu_width - user_menu_width * 0.7);
        save_game_button.setLayoutY(stage_height - 75);
        save_game_button.setText("Сохранить игру");
        save_game_button.setPrefSize(user_menu_width * 0.7, 50);
        save_game_button.setOnAction(event -> this.world.saveGame());
    }

    @Override
    public void start(Stage stage) {
        stage_reference = stage;
        // Создаем объект класса Group для хранения всех кнопок
        group = new Group();
        // Создание главного меню
        this.createStartMenu();

        // Создаем окно интерфейса
        Scene scene = new Scene(group, this.stage_width + this.user_menu_width, this.stage_height);

        // Меняем надпись окна
        stage.setTitle("Conquer Game");
        stage.setScene(scene);
        stage.show();
    }

    private void startNewGame(){
        // Убираем кнопки главного меню
        group.getChildren().clear();

        this.world = new World(rows_amount, columns_amount, null);
        this.createIndicators();

        // Добавляем все кнопки полей в объект group вложенным циклом
        for (int i = 0; i < this.rows_amount; i++) {
            for (int j = 0; j < this.columns_amount; j++) {
                group.getChildren().add(world.getGridMember(i, j));
            }
        }
        // Добавляем кнопки - индикаторы
        group.getChildren().add(rice_amount_indicator);
        group.getChildren().add(water_amount_indicator);
        group.getChildren().add(villagers_amount_indicator);
        group.getChildren().add(game_message_indicator);
        // Добавляем кнопку сохранения игры
        group.getChildren().add(save_game_button);
    }

    private void createStartMenu(){
        // Кнопка "Новая игра, создает новое поле и игра начинается с нуля
        Button new_game_button = new Button();
        new_game_button.setLayoutX((stage_width + user_menu_width) / 2.0 - 75);
        new_game_button.setLayoutY(100);
        new_game_button.setPrefSize(150, 75);
        new_game_button.setText("Новая игра");
        new_game_button.setOnAction(event -> startNewGame());
        group.getChildren().add(new_game_button);

        // Кнопка "Продолжить игру", загружает выбранный файл с игрой
        Button save_game_button = new Button();
        save_game_button.setLayoutX((stage_width + user_menu_width) / 2.0 - 75);
        save_game_button.setLayoutY(300);
        save_game_button.setPrefSize(150, 75);
        save_game_button.setText("Продолжить игру");
        save_game_button.setOnAction(event -> this.getSaveFile());
        group.getChildren().add(save_game_button);

        // Кнопка "Выход", завершает работу программы
        Button exit_game_button = new Button();
        exit_game_button.setLayoutX((stage_width + user_menu_width) / 2.0 - 75);
        exit_game_button.setLayoutY(500);
        exit_game_button.setPrefSize(150, 75);
        exit_game_button.setText("Выход");
        exit_game_button.setOnAction(event -> System.exit(0));
        group.getChildren().add(exit_game_button);
    }

    private void getSaveFile(){
        // Запускаем окно выбора файла
        FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Выберите папку сохранения игры");
        // Сохраняем выбранную директорию
        File file = file_chooser.showOpenDialog(stage_reference);
        // Создаем новый мир с указанием пути к файлу сохранения
        this.world = new World(this.rows_amount, this.columns_amount, file.getAbsolutePath());

        group.getChildren().clear();

        // Добавляем все кнопки полей в объект group вложенным циклом
        for (int i = 0; i < this.rows_amount; i++) {
            for (int j = 0; j < this.columns_amount; j++) {
                group.getChildren().add(world.getGridMember(i, j));
            }
        }
        // Создаем кнопки - индикаторы
        this.createIndicators();
        // Добавляем кнопки - индикаторы
        group.getChildren().add(rice_amount_indicator);
        group.getChildren().add(water_amount_indicator);
        group.getChildren().add(villagers_amount_indicator);
        group.getChildren().add(game_message_indicator);
        // Добавляем кнопку сохранения игры
        group.getChildren().add(save_game_button);
    }

}