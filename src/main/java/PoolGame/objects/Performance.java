package PoolGame.objects;

import PoolGame.Memento.GameMemento;
import PoolGame.observer.Observer;

public abstract class Performance {
    private Observer obs;

    /**
     * connect an observer
     * @param obs Observer object
     */
    public void attach(Observer obs){
        this.obs = obs;
    }

    public void detach(Observer obs){
        if (this.obs == obs){
            this.obs = null;
        }
    }

    /**
     * update the observer
     */
    public void inform(){
        if (this.obs != null){
            this.obs.update();
        }
    }


    /**
     *
     * @return the state of itself
     */
    abstract GameMemento save();

}
