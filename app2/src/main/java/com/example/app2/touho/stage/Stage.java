package com.example.app2.touho.stage;

import android.view.MotionEvent;

import com.example.app2.R;
import com.example.app2.touho.Touho;
import com.example.app2.touho.elements.Bullet.Bullet;
import com.example.app2.touho.elements.Bullet.EnemyBullet;
import com.example.app2.touho.elements.Bullet.PlayerBullet;
import com.example.app2.touho.elements.Element;
import com.example.app2.touho.elements.Touch.Button;
import com.example.app2.touho.elements.Touch.CircularButton;
import com.example.app2.touho.elements.Touch.Joystick;
import com.example.app2.touho.elements.role.Enemy;
import com.example.app2.touho.elements.role.Player;
import com.example.app2.touho.elements.role.Reimu;
import com.example.app2.touho.elements.role.Role;
import com.example.app2.touho.listener.ClickListener;
import com.example.app2.touho.utils.ReplayLogger;
import com.example.app2.touho.utils.ShaderUtil;

import java.util.HashSet;
import java.util.Random;

public abstract class Stage extends Element {
    private float centerY;
    private float drawCenterX;
    private float drawCenterY;
    private float moveSpeed = 0;
    private float moveTickNum = 0;
    protected Player player;
    private HashSet<Enemy> enemys = new HashSet<>();
    private HashSet<PlayerBullet> playerBullets = new HashSet<>();
    private HashSet<EnemyBullet> enmBullets = new HashSet<>();

    protected Joystick joystick = null;
    protected CircularButton shiftButton = null;
    private long seed;
    private Random rand;
    protected ReplayLogger replayLogger = null;
    protected boolean replayMode = false;


    public Stage(Touho touho, int layer, int group, long seed) {
        super(touho, layer, group);
        this.seed = seed;
        this.rand = new Random(seed);
        setBindSubElms(true);   //销毁时同时销毁子元素
        this.replayLogger = new ReplayLogger(false);
        replayLogger.setSeed(seed);
    }


    public Stage(Touho touho, int layer, int group, ReplayLogger replayLogger) {
        this(touho, layer, group, replayLogger.getSeed());
        this.replayLogger = replayLogger;
        this.replayMode = true;
    }

    public void start(){
        if(!replayMode) {
            //操作杆
            joystick = new Joystick(touho, getLayer() + 1, getGroup(), 0.2f, touho.getvRight() - 0.3f, touho.getvBottom() + 0.3f);
            joystick.setPositon(touho.getvRight() - 0.3f, touho.getvBottom() + 0.3f);
            joystick.setCheckBound(0, 0, touho.getvRight(), touho.getvBottom());
            joystick.setDestroyOutScreen(false);
            joystick.setTexture(R.raw.etama2, 65f / 256, 225f / 256, 95f / 256, 255f / 256);

            //潜行按钮
            shiftButton = new CircularButton(touho, getLayer() + 1, getGroup(), touho.getvLeft() + 0.2f, touho.getvBottom() + 0.3f, 0.08f) {
                @Override
                public void onTouchEvent(float touchX, float touchY, int action) {
                    super.onTouchEvent(touchX, touchY, action);
                    if (player != null) {
                        if (action == MotionEvent.ACTION_DOWN) {
                            player.preSetMode(Player.MODE_SHIFT);
                        } else if (action == MotionEvent.ACTION_UP) {
                            player.preSetMode(Player.MODE_NORMAL);
                        }
                    }
                }
            };
            shiftButton.setTexture(R.raw.etama2, 65f / 256, 225f / 256, 95f / 256, 255f / 256);
            shiftButton.setDestroyOutScreen(false);
            addSubElement(joystick);
            addSubElement(shiftButton);
        }

        //暂停按钮
        Button quitButton = new Button(touho, 200, getGroup(), touho.getvLeft() + 0.15f, touho.getvTop() - 0.08f, 0.08f, 0.25f);
        quitButton.setTexture(R.raw.title01, 0,256/512f, 67/512f, 288/512f);
        quitButton.setListener(new ClickListener() {
            @Override
            public void onClick() {
                touho.getAco().callQuitDialog();
            }
        });
        addSubElement(quitButton);
    }

    @Override
    public void tick() {
        super.tick();
        if(moveTickNum != 0 && moveSpeed != 0){
            centerY += moveSpeed;
            moveTickNum--;
            if(player != null){
                player.setPositon(player.getX(), player.getY() + moveSpeed);
            }
        }
        for(EnemyBullet eb : enmBullets){
            if(eb.collisionCheck(player.getX(), player.getY())){
                player.shooted();
            }
        }

        for(Enemy e: enemys){
            for(PlayerBullet pb : playerBullets){
                if(e.collisionCheck(pb.getX(), pb.getY())){
                    e.shooted(pb.getDamage());
                    pb.crash();
                }
            }
        }
    }

    public float getCenterY() {
        return centerY;
    }

    public void move(float speed, int tickNum){
        this.moveSpeed = speed;
        this.moveTickNum = tickNum;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getDrawCenterX() {
        return drawCenterX;
    }

    public void setDrawCenterX(float drawCenterX) {
        this.drawCenterX = drawCenterX;
    }

    public float getDrawCenterY() {
        return drawCenterY;
    }

    public void setDrawCenterY(float drawCenterY) {
        this.drawCenterY = drawCenterY;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public int getPlayerLayer(){
        return getLayer() + 100;
    }

    public int getPlayerBulletLayer(){
        return getLayer() + 99;
    }

    public int getEnmLayer(){
        return getLayer() + 50;
    }

    public int getEnmBulletLayer(){
        return getLayer() + 101;
    }

    public void addEnemyBullet(EnemyBullet eb){
        enmBullets.add(eb);
    }
    public void rmEnemyBullet(EnemyBullet eb){
        enmBullets.remove(eb);
    }

    public void addEnemy(Enemy em){
        enemys.add(em);
    }

    public void rmEnemy(Enemy em){
        enemys.remove(em);
    }

    public void addPlayerBullet(PlayerBullet pb){
        playerBullets.add(pb);
    }

    public void rmPlayerBullet(PlayerBullet pb){
        playerBullets.remove(pb);
    }


    public Random getRand() {
        return rand;
    }


    public long getSeed() {
        return seed;
    }

    public ReplayLogger getReplayLogger() {
        return replayLogger;
    }
}
