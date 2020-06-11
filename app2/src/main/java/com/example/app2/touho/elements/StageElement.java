package com.example.app2.touho.elements;

import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.Controler;
import com.example.app2.touho.stage.Stage;

import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

public class StageElement extends AnmElement{
    protected Stage stg;
    private HashSet<Controler> controlers = new HashSet<>();
    protected float baseDirection = 0;


    public StageElement(Stage stage, int layer, int group) {
        super(stage.touho, layer, group);
        stg = stage;
    }

    @Override
    public void tick() {
        super.tick();

        for(Object c:controlers.toArray()){
            ((Controler) c).tick();
        }
    }

    @Override
    public void draw(GL10 gl, float[] mvpMatrix) {
        //super.draw(gl, mvpMatrix);
        if(frames != null) drawFrame(frames[playingFrame], x - stg.getDrawCenterX(), y - stg.getCenterY() - stg.getDrawCenterY(), roatate, scaleX, scaleY, color, mvpMatrix);
    }

    @Override
    public boolean isOutScreen() {
        return x < touho.getLeft() - drawSizeX*scaleX || x >touho.getRight() + drawSizeX*scaleX
                || getStgY() > touho.getTop() + drawSizeY*scaleY
                || getStgY() < touho.getBottom() - drawSizeY*scaleY;
    }

    @Override
    public void addSubElement(Element e) {
        super.addSubElement(e);
    }

    public void setStgPositon(float x, float y) {
        super.setPositon(x, y + stg.getCenterY());
    }

    public float getStgY(){
        return y - stg.getCenterY();
    }

    public void addControler(Controler controler){
        controlers.add(controler);
        controler.bind(this);
    }

    public void clearControler(){
        controlers.clear();
    }

    public void rmControler(Controler controler){
        controlers.remove(controler);
    }

    public void setBaseDirection(float rad) {this.baseDirection = rad;}

    public void setDirection(float rad){
        setRotate(rad - baseDirection);
    }

    public void moveToRad(float rad, float speed){
        x += speed * Math.cos(rad);
        y += speed * Math.sin(rad);
    }

    public Stage getStg() {
        return stg;
    }
}
