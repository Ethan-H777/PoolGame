package PoolGame.Memento;

public class TimerMemento implements GameMemento{
    private int time;

    public TimerMemento(int time){ this.time = time; }

    public int getTime(){ return time; }
}
