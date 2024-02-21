package com.example.gameoflife;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class HelloController {

    private class MyLabel extends Label{

        public int id;
        private ImageView iv;

        public MyLabel(int y, int x, int kepId){
            setLayoutX(x);
            setLayoutY(y);
            id = kepId;
            iv = new ImageView(icons[kepId]);
            setGraphic(iv);
            setStyle("-fx-border-color: lightgray");
            setOnMouseEntered(MouseEvent -> setStyle("-fx-background-color: lightgray"));
            setOnMouseExited(MouseEvent -> setStyle("-fx-background-color: white; -fx-border-color: lightgray"));
        }

        public void setKep(int kepId){
            id = kepId;
            iv.setImage(icons[kepId]);
        }

    }

    public CheckBox Auto;
    public Slider slider;
    public Label count;
    public Pane pane;

    int db = 0;

    private Image[] icons = {new Image(getClass().getResourceAsStream("icons/null.png")),new Image(getClass().getResourceAsStream("icons/sejt.png"))};

    MyLabel[][] t = new MyLabel[20][20];

    long tt = 0;
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
                if(now > tt){
                    int speed = (int)slider.getValue();
                    tt = now + 20_000_000 * speed; //ns
                    step();
                }
        }
    };

    public void onAutoClick(){
        if(Auto.isSelected()) timer.start();
        else timer.stop();
    }

    public void step(){
        db = 0;
        int[][] t_ = new int[20][20];
        for(int y = 0; y < 20; y++){
            for(int x = 0; x < 20; x++){
                int sz = szomszed(y,x);
                if(t[y][x].id == 1){
                  if(sz >= 4 || sz < 2) t_[y][x] = 0; else{ t_[y][x] = 1;db++;}
                }else{
                    if(sz == 3){ t_[y][x] = 1;db++;} else t_[y][x] = 0;
                }
            }
        }
        for(int y = 0; y < 20; y++)for(int x = 0; x < 20; x++) t[y][x].setKep(t_[y][x]);
        count.setText(db+"");
    }

    public int szomszed(int y, int x){
        int a = 0;
        for(int dy = -1; dy < 2; dy++){
            for(int dx = -1; dx < 2; dx++){
                try {
                    if(t[y+dy][x+dx].id == 1) a++;
                }catch (Exception e){}
            }
        }
        if(t[y][x].id == 1) a--;
        return a;
    }

    public void initialize(){
        for(int y = 0; y < 20; y++){
            for(int x = 0; x < 20; x++){
                t[y][x] = new MyLabel(10+y*33,10+x*33,0);
                int finalX = x;
                int finalY = y;
                t[y][x].setOnMousePressed(MouseEvent -> place(finalY,finalX));
                pane.getChildren().add(t[y][x]);
            }
        }
    }

    public void place(int y, int x){
        if(t[y][x].id == 1){
            t[y][x].setKep(0);
            db--;count.setText(db+"");
        }
        else{
            t[y][x].setKep(1);
            db++;count.setText(db+"");
        }

    }

    public void onStepClick() {
        step();
    }

    public void onClearClick() {
        for(int y = 0; y < 20; y++)for(int x = 0; x < 20; x++)t[y][x].setKep(0);
        db=0;count.setText(db+"");
    }

    public void onRandomClick() {
        onClearClick();
        db = 0;
        for(int y = 0; y < 20; y++){
            for(int x = 0; x < 20; x++){
                if(Math.random() < 0.3){
                    db++;
                    t[y][x].setKep(1);
                }
            }
        }
        count.setText(db+"");
    }
}