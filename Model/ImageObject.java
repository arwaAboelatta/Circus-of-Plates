/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import eg.edu.alexu.csd.oop.game.GameObject;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Esraa
 */

public class ImageObject implements GameObject {
    private int x;
    private int y;
    private int height;
    private int width;
    private boolean isVisible;
    private final int size = 1;
    private BufferedImage[] spriteImages = new BufferedImage[size];
    private int type=0; // 1 left , 2 right
    private ImageObject g;
    private int plateOrGoldOrBomb; //0 for plate , 1 for gold , 2 for bomb


    public int getPlateOrGoldOrBomb() {
        return plateOrGoldOrBomb;
    }

    public void setPlateOrGoldOrBomb(int plateOrGoldOrBomb) {
        this.plateOrGoldOrBomb = plateOrGoldOrBomb;
    }

    public ImageObject(int x, int y, String path) {
        this.x = x;
        this.y = y;
        this.isVisible = true;
        try {
            spriteImages[0] = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {

        if (type == 1) {
            this.x = g.getX() -2;
        } else if (type == 2) {
            this.x = g.getX() + g.getWidth() -55;
        } else {
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        if (!(type == 1 || type == 2))
            this.y = y;
        else {

        }
    }

    @Override
    public int getWidth() {
        return spriteImages[0].getWidth();
    }

    @Override
    public int getHeight() {
        return spriteImages[0].getHeight();
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public BufferedImage[] getSpriteImages() {
        return spriteImages;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ImageObject getG() {
        return g;
    }

    public void setG(ImageObject g) {
        this.g = g;
    }

    public Rectangle getBounds() {

        return new Rectangle(getX(), getY(),getWidth(), getHeight()+2);

    }


}