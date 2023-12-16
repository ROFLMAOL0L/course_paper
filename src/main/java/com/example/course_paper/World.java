package com.example.course_paper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class World {
    // Создание двумерного массива для хранения объектов класса Field
    private Field[][] grid;
    final private int rows_amount, columns_amount;
    // Значение порядкового номера серединного столбца и ряда
    private final int average_column_amount;
    private final int average_rows_amount;
    public final int cell_width = ConquerGame.cell_width;

    public final int cell_height = ConquerGame.cell_height;
    // Создание объект класса Random для генерации псевдо-случайных чисел
    Random random_integer_generator = new Random();
    private final int[][] start_fields_i_j;
    // Массив из булевых значений, каждое определяет возможность ИИ совершить действие
    // {"Набрать воды", "Полить рис", "Построить дом", "Захватить поле"}
    private final boolean[] possible_AI_moves = {false, false, false, false};

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
        // (см отчет "Создание экономической модели")
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
        return grid[i][j];
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
                if (this.grid[i][j].owner == ConquerGame.player && this.grid[i][j].getType() == 3){
                    this.grid[i][j].setDisable(true);
                }
            }
        }
    }
    public void buildHouse(int i, int j, Player player){
        // Удаляем кнопку пустого поля из массива кнопок group
        ConquerGame.group.getChildren().remove(grid[i][j]);
        // Создаем новое поле дома
        HouseField house =  new HouseField(3, i, j, i * this.cell_width + 1, j * this.cell_height + 1, player);
        // Перезаписываем соответствующее поле в двумерном массиве grid[][]
        this.grid[i][j] = house;
        // Присваиваем полю владельца
        this.grid[i][j].conquer(player, 0);
        // Добавляем кнопку поля дома в массив из кнопок group
        ConquerGame.group.getChildren().add(this.getGridMember(i, j));
        // Обновляем доступность полей
        this.refreshGrid();
        if (player == ConquerGame.AIplayer){
            this.grid[i][j].setDisable(true);
        }
    }

    public void handleFieldClick(Field field_sample) {
        // Обработка случая, когда игрок нажал на пустое незахваченное поле
        if (canConquer(ConquerGame.player, field_sample)){
            field_sample.conquer(ConquerGame.player,
                    random_integer_generator.nextInt(field_sample.getRequired_villagers_amount()));
            this.endTurn();
        }
        // Обработка случая, когда игрок нажал на свое поле риса (полив всего риса)
        else if (canWaterRice(ConquerGame.player, field_sample)){
            // Полить весь рис
            this.waterRice(ConquerGame.player);
            this.endTurn();
        }
        // Обработка случая, когда игрок нажал на свое пустое поле (построить дом)
        else if (canBuildHouse(ConquerGame.player, field_sample)) {
            this.buildHouse(field_sample.getXI(), field_sample.getYJ(), ConquerGame.player);
            this.endTurn();
        }
        // Обработка случая, когда игрок нажал на свое поле с водой (набрать воды)
        else if (isPlayersWaterField(ConquerGame.player, field_sample)){
            ConquerGame.player.collectWater();
            this.endTurn();
        }
        refreshGrid();
    }

    // Возвращает true если данный игрок может захватить данное поле
    private boolean canConquer(Player player, Field field_sample){
        if (field_sample.isUnoccupied() && (player.villagers_amount >= field_sample.getRequired_villagers_amount()) &&
                (player.rice_amount >= field_sample.getRequired_villagers_amount())){
            return true;
        } else if (player.villagers_amount < field_sample.getRequired_villagers_amount() && player == ConquerGame.player){
            ConquerGame.game_message_indicator.setText("Недостаточно крестьян!");
            return false;
        } else if (player.rice_amount < field_sample.getRequired_villagers_amount() && player == ConquerGame.player){
            ConquerGame.game_message_indicator.setText("Недостаточно риса!");
            return false;
        } else {
            return false;
        }
    }
    private boolean canWaterRice(Player player, Field field_sample){
        if ((field_sample.owner == player && (field_sample.getType() == 1)) && player.water_amount > 0){
            return true;
        } else if (player.water_amount == 0  && player == ConquerGame.player){
            ConquerGame.game_message_indicator.setText("У вас нет воды!");
            return false;
        }
        else {
            return false;
        }
    }

    private boolean isPlayersWaterField(Player player, Field field_sample){
        if ((field_sample.owner == player && (field_sample.getType() == 2))){
            return true;
        } else {
            return false;
        }
    }

    private boolean canBuildHouse(Player player, Field field_sample){
        // Если поле принадлежит игроку и это поле - пустое (EmptyField)
        if (field_sample.owner == player && field_sample.getType() == 0){
            // Если не хватает риса
            if (player.rice_amount < player.house_rice_cost){
                // Если метод вызван игроком, то выводим сообщение о том, что для постройки дома не хватает риса
                if (player == ConquerGame.player) {
                    ConquerGame.game_message_indicator.setText("Не хватает риса!");
                }
                return false;
            }
            // Если не хватает жителей
            else if (player.villagers_amount < player.house_villager_cost){
                // Если метод вызван игроком, то выводим сообщение о том, что для постройки дома не хватает крестьян
                if (player == ConquerGame.player) {
                    ConquerGame.game_message_indicator.setText("Не хватает крестьян!");
                }
                return false;
            }
            // Всего хватает
            else {
                return true;
            }
        } else {
            return false;
        }
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
                grid[i][j].conquer(ConquerGame.AIplayer, 0);
            } else {
                grid[i][j].conquer(ConquerGame.player, 0);
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
    private void endTurn(){
        if (ConquerGame.player.overall_fields_amount >= (this.rows_amount * this.columns_amount / 2)) {
            //ConquerGame.group.getChildren().clear();
            ConquerGame.game_message_indicator.setText("Вы выиграли!!!");
            ConquerGame.game_message_indicator.autosize();
            ConquerGame.group.getChildren().add(ConquerGame.game_message_indicator);
        } else if (ConquerGame.AIplayer.overall_fields_amount >= (this.rows_amount * this.columns_amount / 2)){
            //ConquerGame.group.getChildren().clear();
            ConquerGame.game_message_indicator.setText("Вы проиграли...");
            ConquerGame.game_message_indicator.autosize();
            ConquerGame.group.getChildren().add(ConquerGame.game_message_indicator);
        }
        for (int i = 0; i < this.rows_amount; i++){
            for (int j = 0; j < this.columns_amount; j++){
                // Создание временной переменно в теле цикла чтобы не обращаться к ней каждый раз через двумерный массив
                Field temp_field = this.grid[i][j];
                // Даем рис, если
                if (temp_field.getType() == 1){
                    temp_field.giveRice();
                } else if (temp_field.getType() == 3) {
                    temp_field.spawnVillager();
                }
            }
        }
        this.handleAITurn();

        ConquerGame.rice_amount_indicator.setText("Рис: " + ConquerGame.player.rice_amount);
        ConquerGame.villagers_amount_indicator.setText("Крестьяне:  " + ConquerGame.player.villagers_amount);
        ConquerGame.water_amount_indicator.setText("Вода: " + ConquerGame.player.water_amount);
        ConquerGame.game_message_indicator.setText("Ваш ход!");
    }
    public void waterRice(Player player){   // Небольшое уточнение, слово water здесь идет как глагол, по всем правилам английского
        int temp_V = player.villagers_amount;
        int temp_R = player.rice_fields_amount;
        int x = 0;

        while (x < player.x_fields.size() && player.water_amount > 0 && temp_V > 0 && temp_R > 0){
            Field temp_field = this.getGridMember(player.x_fields.get(x), player.y_fields.get(x));
            if (temp_field.getType() == 1 && temp_field.owner == player){
                temp_field.waterRiceField();
                temp_V--;
                temp_R--;
                player.water_amount--;
            }
            x++;
        }
    }

    private void handleAITurn(){
        // Обнуляем массив с возможными ходами
        Arrays.fill(this.possible_AI_moves, false);

        List<Field> fields_possible_to_conquer = new ArrayList<>();
        Field empty_owned_field = this.grid[0][0]; // Иначе java считает, что переменная может быть не инициализирована
        for (int i = 0; i < this.rows_amount; i++){
            for (int j = 0; j < this.columns_amount; j++){
                // Объявляем временные локальные переменные
                Field temp_field = this.getGridMember(i, j);
                if (temp_field.isUnoccupied()){
                    if (this.is_next(i, j, ConquerGame.AIplayer) && this.canConquer(ConquerGame.AIplayer, temp_field)){
                        this.possible_AI_moves[3] = true;
                        fields_possible_to_conquer.add(temp_field);
                    }
                } else {
                    if (temp_field.owner == ConquerGame.AIplayer) {
                        if (temp_field.getType() == 1 && this.canWaterRice(ConquerGame.AIplayer, temp_field)){
                            this.possible_AI_moves[1] = true;
                        } else if (temp_field.getType() == 2){
                            this.possible_AI_moves[0] = true;
                        } else if (temp_field.getType() == 0 && this.canBuildHouse(ConquerGame.AIplayer, temp_field)){
                            this.possible_AI_moves[2] = true;
                            empty_owned_field = temp_field;
                        }
                    }
                }
            }
        }
        if (this.possible_AI_moves[3]){
            // Локальные переменные для нахождения наиболее приоритетного поля для захвата

            Field chosen_field = fields_possible_to_conquer.get(0);
            int temp_min = 100;
            for (int k = 0; k < fields_possible_to_conquer.size(); k++){
                Field temp_field = fields_possible_to_conquer.get(k);
                if (temp_field.getRequired_villagers_amount() < temp_min){
                    chosen_field = temp_field;
                    temp_min = temp_field.getRequired_villagers_amount();
                }
            }
            // ХОД 3: завоевать территорию
            chosen_field.conquer(ConquerGame.AIplayer,
                    random_integer_generator.nextInt(chosen_field.getRequired_villagers_amount()));
        } else if (this.possible_AI_moves[2]) {
            // ХОД 2: построить дом
            this.buildHouse(empty_owned_field.getXI(), empty_owned_field.getYJ(), ConquerGame.AIplayer);
        } else if (this.possible_AI_moves[1]){
            // ХОД 1: полить рис
            this.waterRice(ConquerGame.AIplayer);
        } else if (this.possible_AI_moves[0]){
            // ХОД 0: набрать воды
            ConquerGame.AIplayer.collectWater();
        }
    }
}
