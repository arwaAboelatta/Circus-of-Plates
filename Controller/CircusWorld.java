/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import eg.edu.alexu.csd.oop.game.GameObject;
import eg.edu.alexu.csd.oop.game.World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * @author Arwa Mohamed
 */
public class CircusWorld implements World {
    private  int MAXIMUM_TIME;
    private static int PLATES_SPEED; //change when strategy made
    private static int CLOWN_SPEED; //change when strategy made
    private int score = 0;
    private long startTime = System.currentTimeMillis();
    private final int width;
    private final int height;
    private final List<GameObject> constant;
    private final List<GameObject> moving;
    private final List<GameObject> control;
    private Factory factory = new Factory();
    private List<GameObject> leftObjects;
    private List<GameObject> rightObjects;
    private State gameState=new GameRunningState(this);
   // private List<GameObject> fallenPlates = new ArrayList<>();
   

    public CircusWorld(int screenWidth, int screenHeight, Strategy s) {

        MAXIMUM_TIME = s.getTimeout() * 60 * 1000;
        PLATES_SPEED = s.getSpeed();
        CLOWN_SPEED = s.getSpeed();
        this.width = screenWidth;
        this.height = screenHeight;
        this.constant = new LinkedList<GameObject>();
        this.control = new LinkedList<GameObject>();
        this.moving = new LinkedList<GameObject>();
        this.leftObjects = new ArrayList<>();
        this.rightObjects = new ArrayList<>();
        objectFormation();
    }

    private  void objectFormation() {
        constant.add(new ImageObject(0, 0, "background.jpg"));
        constant.add(new Bar(-300, -300, "shelf.png"));
        constant.add(new Bar(-400, -200, "shelf.png"));
        constant.add(new Bar(900, -200, "shelf.png"));
        constant.add(new Bar(800, -300, "shelf.png"));
        constant.add(new Bar(-550, -100, "shelf.png"));
        constant.add(new Bar(1040, -100, "shelf.png"));

        control.add(Clown.getInstance((int) (width / 2.4), (int) (height * 0.668), "clown.png"));
        //upper left bar,barNumber=0
        for (int i = -100; i < 5; i++) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(90 * i, 33, type, 1));
                
            } else {
                moving.add(factory.create(90 * i, 65, type, 0));
               
            }
        
        }
        //lower left bar
        for (int i = -100; i < 4; i++) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(90 * i, 134, type, 1));
            } else {
                moving.add(factory.create(90 * i, 165, type, 0));
            }
          
        }
        //lower low left
        for (int i = -100; i < 2; i++) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(90 * i, 235, type, 1));
            } else {
                moving.add(factory.create(90 * i, 265, type, 0));
            }
          
        }
        //upper right bar
        for (int i = 100; i > 14; i--) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(75 * i, 134, type, 1));
            } else {
                moving.add(factory.create(75 * i, 165, type, 0));
            }
          
        }
        // lower right bar
        for (int i = 100; i > 12; i--) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(75 * i, 33, type, 1));
            } else {
                moving.add(factory.create(75 * i, 65, type, 0));
            }
          
        }

        //lower low right
        for (int i = 100; i > 15; i--) {
            int type = (int) (Math.random() * 7 + 1);
            if (type == 6 || type == 7) {
                moving.add(factory.create(75 * i, 235, type, 1));
            } else {
                moving.add(factory.create(75 * i, 265, type, 0));
            }
           
        }
       
    }
    
   

    @Override
    public boolean refresh() {
//       boolean timeout = System.currentTimeMillis() - startTime > MAXIMUM_TIME; // time end and game over
        
       platesMotion();
        scorePlates();   
        if(!gameState.handle(this)){
            
            return false;
        }
       return gameState.handle(this);

    }

    private void platesMotion() {
        Iterator iterator = new iterator(moving);
        while (iterator.hasNext()) {
            GameObject g = (GameObject) iterator.next();
            //intersect(g);
            checkCollisions(g);
            //upper left
            if (g.getX() < 360 && (g.getY() == 33 || g.getY() == 65)) {
                g.setX(g.getX() + 1);
            } //lower left
            else if (g.getX() < 270 && (g.getY() == 134 || g.getY() == 165)) {
                g.setX(g.getX() + 1);
            } //lower right
            else if (g.getX() > 945 && (g.getY() == 134 || g.getY() == 165)) {
                g.setX(g.getX() - 1);
            } //upper right
            else if (g.getX() > 845 && (g.getY() == 33 || g.getY() == 65)) {
                g.setX(g.getX() - 1);
            } //lower low left
            else if (g.getX() < 100 && (g.getY() == 235 || g.getY() == 265)) {
                g.setX(g.getX() + 1);
            } // lower low right
            else if (g.getX() > 1100 && (g.getY() == 235 || g.getY() == 265)) {
                g.setX(g.getX() - 1);
            } //out of the bar
            else {

                Random random = new Random();
                int remainingX;

                if (g.getY() == 33 || g.getY() == 65) { // Upper bar (left)
                    int randomX = random.nextInt(g.getX() + 1); // Adjust the width of the plate
                    remainingX = randomX - g.getX();
                } else { // Lower bar (right)
                    int randomX = random.nextInt(width - g.getWidth()); // Adjust the width of the plate
                    remainingX = randomX - g.getX();
                }

                int remainingY = height - g.getHeight();

                // Calculate the movement steps for x and y axes
                int stepsX = remainingX / 200; // Adjust this value to control the speed of descent
                int stepsY = remainingY / 200; // Adjust this value to control the speed of descent

                // Set the plate's new x and y positions based on the movement steps
                g.setX(g.getX() + stepsX);
                g.setY(g.getY() + stepsY);
            }
           

        }

    }

    //if plate  left of clown  return 1 , else plate right of clown  return 2 else return 0
    private  int isOnLeftOrRightOfClown(GameObject g) {
        GameObject clown = control.get(0);
        if (g.getX() < clown.getX() + clown.getWidth() / 2) {
            return 1;
        } else if (g.getX() > clown.getX() + clown.getWidth() / 2) {
            return 2;
        }

        return 0;
        // checkIfMovingEnded();

    }

    // 1 left ,  2 right
    private void firstIntersectClown(int x, GameObject g) {
        GameObject clown = control.get(0);
        ImageObject i = (ImageObject) g;

        //left
        if (x == 1) {
            i.setType(x);
            moving.remove(g);
            control.add(g);
            leftObjects.add(g);
            i.setG((ImageObject) clown);
        } //right
        else if (x == 2) {
            i.setType(x);
            moving.remove(g);
            control.add(g);
            rightObjects.add(g);
            i.setG((ImageObject) clown);
        }

        if (g instanceof Plates) {
            Plates p = (Plates) g;
            if (p.getPlateOrGold() == 0) {
                i.setPlateOrGoldOrBomb(0);
            } else {
                i.setPlateOrGoldOrBomb(1);
                score += 5;
                control.remove(p);
                //   moving.add(g);
                if (x == 1)
                    leftObjects.remove(p);
                else
                    rightObjects.remove(p);

            }

        } else if (g instanceof Bomb) {
            i.setPlateOrGoldOrBomb(2);
            score = 0;
            control.remove(g);
            //   moving.add(g);
            if (x == 1)
                leftObjects.remove(g);
            else
                rightObjects.remove(g);
        }
    }

    private void checkCollisions(GameObject g) {
        // Get the clown object for collision detection
        Clown clown = (Clown) control.get(0);
        ImageObject object = (ImageObject) g;

        if (isOnLeftOrRightOfClown(g) == 1) {
            if (object.getBounds().intersects(clown.getBoundsLeft())) {
                if (leftObjects.isEmpty()) {
                    firstIntersectClown(1, object);
                }
            } else if (!leftObjects.isEmpty()) {
                objectIntersect(1, g);
            }

        } else if (isOnLeftOrRightOfClown(g) == 2) {
            if (object.getBounds().intersects(clown.getBoundsRight())) {
                if (rightObjects.isEmpty()) {
                    firstIntersectClown(2, object);
                }
            } else if (!rightObjects.isEmpty()) {
                objectIntersect(2, g);

            }
        }
    }

    private  void objectIntersect(int x, GameObject g) {
        ImageObject object = (ImageObject) g;
        Clown clown = (Clown) control.get(0);

        if (g instanceof Plates) {
            Plates p = (Plates) g;
            if (p.getPlateOrGold() == 0) {
                {
                    object.setPlateOrGoldOrBomb(0);
                }
            } else {
                {
                    object.setPlateOrGoldOrBomb(1);

                }
            }

        } else if (g instanceof Bomb) {
            {
                object.setPlateOrGoldOrBomb(2);
            }

        }

        if (x == 1) {

            ImageObject objectLeft = (ImageObject) leftObjects.get(leftObjects.size() - 1);
            if (object.getBounds().intersects(objectLeft.getBounds())) {
                object.setG((ImageObject) clown);
                object.setType(x);
                moving.remove(g);
                g.setY((int) (g.getY() - objectLeft.getHeight() * 2));
                control.add(g);
                leftObjects.add(g);

                if (object.getPlateOrGoldOrBomb() == 1) {
                    score += 5;
                    control.remove(object);
                    //   moving.add(g);
                    leftObjects.remove(object);
                } else if (object instanceof Bomb) {
                    score = 0;
                    control.remove(g);
                    //   moving.add(g);
                    leftObjects.remove(object);
                }

            }
        } else if (x == 2) {
            ImageObject objectRight = (ImageObject) rightObjects.get(rightObjects.size() - 1);
            if (object.getBounds().intersects(objectRight.getBounds())) {
                {
                    object.setG((ImageObject) clown);
                    object.setType(2);
                    moving.remove(g);
                    g.setY(g.getY() - objectRight.getHeight() * 2);
                    control.add(g);

                    rightObjects.add(g);

                    if (object.getPlateOrGoldOrBomb() == 1) {
                        score += 5;
                        control.remove(object);
                        //   moving.add(g);
                        rightObjects.remove(object);
                    } else if (object instanceof Bomb) {
                        score = 0;
                        control.remove(g);
                     //   moving.add(g);
                        rightObjects.remove(object);
                    }

                }
            }
        }
    }

    private  void scorePlates() {

        Plates first = null;
        Plates second = null;
        Plates third = null;
        if (!leftObjects.isEmpty() && leftObjects.size() >= 3) {
            first = (Plates) leftObjects.get(leftObjects.size() - 1);
            second = (Plates) leftObjects.get(leftObjects.size() - 2);
            third = (Plates) leftObjects.get(leftObjects.size() - 3);

            if (first.getColor() == second.getColor() && first.getColor() == third.getColor()) {
                leftObjects.remove(leftObjects.size() - 1);
                leftObjects.remove(leftObjects.size() - 1);
                leftObjects.remove(leftObjects.size() - 1);

                control.remove(first);
                control.remove(second);
                control.remove(third);

//                moving.add(first);
//                moving.add(second);
//                moving.add(third);
                score++;

            }

        }
        if (!rightObjects.isEmpty() && rightObjects.size() >= 3) {
            first = (Plates) rightObjects.get(rightObjects.size() - 1);
            second = (Plates) rightObjects.get(rightObjects.size() - 2);
            third = (Plates) rightObjects.get(rightObjects.size() - 3);

            if (first.getColor() == second.getColor() && first.getColor() == third.getColor()) {

                rightObjects.remove(rightObjects.size() - 1);
                rightObjects.remove(rightObjects.size() - 1);
                rightObjects.remove(rightObjects.size() - 1);

                control.remove(first);
                control.remove(second);
                control.remove(third);

//                 moving.add(first);
//                moving.add(second);
//                moving.add(third);
                score++;

            }
        }

    }

    
    
    @Override
    public List<GameObject> getConstantObjects() {
        return constant;
    }

    @Override
    public List<GameObject> getMovableObjects() {
        return moving;
    }

    @Override
    public List<GameObject> getControlableObjects() {
        return control;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getStatus() {
        return "Score=" + score + "   |   Time=" + Math.max(0, (MAXIMUM_TIME - (System.currentTimeMillis() - startTime)) / 1000);
    }

    @Override
    public int getSpeed() {
        return PLATES_SPEED;
    }

    @Override
    public int getControlSpeed() {
        return CLOWN_SPEED;
    }

    public State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public  int getMAXIMUM_TIME() {
        return MAXIMUM_TIME;
    }

    public  void setMAXIMUM_TIME(int MAXIMUM_TIME) {
        this.MAXIMUM_TIME = MAXIMUM_TIME;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
//    public void checkIfMovingEnded()
//  { 
//    if(moving.isEmpty())
//         
//           // reuseFallenPlates();
//    
//  }
    
    
   
//private void reuseFallenPlates() {
//    Iterator<GameObject> iterator = fallenPlates.iterator();
//    while (iterator.hasNext()) {
//        GameObject fallenPlate = iterator.next();
//
//        // Check if the fallen plate is out of the visible area
//            // Reset the fallen plate's position based on the object formation logic
//            resetPlatePosition(fallenPlate);
//            // Add the fallen plate back to the moving list
//            moving.add(fallenPlate);
//            // Remove the fallen plate from the fallenPlates list
//            iterator.remove();
//        
//    }
//}

//private void resetPlatePosition(GameObject plate) {
//    if (plate instanceof Plates) {
//        Plates p = (Plates) plate;
//
//        // Determine the bar on which the plate is falling randomly among the six bars
//        int barNumber = (int) (Math.random() * 6) + 1; // Values from 1 to 6
//
//        // Set the fallen plate's new position based on the bar
//        int newX;
//        int newY;
//
//        if (barNumber <= 2) { // Bars on the left side
//            newX = (int) (Math.random() * width / 4 - p.getWidth() - 20); // Adjust the width of the plate
//            newY = (int) (Math.random() * 2) == 0 ? 65 : 33; // Randomly choose between 33 and 65
//        } else if (barNumber <= 4) { // Bars on the right side
//            newX = (int) (Math.random() * width / 4 + 3 * width / 4); // Adjust the width of the plate
//            newY = (int) (Math.random() * 2) == 0 ? 165 : 134; // Randomly choose between 134 and 165
//        } else { // Lower low bars
//            newX = (int) (Math.random() * width / 4 + width / 2); // Adjust the width of the plate
//            newY = (int) (Math.random() * 2) == 0 ? 265 : 235; // Randomly choose between 235 and 265
//        }
//
//        plate.setX(newX);
//        plate.setY(newY);
//    }
//}

}