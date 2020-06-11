package com.example.app2.touho.elements.role;

import android.media.SoundPool;

import com.example.app2.MainActivity;
import com.example.app2.R;
import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.LineControler;
import com.example.app2.touho.controler.ShootControler;
import com.example.app2.touho.elements.Bullet.Bullet;
import com.example.app2.touho.elements.Bullet.CircularBullet;
import com.example.app2.touho.elements.Bullet.PlayerBullet;
import com.example.app2.touho.elements.Touch.Joystick;
import com.example.app2.touho.factory.BulletFactory;
import com.example.app2.touho.factory.ControlerFactory;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.stage.Stage1;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.MathUtil;
import com.example.app2.touho.utils.ReplayLogger;
import com.example.app2.touho.utils.ShaderUtil;

import javax.microedition.khronos.opengles.GL10;

public class Reimu extends Player{
    private static int textureSize = 256;
    private static int XStep = 32;
    private static int YStep = 48;
    private float damaAngle = 0;
    private float damaOmega = 0.2f;
    private Frame damaFrame;
    private int shootInterval = 5;
    private int shootCount = 0;
    private int damage[] = new int[]{10, 5};

    public Reimu(Stage stg, int layer, int group, float width, float height, ReplayLogger replayLogger) {
        super(stg, layer, group, width, height, replayLogger);
    }

    @Override
    public void load() {
        super.load();
        Frame[] frames = new Frame[24];
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 8; c++){
                int tx = c * XStep;
                int ty = r * YStep;
                frames[r * 8 + c] = Frame.getInstance(
                        touho.getResouseManager().getTexture(R.raw.pl00)
                        , 4
                        , ShaderUtil.getRectVtxBuffer(-width/2, height/2, width/2, -height/2)
                        , ShaderUtil.getRectVtxBuffer(tx/(float) textureSize, ty/(float) textureSize
                                , (tx + XStep)/(float) textureSize, (ty + YStep)/(float) textureSize));
            }
        }
        setFrames(frames);
        setAnms(new int[][]{{0,1,2,3,4,5,6,7},{12,13,14,15},{20,21,22,23}, {16, 17, 18, 19}, {8, 9, 10, 11}});
        float damaRadius = width/5;
        damaFrame = Frame.getInstance(
                touho.getResouseManager().getTexture(R.raw.pl00)
                , 4
                , ShaderUtil.getRectVtxBuffer(-damaRadius, damaRadius, damaRadius, -damaRadius)
                , ShaderUtil.getRectVtxBuffer(80/(float) textureSize, 144/(float) textureSize
                        , 96/(float) textureSize, 160/(float) textureSize));
        setAction(ACTION_RIGHT);
        setBindSubElms(false);
    }

    @Override
    public void shoot() {
        if(++shootCount == shootInterval) {
            shootBullet1(0.25f);
            shootBullet1(-0.25f);
            touho.getResouseManager().playSound(R.raw.se_plst00, 0.1f, 1, 1.0f);
            shootCount = 0;
        }
    }

    private void shootBullet1(float dx){
        PlayerBullet pb = BulletFactory.getPlayerBullet(stg, 0.05f, 1);
        pb.setAlpha(0.6f);
        pb.setPositon(x + dx * width, y + 0.07f);
        pb.setRotate(MathUtil.PI/2);
        pb.setDamage(damage[0]);
        pb.addControler(new ShootControler((float)Math.PI/2, 0.1f, 0f, 0));
        addSubElement(pb);
    }

    @Override
    public void tick() {
        super.tick();
        damaAngle += damaOmega;
        if(damaAngle > 2 * Math.PI) damaAngle -= 2*Math.PI;
    }

    @Override
    public void draw(GL10 gl, float[] mvpMatrix) {
        super.draw(gl, mvpMatrix);
        drawDama(0, width , mvpMatrix);

    }

    public void drawDama(float x, float y, float[] mvpMatrix) {
        drawFrame(damaFrame, this.getX() + x * scaleX, getStgY() + y * scaleY, damaAngle, scaleX, scaleY, color, mvpMatrix);
    }


}
