package PoolGame.Memento;

import PoolGame.objects.Ball;

import java.util.HashMap;

public class CareTaker {
    private HashMap<Ball, BallMemento> ballMementos;
    private ScoreMemento sm;
    private TimerMemento tm;

    public CareTaker(){
        this.ballMementos = new HashMap<>();
    }

    /**
     * get ball state
     * @param ball
     * @return the state of ball
     */
    public BallMemento getBallMementos(Ball ball){
        return ballMementos.get(ball);
    }

    /**
     * add a state of ball to caretaker
     * @param ball
     * @param bm state of ball
     */
    public void addBallMemento(Ball ball, BallMemento bm){
        ballMementos.put(ball, bm);
    }

    /**
     * rewrite the state
     * @param sm state of Score
     */
    public void updateScoreState(ScoreMemento sm){
        this.sm = sm;
    }


    /**
     *
     * @return a state of score
     */
    public ScoreMemento getScoreState(){
        return sm;
    }

    /**
     * add the timer state to caretaker
     * @param tm state of Timer
     */
    public void updateTimerState(TimerMemento tm){
        this.tm = tm;
    }

    /**
     *
     * @return a state of timer
     */
    public TimerMemento getTimerState(){
        return tm;
    }
}
