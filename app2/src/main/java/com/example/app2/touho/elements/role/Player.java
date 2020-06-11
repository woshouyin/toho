package com.example.app2.touho.elements.role;

import com.example.app2.MainActivity;
import com.example.app2.R;
import com.example.app2.Texture;
import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.BindControler;
import com.example.app2.touho.controler.BiuControler;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.elements.Touch.Joystick;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.ReplayLogger;
import com.example.app2.touho.utils.ShaderUtil;

import javax.microedition.khronos.opengles.GL10;

public abstract class Player extends Role{
    public static final int MODE_NORMAL = 0;
    public static final int MODE_SHIFT = 1;
    protected float width;
    protected float height;
    private float speed = 0.01f;
    private float shiftSpeed = 0.005f;
    private float normalSpeed = 0.01f;
    protected Joystick joystick = null;
    protected boolean canOutBound = false;
    protected boolean shootting = false;
    protected boolean shiftMode = false;
    protected boolean shooted = false;
    protected boolean imba = false;
    protected Frame shiftFrame[] = new Frame[2];
    protected float shiftRotate = 0;
    protected float dShiftRotate = 0.02f;
    protected int mode = 0;
    protected int psMode = 0;
    private ReplayLogger replayLogger = null;
    private StageElement shiftGraph;
//    StringBuilder sb1 = new StringBuilder();
//    StringBuilder sb2 = new StringBuilder();
//    StringBuilder sb3 = new StringBuilder();
    int i = 0;

    public Player(Stage stg, int layer, int group, float width, float height, ReplayLogger replayLogger) {
        super(stg, layer, group);
        this.width = width;
        this.height = height;
        setDestroyOutScreen(false);
        setDrawSize(width/2, height/2);
        this.replayLogger = replayLogger;
    }

    @Override
    public void load() {
        super.load();
        shiftFrame[0] = Frame.getInstance(
                touho.getResouseManager().getTexture(R.raw.etama2),
                4,
                ShaderUtil.getRectVtxBuffer(-0.5f * width, 0.5f * width, 0.5f * width, -0.5f * width),
                ShaderUtil.getRectVtxBuffer(0, 16/256f, 64/256f, 80/256f)
        );
        shiftFrame[1] = Frame.getInstance(
                touho.getResouseManager().getTexture(R.raw.etama2),
                4,
                ShaderUtil.getRectVtxBuffer(-0.5f * width, 0.5f * width, 0.5f * width, -0.5f * width),
                ShaderUtil.getRectVtxBuffer(64/256f, 16/256f, 128/256f, 80/256f)
        );
        shiftGraph = new StageElement(stg, getLayer() + 1, getGroup()){

            @Override
            public void draw(GL10 gl, float[] mvpMatrix) {
                super.draw(gl, mvpMatrix);
                float c[] = Player.this.color.clone();
                c[3] = 1;
                drawFrame(shiftFrame[1], Player.this.x, Player.this.y, shiftRotate, scaleX,scaleY, color, mvpMatrix);
                drawFrame(shiftFrame[0], Player.this.x, Player.this.y, shiftRotate, scaleX,scaleY, c, mvpMatrix);
            }
        };
        shiftGraph.setScale(0);
        addSubElement(shiftGraph);

    }

    @Override
    public void tick() {
        super.tick();
        shiftRotate -= dShiftRotate;
        if(shootting){
            shoot();
        }

        int direction = 0;
        if(!replayLogger.isReplayMode()) {
            if (joystick != null && joystick.getAction() != 0) {
                float rd = (float) (joystick.getRad() / Math.PI);
                if (rd > 3 / 8f && rd <= 5 / 8f) {
                    direction = 1;
                } else if (rd > 1 / 8f && rd <= 3 / 8f) {
                    direction = 2;
                } else if (rd > -1 / 8f && rd <= 1 / 8f) {
                    direction = 3;
                } else if (rd > -3 / 8f && rd <= -1 / 8f) {
                    direction = 4;
                } else if (rd > -5 / 8f && rd <= -3 / 8f) {
                    direction = 5;
                } else if (rd > -7 / 8f && rd <= -5 / 8f) {
                    direction = 6;
                } else if (rd > 7 / 8f || rd <= -7 / 8f) {
                    direction = 7;
                } else if (rd > 5 / 8f && rd <= 7 / 8f) {
                    direction = 8;
                }
            }
            if(psMode != mode){
                setMode(psMode);
            }
            replayLogger.push(mode, 0, direction);
        }else {
            if(!replayLogger.next()){
                touho.getAco().callReplayOverDialog();
                return;
            }
            if(mode != replayLogger.getMode()){
                setMode(replayLogger.getMode());
            }
            direction = replayLogger.getDirection();
        }
        i++;
//        sb1.append(i + ":" + getX() + ",");
//        sb2.append(i + ":" + direction + ",");
//        sb3.append(i + ":" + mode + ",");
        move(direction, speed);
//        System.out.println(sb1);
//        System.out.println(sb2);
//        System.out.println(sb3);
        if(!canOutBound){
            x = Math.max(touho.getLeft() + drawSizeX , Math.min(x, touho.getRight() - drawSizeX));      //边界限制
            y = Math.max(touho.getBottom() + stg.getCenterY() + drawSizeY , Math.min(y, touho.getTop() + stg.getCenterY() - drawSizeY));
        }
    }

    public void move(int direction, float speed){
        double rad = 0;
        switch (direction){
            case 0:
                setAction(ACTION_STOP);
                break;
            case 1:
                setAction(ACTION_STOP);
                rad = Math.PI / 2;
                break;
            case 2:
                setAction(ACTION_RIGHT);
                rad = Math.PI / 4;
                break;
            case 3:
                setAction(ACTION_RIGHT);
                rad = 0;
                break;
            case 4:
                setAction(ACTION_RIGHT);
                rad = -Math.PI / 4;
                break;
            case 5:
                setAction(ACTION_STOP);
                rad = -Math.PI / 2;
                break;
            case 6:
                setAction(ACTION_LEFT);
                rad = -Math.PI / 4 * 3;
                break;
            case 7:
                setAction(ACTION_LEFT);
                rad = -Math.PI;
                break;
            case 8:
                setAction(ACTION_LEFT);
                rad = Math.PI / 4 * 3;
                break;
        }
        if(direction != 0) {
            moveToRad((float) rad, speed);
        }
    }

    @Override
    public void draw(GL10 gl, float[] mvpMatrix) {
        super.draw(gl, mvpMatrix);
    }

    protected abstract void shoot();

    public void startShoot(){shootting = true;};
    public void stopShoot() {shootting = false;}

    public void bindJoystick(Joystick js){
        this.joystick = js;
    }

    public boolean isCanOutBound() {
        return canOutBound;
    }

    public void setCanOutBound(boolean canOutBound) {
        this.canOutBound = canOutBound;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isImba() {
        return imba;
    }

    public void setImba(boolean imba) {
        this.imba = imba;
    }

    public ReplayLogger getReplayLogger() {
        return replayLogger;
    }

    public void shooted(){
        if(!imba && !shooted){
            touho.getResouseManager().playSound(R.raw.se_pldead00, 1);
            //setSpeed(0);
            setAlpha(0.7f);
            setLayer(999);
            stopShoot();
            shooted = true;
            if(!replayLogger.isReplayMode()) touho.getAco().callRestartDialog();
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
        if(mode == MODE_NORMAL){
            speed = normalSpeed;
            shiftGraph.clearControler();
            shiftGraph.addControler(new BiuControler(shiftGraph.getScaleX(), 0, shiftGraph.getAlpha(), 0, (long)(10 / shiftGraph.getScaleX()), false));
        }else if(mode == MODE_SHIFT){
            speed = shiftSpeed;
            shiftGraph.addControler(new BiuControler(shiftGraph.getScaleX(), 1, shiftGraph.getAlpha(), 0.3f, (long)(10 / ( 1 - shiftGraph.getScaleX())), false));
        }
    }

    public void preSetMode(int mode){
        if(this.psMode != mode){
            psMode = mode;
        }
    }
}
