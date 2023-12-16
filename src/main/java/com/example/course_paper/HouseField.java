package com.example.course_paper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HouseField extends Field{
    public HouseField(int type_value, int i, int j, int x, int y, Player owner) {
        super(type_value, i, j, x, y);
        this.owner = owner;
        owner.addHouse();
        this.setPrefSize(ConquerGame.cell_width, ConquerGame.cell_height);
    }

    @Override
    public void setTexture(){
        ImageView house_view = new ImageView(new Image("C:\\Users\\Кирилл\\Desktop\\course_paper\\house.png"));
        house_view.setFitHeight(ConquerGame.cell_height / 2.0);
        house_view.setFitWidth(ConquerGame.cell_width / 2.0);
        house_view.setPreserveRatio(true);
        house_view.setX(this.getX());
        house_view.setY(this.getY());
        house_view.autosize();
        this.setGraphic(house_view);
    }
    @Override
    public void spawnVillager(){
        this.owner.villagers_amount++;
    }
}
