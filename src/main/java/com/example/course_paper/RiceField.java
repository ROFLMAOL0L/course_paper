package com.example.course_paper;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RiceField extends Field{
    private boolean is_watered = false;
    public RiceField(int type_value, int i, int j, int x, int y, int required_villagers_amount){
        super(type_value, i, j, x, y);
        super.setRequired_villagers_amount(required_villagers_amount);

        // Изменяем надпись на значение "сложности" захвата каждого поля
        this.setText(Integer.toString(this.getType()));
        this.setContentDisplay(ContentDisplay.TOP);
    }

    @Override
    public void setTexture(){
        ImageView rice_view = new ImageView(new Image("C:\\Users\\Кирилл\\Desktop\\course_paper\\rice.png"));
        rice_view.setFitHeight(ConquerGame.cell_height / 2.0);
        rice_view.setFitWidth(ConquerGame.cell_width / 2.0);
        rice_view.setPreserveRatio(true);
        rice_view.setX(this.getX());
        rice_view.setY(this.getY());
        rice_view.autosize();
        this.setGraphic(rice_view);
    }
}
