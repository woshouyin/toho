package com.example.app2.touho.elements.Bullet;

import com.example.app2.touho.stage.Stage;

public class PlayerBullet extends Bullet {
    private int damage;

    public PlayerBullet(Stage stage, int layer, int group) {
        super(stage, layer, group);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stg.addPlayerBullet(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stg.rmPlayerBullet(this);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void crash(){
        if(destroyCrash){
           die();
        }
    }
}
