package PoolGame.strategy;

import PoolGame.objects.Performance;

public class BlueStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public BlueStrategy() {
        this.lives = 2;
    }

    public void reset() {
        this.lives = 2;
    }

    public PocketStrategy copy(){
        BlueStrategy blue = new BlueStrategy();
        blue.setLives(this.getLives());
        return (PocketStrategy) blue;
    }
}
