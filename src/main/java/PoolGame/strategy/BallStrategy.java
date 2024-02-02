package PoolGame.strategy;

public class BallStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public BallStrategy() {
        this.lives = 1;
    }

    public void reset() {
        this.lives = 1;
    }

    public PocketStrategy copy(){
        BallStrategy ball = new BallStrategy();
        ball.setLives(this.getLives());
        return (PocketStrategy) ball;
    }
}
