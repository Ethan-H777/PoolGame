package PoolGame.observer;

import PoolGame.objects.Score;
import javafx.scene.control.Label;

public class ScoreObserver implements Observer{
    private Score score;
    private Label label;

    public ScoreObserver(Score score, Label label){
        this.score = score;
        this.label = label;
        score.attach(this);
    }

    /**
     * update the text of score label
     */
    @Override
    public void update() {
        String scoreString = "Score: " + score.getScore();
        label.setText(scoreString);
    }
}
