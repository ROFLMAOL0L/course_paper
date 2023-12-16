package com.example.course_paper;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public int rice_amount;
    public int water_amount;
    public int villagers_amount;
    public int houses_amount = 0;
    public int house_rice_cost = 1;
    public int house_villager_cost = 1;
    public int rice_fields_amount = 0;
    public int water_fields_amount = 0;
    public int overall_fields_amount = 3;
    private int house_rice_cost_increment = 1;

    public List<Integer> x_fields = new ArrayList<Integer>();
    public List<Integer> y_fields = new ArrayList<Integer>();
    public Player(){
        rice_amount = 0;
        water_amount = 0;
        villagers_amount = 1;
    }
    public void give_field(int x, int y){
        this.x_fields.add(x);
        this.y_fields.add(y);
    }
    public void addHouse(){
        this.houses_amount++;
        // Уменьшаем количество ресурсов на соответствующие стоимости постройки дома
        this.rice_amount -= this.house_rice_cost;
        this.villagers_amount -= this.house_villager_cost;

        this.increaseHouseCost();
    }
    public void increaseHouseCost(){
        // Рост цены постройки дома (требуемый рис) равноускоренно
        this.house_rice_cost += this.house_rice_cost_increment;
        // Рост "ускорения роста" линейно
        this.house_rice_cost_increment++;
        // Рост цены постройки дома (требуемые крестьяне) линейно
        this.house_villager_cost++;
    }

    public void collectWater(){
        if (this.water_fields_amount <= this.villagers_amount){
            this.water_amount += this.water_fields_amount;
        } else {
            this.water_amount += this.villagers_amount;
        }
    }
}
