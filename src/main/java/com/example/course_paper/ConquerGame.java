package com.example.course_paper;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ConquerGame extends Application {

    // JavaFx запускается с помощью метода launch() наследуемого класса Application
    public static void main(String[] args) {
        launch();
    }

    // Объявление констант для работы программы
    // cell_height и cell_width - размеры "полей"
    // ПАВЕЛ ИВАНОВИЧ, ЕСЛИ БУДЕТЕ ТЕСТИТЬ ПОСТАВЬТЕ ИХ РАВНЫМИ 150
    public static final int cell_height = 50;   // 150
    public static final int cell_width = 50;   // 150

    // Размеры окна программы
    private final int stage_height = 750;
    private final int stage_width = 750;
    private final int user_menu_width = 150;   // Ширина пользовательского меню

    // Количество столбцов и рядов "полей"
    private final int rows_amount = stage_height / cell_height;
    private final int columns_amount = stage_height / cell_width;

    // Объявление объектов класса Player
    public static Player player = new Player();   // Это игрок
    public static Player AIplayer = new Player();   // Это ИИ

    // Объявление кнопок-индикаторов
    public static Button rice_amount_indicator = new Button();   // Индикатор количества риса
    public static Button water_amount_indicator = new Button();   // Индикатор количества воды
    public static Button villagers_amount_indicator = new Button();   // Индикатор количества крестьян
    public static Button game_message_indicator = new Button();   // Индикатор с сообщениями для игрока (неправильные действия, не хватает ресурсов)
    public Button save_game_button = new Button();   // Кнопка сохранения игры
    private World world;   // Объявление объекта класса World через который проходят все вычисления и основная часть программы
    static public Group group = new Group();   // "Группа" для хранения всех объектов интерфейса JavaFx
    public static Stage stage_reference;   // Референс на переменную stage, нужен для доступа к сцене из методов класса World

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

    // Главный метод start, запускающий приложение при запуске main()
    @Override
    public void start(Stage stage) {
        // Создаем референс на переменную stage для доступа к экрану
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
        try {
            exit_game_button.setOnAction(event -> System.exit(0));
        } catch (NullPointerException e){
            game_message_indicator.setText("Не удалось загрузить");
            group.getChildren().add(game_message_indicator);
        }
        group.getChildren().add(exit_game_button);
    }

    private void getSaveFile() throws NullPointerException{
        // Запускаем окно выбора файла
        FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Выберите папку сохранения игры");
        // Сохраняем выбранную директорию
        File file = file_chooser.showOpenDialog(stage_reference);
        group.getChildren().clear();
        // Создаем новый мир с указанием пути к файлу сохранения
        this.world = new World(this.rows_amount, this.columns_amount, file.getAbsolutePath());
        // Если произошла ошибка во время считывания файла - программа завершается, интерфейс остается открытым
        if (game_message_indicator.getText().startsWith("Невозможно открыть")){
            return;
        }
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