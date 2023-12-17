package com.example.course_paper;

import javafx.scene.control.Button;

// Класс Поле -
public class Field extends Button {
    // Переменная type хранит значение Типа Поля - {0 - пустое; 1 - рис; 2 - вода; 3 - дом}
    final private int type;
    private int required_villagers_amount;
    private int x_pos, y_pos;
    private boolean is_occupied = false;
    public Player owner = null; //
    private int x_i, y_j;

    public Field(int type_value, int i, int j, int x, int y){   // Присваивает Полю значение
        super();
        this.type = type_value;
        // Присвоение текстуры
        this.setTexture();
        // Присвоение порядковых номеров i и j нахождения в двумерном массиве
        this.setRelativePos(i, j);
        // Присвоение координат x и y полю grid[i][j] для правильного отображения
        this.setGraphicsPos(x, y);


        // Присвоение координат x и y кнопке grid[i][j] для правильного отображения (для JavaFX)
        this.setLayoutX(x_pos);
        this.setLayoutY(y_pos);
    }

    public int getType(){   // Возвращает значение типа поля
        return this.type;
    }

    // Функция для присваивания количества требуемых крестьян для захвата
    public void setRequired_villagers_amount(int required_villagers_amount_value){
        this.required_villagers_amount = required_villagers_amount_value;
    }

    public int getRequired_villagers_amount(){   // Возвращает количество требуемых крестьян для захвата
        return this.required_villagers_amount;
    }

    public void setGraphicsPos(int x, int y){    // Присваивает полю координаты x и y (координаты по пикселям)
        this.x_pos = x;
        this.y_pos = y;
    }
    public int getX(){   // Возвращает x координату поля
        return this.x_pos;
    }
    public int getY(){   // Возвращает y координату поля
        return this.y_pos;
    }

    public void conquer(Player player, int villagers_amount_decrease){
        this.is_occupied = true;
        this.owner = player;
        player.give_field(this.x_i, this.y_j);
        this.setText("");
        // Если данное поле - поле воды или поле риса - увеличиваем количество соответствующих полей данного игрока
        if (this.getType() == 1){
            player.rice_fields_amount++;
        } else if (this.getType() == 2){
            player.water_fields_amount++;
        }
        if (villagers_amount_decrease != 0) {
            // Увеличиваем общее количество полей во владении данного игрока
            player.overall_fields_amount++;
            // Уменьшаем количество риса игрока
            player.rice_amount -= this.required_villagers_amount;
            // Уменьшаем количество жителей на случайную величину, сгенерированную в прошлой функции
            player.villagers_amount -= villagers_amount_decrease;
        }
        // Меняет цвет если поле принадлежит ИИ
        if (player == ConquerGame.AIplayer) {
            this.setStyle("-fx-background-color:#b786f7;");
        } else if (player == ConquerGame.player) {
            this.setStyle("-fx-background-color:#8bfcc9;");
        }
    }

    public boolean isUnoccupied(){
        return !this.is_occupied;
    }

    public void setRelativePos(int i, int j){
        this.x_i = i;
        this.y_j = j;
    }
    public int getXI(){
        return this.x_i;
    }
    public int getYJ(){
        return this.y_j;
    }

    public void setTexture(){
    }
    public void giveRice(){
    }
    public void waterRiceField(){
    }
    public void spawnVillager() {
    }
}
