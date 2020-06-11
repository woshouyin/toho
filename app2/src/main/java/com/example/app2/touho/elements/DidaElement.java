package com.example.app2.touho.elements;

import com.example.app2.touho.Touho;

public abstract class DidaElement extends Element {
    private int step;
    private int count = 0;

    public DidaElement(Touho touho, int layer, int group, int step) {
        super(touho, layer, group);
        this.step = step;
        setDrawable(false);
    }

    @Override
    public void tick() {
        super.tick();
        if(++count == step){
            dida();
            count = 0;
        }
    }

    public abstract void dida();
}
