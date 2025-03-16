/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Arwa Mohamed
 */
public class GameRunningState implements State {

    CircusWorld c;

    public GameRunningState(CircusWorld c) {
        this.c = c;
    }

    @Override
    public boolean handle(CircusWorld c) {
        boolean timeout = System.currentTimeMillis() - c.getStartTime() > c.getMAXIMUM_TIME();
        if (timeout) {
            c.setGameState(new GameOverState());
            return false;
        }
        return true;
    }

  

}
