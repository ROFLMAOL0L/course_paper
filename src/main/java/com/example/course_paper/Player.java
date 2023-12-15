package com.example.course_paper;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public int rice_amount;
    public int water_amount;
    public int villagers_amount;
    public int houses_amount = 0;
    public List<Integer> x_fields = new ArrayList<Integer>();
    public List<Integer> y_fields = new ArrayList<Integer>();
    public Player(){
        rice_amount = 1;
        water_amount = 1;
        villagers_amount = 10;
    }
    public void give_rice(){
        this.rice_amount++;
    }
    public void give_field(int x, int y){
        x_fields.add(x);
        y_fields.add(y);
    }
    public void addHouse(){
        this.houses_amount++;
    }

}
