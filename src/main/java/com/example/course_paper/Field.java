package com.example.course_paper;

import javafx.scene.control.Button;

// Класс Поле -
public class Field extends Button {
    // Переменная type хранит значение Типа Поля - {0 - пустое; 1 - рис; 2 - дом; 3 - вода}
    final private int type;
    private int required_villagers_amount;

    public Field(int type_value){   // Присваивает Полю значение
        super();
        this.type = type_value;
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
}
