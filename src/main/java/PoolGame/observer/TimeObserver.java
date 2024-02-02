package PoolGame.observer;

import PoolGame.objects.Performance;
import PoolGame.objects.Timer;
import javafx.scene.control.Label;

public class TimeObserver implements Observer{
    private Timer timer;
    private Label label;

    public TimeObserver(Timer timer, Label label){
        this.label = label;
        this.timer = timer;
        timer.attach(this);
    }

    /**
     * update the text of timer label
     */
    @Override
    public void update() {
        String timeString = "Time: " + timer.getMin() + ":" + timer.getSec();
        label.setText(timeString);
    }
}
