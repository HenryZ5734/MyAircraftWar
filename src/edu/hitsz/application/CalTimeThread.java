package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.util.Date;

/**
 * @author henry
 */
public class CalTimeThread extends Thread{

    HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();

    @Override
    public void run() {
        int activateTime = 15000;
        try {
            sleep(activateTime);
            heroAircraft.setFireActivated(false);
        } catch (InterruptedException ignored) {

        }
    }
}
