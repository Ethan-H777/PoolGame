package PoolGame.strategy;

public class BlackStrategy extends PocketStrategy{

    public BlackStrategy(){
        this.lives = 3;
    }
    @Override
    public void reset() {
        this.lives = 3;
    }

    public PocketStrategy copy(){
        BlackStrategy black = new BlackStrategy();
        black.setLives(this.getLives());
        return (PocketStrategy) black;
    }
}
