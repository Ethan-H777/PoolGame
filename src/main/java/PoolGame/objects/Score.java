package PoolGame.objects;

import PoolGame.Memento.ScoreMemento;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class Score extends Performance{
    private HashMap<Paint, Integer> ballValues;
    private int score;

    public Score(int score){
        this.score = score;
        fillBallValues();
    }

    /**
     * set up hashMap of ball scores
     */
    public void fillBallValues(){
        ballValues = new HashMap<>();
        ballValues.put(Paint.valueOf("red"), 1);
        ballValues.put(Paint.valueOf("yellow"), 2);
        ballValues.put(Paint.valueOf("green"), 3);
        ballValues.put(Paint.valueOf("brown"), 4);
        ballValues.put(Paint.valueOf("blue"), 5);
        ballValues.put(Paint.valueOf("purple"), 6);
        ballValues.put(Paint.valueOf("black"), 7);
        ballValues.put(Paint.valueOf("orange"), 8);
    }


    /**
     * add 1 score
     * @param ball
     */
    public void addScore(Ball ball){
        this.score += ballValues.get(ball.getColour());
        inform();
    }

    /**
     * clear the score to 0
     */
    public void clearScore(){
        score = 0;
    }

    public int getScore() { return score; }

    /**
     *
     * @return the state of Score
     */
    @Override
    public ScoreMemento save(){
        return new ScoreMemento(this.score);
    }

    /**
     * set the given state to this
     * @param sm state of score
     */
    public void restore(ScoreMemento sm){
        score = sm.getScore();
    }

}
