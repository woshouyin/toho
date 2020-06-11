package com.example.app2.touho.factory;

import com.example.app2.touho.controler.BiuControler;
import com.example.app2.touho.controler.LineControler;
import com.example.app2.touho.stage.Stage;


@Deprecated
public class ControlerFactory {
    public static LineControler getLineControler(float startX, float startY, float endX, float endY, float v0, float v1, float t1, float t2, int playTick){
        return new LineControler(startX, startY, endX, endY, v0, v1, t1, t2, playTick);
    }

    public static LineControler getRayControler(float startX, float startY, float rad, float length, float v0, float v1, float t1, float t2, int playTick){
        return new LineControler(startX, startY, startX + length * (float) Math.cos(rad)
                , startX + length * (float)Math.sin(rad), v0, v1, t1, t2, playTick);
    }

}
