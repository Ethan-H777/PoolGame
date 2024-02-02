package PoolGame.Memento;

import PoolGame.objects.Ball;
import PoolGame.strategy.PocketStrategy;

import java.util.ArrayList;

public class BallMemento implements GameMemento{
    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private boolean isActive;
    private PocketStrategy strategy;

    public BallMemento(double posX, double posY, double velX, double velY, boolean isActive, PocketStrategy strategy){
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.isActive = isActive;
        this.strategy = strategy;
    }

    /**
     *
     * @return the pocket strategy
     */
    public PocketStrategy getStrategy() {
        return strategy;
    }

    public double getPosX(){
        return posX;
    }

    public double getPosY(){
        return posY;
    }

    public double getVelX(){
        return velX;
    }

    public double getVelY(){
        return  velY;
    }

    public boolean getIsActive(){
        return isActive;
    }
}
