package com.example.course_paper;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WaterField extends Field{
    public WaterField(int type_value, int i, int j, int x, int y, int required_villagers_amount) {
        super(type_value, i, j, x, y);
        super.setRequired_villagers_amount(required_villagers_amount);

        // Изменяем надпись на значение "сложности" захвата каждого поля
        this.setText(Integer.toString(required_villagers_amount));
        this.setContentDisplay(ContentDisplay.TOP);
    }

    @Override
    public void setTexture(){
        ImageView water_view = new ImageView(new Image("C:\\Users\\Кирилл\\Desktop\\course_paper\\water.png"));
        water_view.setFitHeight(ConquerGame.cell_height / 2.0);
        water_view.setFitWidth(ConquerGame.cell_width / 2.0);
        water_view.setPreserveRatio(true);
        water_view.setX(this.getX());
        water_view.setY(this.getY());
        water_view.autosize();
        this.setGraphic(water_view);
    }
}
