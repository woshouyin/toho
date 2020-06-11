package com.example.app2.touho.elements.Bullet;

import com.example.app2.touho.stage.Stage;

public abstract class EnemyBullet extends Bullet{

    public EnemyBullet(Stage stage, int layer, int group) {
        super(stage, layer, group);
    }

    public abstract boolean collisionCheck(float x, float y);

    @Override
    public void onCreate() {
        super.onCreate();
        stg.addEnemyBullet(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stg.rmEnemyBullet(this);
    }
}
