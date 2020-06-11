package com.example.app2.touho.stage;

import android.media.SoundPool;

import com.example.app2.R;
import com.example.app2.touho.controler.Controler;
import com.example.app2.touho.controler.DidaControler;
import com.example.app2.touho.controler.ShootControler;
import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.elements.DidaElement;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.elements.Touch.CircularButton;
import com.example.app2.touho.elements.Touch.Joystick;
import com.example.app2.touho.elements.role.Enemy;
import com.example.app2.touho.elements.role.Player;
import com.example.app2.touho.elements.role.Reimu;
import com.example.app2.touho.factory.BulletFactory;
import com.example.app2.touho.factory.ControlerFactory;
import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.LineControler;
import com.example.app2.touho.elements.Background;
import com.example.app2.touho.elements.Bullet.CircularBullet;
import com.example.app2.touho.factory.EnemyFactory;
import com.example.app2.touho.listener.ClickListener;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.MathUtil;
import com.example.app2.touho.utils.ReplayLogger;
import com.example.app2.touho.utils.ShaderUtil;

import java.io.IOException;
import java.util.Random;

public class Stage1 extends Stage{
    StringBuilder sb = new StringBuilder();

    public Stage1(Touho touho, int layer, int group, long seed) {
        super(touho, layer, group, seed);
    }
    public Stage1(Touho touho, int layer, int group, ReplayLogger replayLogger) {
        super(touho, layer, group, replayLogger);
    }

    @Override
    public void start() {
        super.start();
        Background bg = new Background(touho, getGroup(), R.raw.stg2bg, Background.BG_FULL);

        //玩家
        Player rm;
        rm = new Reimu(this, getPlayerLayer(), getGroup(), 0.2f, 0.3f, replayLogger);
        if(!replayMode) {
            rm.bindJoystick(joystick);
        }
        setPlayer(rm);
        final Enemy enm = EnemyFactory.getYousei(this, 4, 0.2f);
        enm.addControler(new DidaControler( 120) {
            private int d = 0;

            @Override
            public void tick() {
                super.tick();

                sb.append(((Enemy)elm).getHealth() + ",");
                System.out.println(sb);
            }

            @Override
            public void dida() {
                ShootControler sc = new ShootControler(MathUtil.PI * d, 0.005f, 0, 0);
                d = 1 - d;
                sc.setPlayTick(120);
                elm.addControler(sc);
            }
        });

        enm.addControler(new DidaControler(30) {
            @Override
            public void dida() {

                int n = 10;
                float radStep = (float) Math.PI * 2 / n;
                float rad = getRand().nextFloat();
                for(int i = 0; i < n; i++) {
                    rad += radStep;
                    CircularBullet cb = (CircularBullet) BulletFactory.getEtama(Stage1.this, 3, i%8+1);
                    cb.setRadius(0.06f);
                    LineControler lc = ControlerFactory.getRayControler( elm.getX(), elm.getY(), rad, 2, 0, 0, 0, 1, 600);
                    cb.setStgPositon(elm.getX(),elm.getY());
                    cb.addControler(lc);
                    addSubElement(cb);
                }
            }
        });
        if(!replayMode) {
            enm.addControler(new Controler() {
                @Override
                public void tick() {

                    if (!elm.isAlive()) {
                        touho.getAco().callSaveReplayDialog(replayLogger);
                    }
                }
            });
        }
        enm.setHealth(5000);

        StageElement helthBar = new StageElement(this, 999, getGroup());
        helthBar.setFrame(Frame.getInstance(-1,4, ShaderUtil.getRectVtxBuffer(0,0,0.7f,-0.1f),ShaderUtil.getRectVtxBuffer(0,0,0.5f,-0.02f)));
        helthBar.addControler(new Controler(){
            @Override
            public void tick() {
                super.tick();
                elm.setScaleX(enm.getHealth()/5000f);
            }
        });
        helthBar.setPositon(-0.3f, 1 - 0.02f);
        enm.setPositon(-120 * 0.005f * 0.5f, 0.6f);
        addSubElement(enm);
        addSubElement(rm);
        addSubElement(bg);
        addSubElement(helthBar);
        //move(0.005f, 12000);
        touho.getResouseManager().stopBgm(1000);
        rm.startShoot();
        //rm.setImba(true);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void onSurfaceChanged() {
        super.onSurfaceChanged();
        if(joystick != null) {
            joystick.setPositon(touho.getvRight() - 0.3f, touho.getvBottom() + 0.3f);
            joystick.setCheckBound(0, 0, touho.getvRight(), touho.getvBottom());
        }
    }
}
