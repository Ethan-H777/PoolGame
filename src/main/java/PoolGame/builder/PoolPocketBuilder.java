package PoolGame.builder;

import PoolGame.builder.PocketBuilder;
import PoolGame.objects.Pocket;

public class PoolPocketBuilder implements PocketBuilder {
    private double x;
    private double y;
    private double r;


    /**
     * set the x position of pocket
     * @param x
     */
    @Override
    public void setPosX(double x) {
        this.x = x;
    }

    /**
     * set y position
     * @param y
     */
    @Override
    public void setPosY(double y) {
        this.y = y;
    }

    /**
     * set radius of pocket
     * @param r
     */
    @Override
    public void setRadius(double r) {
        this.r = r;
    }

    /**
     *
     * @return a Pocket object
     */
    public Pocket build(){
        return new Pocket(x, y, r);
    }
}
