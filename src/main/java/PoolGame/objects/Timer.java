package PoolGame.objects;

import PoolGame.Memento.TimerMemento;

public class Timer extends Performance{
    private int time;
    private boolean stop;

    public Timer(int time, boolean stop){
        this.stop = stop;
        this.time = time;
    }

    /**
     * add 1 to time
     */
    public void addTime(){
        if (!stop) time += 1;
        inform();
    }

    /**
     *
     * @return string of the minutes
     */
    public String getMin(){
        int sec = time % 60;
        int min = (time - sec) / 60;

        return String.format("%d", min);
    }

    /**
     *
     * @return the string of the seconds
     */
    public String getSec(){
        int sec = time % 60;
        String secString;

        if (sec < 10){
            secString = "0"+sec;
        } else{
            secString = String.format("%d", sec);
        }

        return secString;
    }

    /**
     * stop the timer
     */
    public void stop(){
        stop = true;
    }

    /**
     *
     * @return s state of the timer
     */
    @Override
    public TimerMemento save(){
        return new TimerMemento(this.time);
    }

    /**
     * set a state to current timer
     * @param tm state of timer
     */
    public void restore(TimerMemento tm){
        time = tm.getTime();
    }
}
