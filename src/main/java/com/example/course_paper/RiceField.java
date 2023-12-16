package com.example.course_paper;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RiceField extends Field{
    private boolean is_just_watered = false;
    // Используется для подсчета оставшихся ходов, когда
    private int wet_level = 0;

    public RiceField(int type_value, int i, int j, int x, int y, int required_villagers_amount){
        super(type_value, i, j, x, y);
        super.setRequired_villagers_amount(required_villagers_amount);

        // Изменяем надпись на значение "сложности" захвата каждого поля
        this.setText(Integer.toString(required_villagers_amount));
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
    @Override
    public void giveRice(){
        if (this.owner == null){
            return;
        }
        // Это условие позволяет не давать игроку больше риса в конце хода за только что политый рис. Реализация
        // достаточно странная, однако она позволяет
        if (this.is_just_watered){
            this.is_just_watered = false;
            this.owner.rice_amount++;
        } else if (this.wet_level > 0){
            this.owner.rice_amount += 2;
            this.wet_level--;
        } else{
            this.owner.rice_amount++;
        }
    }
    @Override
    public void waterRiceField(){
        this.is_just_watered = true;
        this.wet_level = 2;
    }

}
