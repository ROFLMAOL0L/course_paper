package com.example.course_paper;

import javafx.scene.Group;

import java.util.Random;

public class World {
    // Создание двумерного массива для хранения объектов класса Field
    private Field[][] grid;
    final private int rows_amount, columns_amount;
    // Значение порядкового номера серединного столбца и ряда
    private final int average_column_amount;
    private final int average_rows_amount;
    public final int cell_width = ConquerGame.cell_width;

    public final int cell_height= ConquerGame.cell_height;
    // Создание объект класса Random для генерации псевдо-случайных чисел
    Random random_integer_generator = new Random();
    private final int[][] start_fields_i_j;;

    public World(int rows_amount, int columns_amount){
        // Создание двумерного массива для хранения всех объектов класса Field
        this.grid = new Field[rows_amount][columns_amount];
        this.rows_amount = rows_amount;
        this.columns_amount = columns_amount;
        this.average_column_amount = columns_amount / 2;
        this.average_rows_amount = rows_amount / 2;
        start_fields_i_j = new int[][] {{0, 0}, {1, 0}, {0, 1},
                                        {rows_amount - 1, columns_amount - 1},
                                        {rows_amount - 1, columns_amount - 2},
                                        {rows_amount - 2, columns_amount - 1}};
        this.generateGrid();
    }


    // Генерация случайного поля из возможных вариантов {рис; вода; пустое поле}
    private void generateGrid(){
        // Присваиваем начальные поля игрокам
        this.createStartFields();
        // Заполнение двумерного массива объектов класса Field
        for (int i = 0; i < this.rows_amount; i++) {
            outer_loop:
            for (int j = 0; j < this.columns_amount; j++) {
                for (int k = 0; k < this.start_fields_i_j.length; k ++) {
                    if (this.start_fields_i_j[k][0] == i && this.start_fields_i_j[k][1] == j){
                        continue outer_loop;
                    }
                }
                // Заполнение случайным полем из списка {0 - пустое; 1 - рис; 2 - вода; 3 - дом} (все кроме 3)
                this.addField(random_integer_generator.nextInt(3), i, j,
                           i * this.cell_width, j * cell_height, calculateRequired_villagers_amount(i, j));

                // Присвоение размера полю grid[i][j]
                grid[i][j].setPrefSize(this.cell_width, this.cell_height);

                if (this.is_next(i, j, ConquerGame.player)){
                    grid[i][j].setDisable(false);
                } else {
                    grid[i][j].setDisable(true);
                }

                // Указываем на функцию, которая будет выполняться при нажатии кнопки.
                Field temp_field = grid[i][j];   // Создание переменной для передачи в lambda функцию
                grid[i][j].setOnAction(event -> {
                    handleFieldClick(temp_field);
                });
            }
        }
    }

    private int calculateRequired_villagers_amount(int i, int j){
        int temp_integer;
        int delta_i, delta_j;

        // Присвоение количества требуемых жителей для захвата поля по математической модели
        delta_j = Math.abs(this.average_column_amount - j);
        delta_i = Math.abs(this.average_rows_amount - i);
        temp_integer = Math.abs(this.columns_amount - (delta_i + delta_j));

        // Добавляем i и j по мере приближения к центру, чтобы количество требуемых крестьян неубывало
        if (i + j < this.average_rows_amount + this.average_column_amount){
            temp_integer += i + j;
        } else {
            temp_integer += this.rows_amount - i + this.columns_amount - j - 2;
        }
        return temp_integer;
    }

    public void addField(int type, int x_i, int y_j, int x, int y, int required_villages_amount){
        if (type == 0){
            grid[x_i][y_j] = new EmptyField(type, x_i, y_j, x, y, required_villages_amount);
        } else if (type == 1){
            grid[x_i][y_j] = new RiceField(type, x_i, y_j, x, y, required_villages_amount);
        } else if (type == 2){
            grid[x_i][y_j] = new WaterField(type, x_i, y_j, x, y, required_villages_amount);
        }
    }

    public Field getGridMember(int i, int j){
        return this.grid[i][j];
    }

    public boolean is_next(int x, int y, Player player){
        for (int i = 0; i < player.x_fields.size(); i++){
            if ((player.x_fields.get(i) == x - 1) && (player.y_fields.get(i) == y)){
                return true;
            } else if ((player.x_fields.get(i) == x) && (player.y_fields.get(i) == y - 1)){
                return true;
            } else if ((player.x_fields.get(i) == x + 1) && (player.y_fields.get(i) == y)){
                return true;
            } else if ((player.x_fields.get(i) == x) && (player.y_fields.get(i) == y + 1)){
                return true;
            } else if ((player.x_fields.get(i) == x) && (player.y_fields.get(i) == y)){
                return true;
            }
        }
        return false;
    }

    public void refreshGrid(){
        for (int i = 0; i < this.rows_amount; i++){
            for (int j = 0; j < this.columns_amount; j++){
                if (this.is_next(i, j, ConquerGame.player)){
                    this.grid[i][j].setDisable(false);
                }
            }
        }
    }
    public void buildHouse(int i, int j, Player player){
        ConquerGame.group.getChildren().remove(grid[i][j]);
        HouseField house =  new HouseField(3, i, j, i * this.cell_width + 1, j * this.cell_height + 1, player);
        this.grid[i][j] = house;
        this.grid[i][j].conquer(player);
        ConquerGame.group.getChildren().add(this.getGridMember(i, j));
        this.refreshGrid();
    }

    public void handleFieldClick(Field field_sample) {
        if ((field_sample.isUnoccupied()) && (ConquerGame.player.villagers_amount >= field_sample.getRequired_villagers_amount())){
            field_sample.conquer(ConquerGame.player);
            this.refreshGrid();
        } else if ((!field_sample.isUnoccupied()) && (field_sample.getType() == 1)){
            // Полить весь рис
        } else if (field_sample.belongs_to == ConquerGame.player && field_sample.getType() == 0) {
            this.buildHouse(field_sample.getXI(), field_sample.getYJ(), ConquerGame.player);
        }
        ConquerGame.player.give_rice();
    }

    public void createStartFields(){
        for (int k = 0; k < this.start_fields_i_j.length; k++){
            // Временные переменные для читаемости кода
            int i = this.start_fields_i_j[k][0];
            int j = this.start_fields_i_j[k][1];

            // Создаем нейтральное поле
            this.addField(k % 3, i, j, i * this.cell_width, j * this.cell_height, 0);

            // Присваиваем поле игроку или компьютеру
            if (i < 3 ) {
                grid[i][j].conquer(ConquerGame.AIplayer);
            } else {
                grid[i][j].conquer(ConquerGame.player);
            }

            // Выставляем размеры и текст кнопки (для JavaFX)
            grid[i][j].setPrefSize(this.cell_width, this.cell_height);
            grid[i][j].setText("");

            // Создание переменной для передачи в lambda функцию
            Field temp_field = grid[i][j];
            // Присваиваем функцию, которая будет запускаться при нажатии кнопки.
            grid[i][j].setOnAction(event -> {
                handleFieldClick(temp_field);
            });

            // Включаем кнопки игрока, выключаем кнопки ИИ
            if (i > 2) {
                grid[i][j].setDisable(false);
            } else {
                grid[i][j].setDisable(true);
            }
        }
    }
}
