package com.example.app2.touho.factory;

import android.graphics.Shader;
import android.media.Image;

import com.example.app2.R;
import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.BiuControler;
import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.elements.Bullet.Bullet;
import com.example.app2.touho.elements.Bullet.CircularBullet;
import com.example.app2.touho.elements.Bullet.EnemyBullet;
import com.example.app2.touho.elements.Bullet.PlayerBullet;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.elements.role.Player;
import com.example.app2.touho.elements.role.Reimu;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.MathUtil;
import com.example.app2.touho.utils.ShaderUtil;

public class BulletFactory {
    /**
     *  etama中的子弹矩阵获取
     */
                                                //1   2   3   4   5   6    7    8    9    10   11
    private static int etama_Top[] = new int[]   {17, 32, 48, 65, 80, 96 , 112, 128, 144, 160, 176};
    private static int etama_Bottom[] = new int[]{32, 47, 63, 78, 95, 111, 127, 143, 162, 174, 191};
    private static int etama_hw[] = new int[]    {7 , 8 , 8 , 4 , 4 , 5  , 7  , 4  , 4  , 8  , 8};
    private static int etama_step = 16;
    private static int etama_start = 8;
    private static float etama_size = 256;
    public static EnemyBullet getEtama(Stage stage, int r, int c){
        EnemyBullet bullet = null;
        r = r - 1;
        int xl = etama_start + c * etama_step;
        bullet = new CircularBullet(stage, stage.getEnmBulletLayer(), stage.getGroup());/*
        bullet.setFrame(Frame.getInstance(touho.getResouseManager().getTexture(R.raw.etama)
                        ,4
                        ,ShaderUtil.getRectVtxBuffer(-1,1,1,-1)
                        ,ShaderUtil.getRectVtxBuffer((xl-etama_hw[r])/etama_size, etama_Top[r]/etama_size
                                                , (xl+etama_hw[r] + 1)/etama_size, etama_Bottom[r]/etama_size)));*/

        bullet.setFrame(Frame.getInstance(stage.getTouho().getResouseManager().getTexture(R.raw.etama)
                ,4
                ,ShaderUtil.getRectVtxBuffer(-1,1,1,-1)
                ,ShaderUtil.getRectVtxBuffer((16*c)/etama_size, (16*r)/etama_size
                        , (16*c + 16)/etama_size, (16*r + 16)/etama_size)));
        bullet.setDrawSize(1, 1);
        bullet.setBaseDirection((float) (Math.PI/2));
        return bullet;
    }

    /**
     * @param num 1.直线 2.追踪
     */
    public static PlayerBullet getPlayerBullet(Stage stage, float width, int num){
        PlayerBullet pb = null;
        if(num == 1) {
            pb = new PlayerBullet(stage, stage.getPlayerBulletLayer(), stage.getGroup()){
                @Override
                public void crash() {
                    super.crash();
                    StageElement se = new StageElement(stg, stg.getEnmBulletLayer() + 1, getGroup());
                    se.setFrame(Frame.getInstance(touho.getResouseManager().getTexture(R.raw.pl00)
                            ,4
                    , ShaderUtil.getRectVtxBuffer(-0.5f, 0.5f * 16 / 56, 0.5f, -0.5f * 16 / 56)
                    , ShaderUtil.getRectVtxBuffer(68/256f, 177/256f, 124/256f, 183/256f))
                    );
                    se.setPositon(x, y);
                    se.setDrawSize(0.5f,0.5f);
                    se.setRotate((touho.getRand().nextFloat() - 0.5f) * MathUtil.PI/4 + MathUtil.PI / 2);
                    se.addControler(new BiuControler(0.1f, 0.5f, 0.9f, 0f, 10));
                    addSubElement(se);
                }
            };
            pb.setFrame(Frame.getInstance(stage.getTouho().getResouseManager().getTexture(R.raw.pl00)
                    , 4
                    , ShaderUtil.getRectVtxBuffer(-64/12f * width + width / 2, width / 2, width / 2,-width/2 )
                    , ShaderUtil.getRectVtxBuffer(0/256f, 178/256f, 64/256f, 190/256f)));
            pb.setDrawSize(width/2, 64/12f * width);
        }else if(num == 2){
            pb = new PlayerBullet(stage, stage.getPlayerBulletLayer(), stage.getGroup());
            pb.setFrame(Frame.getInstance(stage.getTouho().getResouseManager().getTexture(R.raw.pl00)
                    , 4
                    , ShaderUtil.getRectVtxBuffer(78/256f, 162/256f - 0.5f/256, 64/256f, 174/256f + 0.5f/256)
                    , ShaderUtil.getRectVtxBuffer(-width/2, width / 2, width / 2, -width / 2)));
            pb.setDrawSize(width/2, width/2);
        }
        return pb;
    }

}
