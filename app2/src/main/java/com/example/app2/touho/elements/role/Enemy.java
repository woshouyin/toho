package com.example.app2.touho.elements.role;

import com.example.app2.R;
import com.example.app2.touho.stage.Stage;

public abstract class Enemy extends Role{
    private int health;
    public Enemy(Stage stg, int layer, int group) {
        super(stg, layer, group);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stg.addEnemy(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stg.rmEnemy(this);
    }

    public void onDead(){
        touho.getResouseManager().playSound(R.raw.se_tan01, 1);
    }

    public void shooted(int damage){
        if(isAlive()) {
            touho.getResouseManager().playSound(R.raw.se_damage00, 0.5f);
            health -= damage;
            if (health <= 0) {
                health = 0;
                die();
                onDead();
            }
        }

    }

    public abstract boolean collisionCheck(float x, float y);

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
