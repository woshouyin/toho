package com.example.app2.touho.elements.Touch;

import com.example.app2.touho.Touho;
import com.example.app2.touho.utils.MathUtil;

public class CircularButton extends Button{
    private float radius;
    public CircularButton(Touho touho, int layer, int group, float x, float y, float radius) {
        super(touho, layer, group, x, y, radius * 2, radius * 2);
        this.radius = radius;
    }

    @Override
    public boolean pressingCheck(float x, float y) {
        return MathUtil.distance(this.x, this.y, x, y) < radius;
    }
}
