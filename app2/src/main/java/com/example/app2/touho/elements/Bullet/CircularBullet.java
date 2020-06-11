package com.example.app2.touho.elements.Bullet;

import com.example.app2.touho.Touho;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.utils.MathUtil;

public class CircularBullet extends EnemyBullet{
    private float radius;
    public CircularBullet(Stage stage, int layer, int group){
        super(stage, layer, group);
        radius = 1;
    }

    public void setRadius(float radius){
        this.radius = radius;
        this.setScale(radius);
    }

    @Override
    public boolean collisionCheck(float x, float y) {
        return MathUtil.distance(this.x, this.y, x, y) <= radius;
    }
}
