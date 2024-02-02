package PoolGame.Memento;

public class ScoreMemento implements GameMemento{
    private int score;

    public ScoreMemento(int score){
        this.score = score;
    }

    public int getScore(){ return score; }

}
