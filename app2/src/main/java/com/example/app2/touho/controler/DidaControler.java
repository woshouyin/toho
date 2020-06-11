package com.example.app2.touho.controler;

import com.example.app2.touho.stage.Stage;

public abstract class DidaControler extends Controler{
    private int step;
    private int count = 0;

    public DidaControler(int step) {
        super();
        this.step = step;
    }

    @Override
    public void tick() {
        super.tick();
        if(count-- == 0){
            dida();
            count = step - 1;
        }
    }

    public abstract void dida();
}
