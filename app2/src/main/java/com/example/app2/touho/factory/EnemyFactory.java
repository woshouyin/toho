package com.example.app2.touho.factory;

import com.example.app2.R;
import com.example.app2.touho.elements.role.Enemy;
import com.example.app2.touho.elements.role.Role;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.MathUtil;
import com.example.app2.touho.utils.ShaderUtil;

public class EnemyFactory {

    public static Enemy getEnemy(Stage stg){
        return new Enemy(stg, stg.getEnmLayer(), stg.getGroup()) {
            @Override
            public boolean collisionCheck(float x, float y) {
                return MathUtil.distance( this.x, this.y, x, y) < 0.1;
            }
        };
    }


    private static final int YS_STEP_Y = 32;
    private static final int YS_STEP_X = 32;
    public static Enemy getYousei(Stage stg, int num, float width){
        Enemy enm = new CircularEnemy(stg, stg.getEnmLayer(), stg.getGroup(), width * 0.6f);
        num = 0;
        int sy = 0;
        if(num >= 4){
            sy = 256;
        }
        Frame[] frames = new Frame[19];
        for(int i = 0; i < 12; i++){
            frames[i] = Frame.getInstance(
                    stg.getTouho().getResouseManager().getTexture(R.raw.enemy),
                    4,
                    ShaderUtil.getRectVtxBuffer(-width / 2, width / 2, width / 2, -width / 2),
                    ShaderUtil.getRectVtxBuffer(YS_STEP_X * i/512f, sy/512f, YS_STEP_X * (i + 1)/512f, (sy + YS_STEP_Y)/512f)
            );
        }
        for(int i = 5; i < 12; i++){
            frames[i + 7] = Frame.getInstance(
                    stg.getTouho().getResouseManager().getTexture(R.raw.enemy),
                    4,
                    ShaderUtil.getXFlipRectVtxBuffer(-width / 2, width / 2, width / 2, -width / 2),
                    ShaderUtil.getRectVtxBuffer(YS_STEP_X * i/512f, sy/512f, YS_STEP_X * (i + 1)/512f, (sy + YS_STEP_Y)/512f)
            );
        }
        enm.setPlayStep(10);
        enm.setFrames(frames);
        enm.setAnms(new int[][]{{0, 1, 2, 3, 4}, {15, 16, 17, 18}, {8, 9, 10, 11}, {5, 6, 7}, {12, 13, 14}});
        enm.setDrawSize(width/2, width/2);
        return enm;
    }

    private static class CircularEnemy extends Enemy{
        private float collisionRadius = 0;

        public CircularEnemy(Stage stg, int layer, int group, float collisionRadius) {
            super(stg, layer, group);
            this.collisionRadius = collisionRadius;
        }

        @Override
        public boolean collisionCheck(float x, float y) {
            return MathUtil.distance(x, y, this.x, this.y) <= collisionRadius;
        }
    }


}
