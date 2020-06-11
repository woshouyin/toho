package com.example.app2.touho.controler;

import com.example.app2.touho.stage.Stage;

public class BiuControler extends Controler{
    private float xscale;
    private float yscale;
    private float alpha;
    private float dxscale;
    private float dyscale;
    private float dalpha;
    private boolean dieEnd;

    /**
     *  缩放与透明度线性变化
     * @param dieEnd 在结束时销毁元素
     */
    public BiuControler(float startXScale, float endXScale, float startYScale, float endYScale
                        , float startAlpha, float endAlpha, long playTick, boolean dieEnd) {
        super();
        xscale = startXScale;
        yscale = startYScale;
        alpha = startAlpha;
        dxscale = (endXScale - startXScale) / playTick;
        dyscale = (endYScale - startYScale) / playTick;
        dalpha = (endAlpha - startAlpha) / playTick;
        this.dieEnd = dieEnd;
        setPlayTick(playTick);
    }

    public BiuControler(float startScale, float endScale
            , float startAlpha, float endAlpha, long playTick, boolean dieEnd) {
        this(startScale, endScale, startScale, endScale, startAlpha, endAlpha, playTick, dieEnd);
    }
    public BiuControler(float startScale, float endScale
            , float startAlpha, float endAlpha, long playTick) {
        this(startScale, endScale, startAlpha, endAlpha, playTick, true);
    }
    @Override
    public void tick() {
        super.tick();
        xscale += dxscale;
        yscale += dyscale;
        alpha += dalpha;
        elm.setScale(xscale, yscale);
        elm.setAlpha(alpha);
    }

    @Override
    public void start() {
        super.start();
        elm.setScale(xscale, yscale);
        elm.setAlpha(alpha);
    }

    @Override
    public void end() {
        super.end();
        if(dieEnd){
            elm.die();
        }
    }
}
