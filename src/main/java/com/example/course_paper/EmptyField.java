package com.example.course_paper;

import javafx.scene.control.ContentDisplay;

public class EmptyField extends Field{
    public EmptyField(int type_value, int i, int j, int x, int y, int required_villagers_amount){
        super(type_value, i, j, x, y);
        super.setRequired_villagers_amount(required_villagers_amount);

        // Изменяем надпись на значение "сложности" захвата каждого поля
        this.setText(Integer.toString(required_villagers_amount));
        this.setContentDisplay(ContentDisplay.TOP);
    }
}
