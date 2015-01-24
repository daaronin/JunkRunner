/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package junk;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;


/**
 *
 * @author Dan Ford
 */
public class Location{

    public int LOCATION_WIDTH = 64;
    public int LOCATION_HEIGHT = 64;
    private int QUAD_LOC = 0;
    private int SCREEN_WIDTH = 800;
    private int SCREEN_HEIGHT = 600;

    protected Rectangle pixelLocation = new Rectangle(0, 0,
            LOCATION_WIDTH, LOCATION_HEIGHT);

    public void setPixelLocation(int x, int y, int width, int height){
        pixelLocation = new Rectangle(x, y, width, height);
    }

    public Location(Point p, int width, int height) {
        pixelLocation.setX(p.getX());
        pixelLocation.setY(p.getY());
        pixelLocation.setWidth(width);
        pixelLocation.setHeight(height);
        updateQuad(p.getX(), p.getY(), width, height);
    }
    
    public Location(int x, int y, int width, int height){
        pixelLocation.setX(x);
        pixelLocation.setY(y);
        pixelLocation.setWidth(width);
        pixelLocation.setHeight(height);
        updateQuad(x, y, width, height);
    }
    
    public int getX() {
        return pixelLocation.getX();
    }

    public int getY() {
        return pixelLocation.getY();
    }
    
    public int getWidth(){
        return pixelLocation.getWidth();
    }
    
    public int getHeight(){
        return pixelLocation.getHeight();
    }
    
    public void setX(int x) {
        pixelLocation.setX(x);
    }

    public void setY(int y) {
        pixelLocation.setY(y);
    }
    
    public void setWidth(int width){
        pixelLocation.setWidth(width);
    }
    
    public void setHeight(int height){
        pixelLocation.setHeight(height);
    }

    public void incX(int amount) {
        pixelLocation.setX(pixelLocation.getX() + amount);
    }

    public void incY(int amount) {
        pixelLocation.setY(pixelLocation.getY() + amount);
    }

    public void decX(int amount) {
        pixelLocation.setX(pixelLocation.getX() - amount);
    }

    public void decY(int amount) {
        pixelLocation.setY(pixelLocation.getY() - amount);
    }

    public Rectangle getScreenLocation() {
        return pixelLocation;
    }

    public void updateQuad(int x, int y, int width, int height) {
        setQuad(0);
        int quadwidth = SCREEN_WIDTH/4;
        int quadheight = SCREEN_HEIGHT/4;
        
        if (y <= quadheight*2){
            if (x <= quadwidth*2){
                //1
                if (y <= quadheight){
                    if (x <= quadwidth){
                        addQuad(1);
                    }
                    if (x+width >= quadwidth){
                        addQuad(2);
                    }
                }
                if (y+height >= quadheight){
                    if (x <= quadwidth){
                        addQuad(16);
                    }
                    if (x+width >= quadwidth){
                        addQuad(32);
                    }
                }
            } 
            if (x+width >= quadwidth*2){
                //2
                if (y <= quadheight){
                    if (x <= quadwidth*3){
                        addQuad(4);
                    }
                    if (x+width >= quadwidth*3){
                        addQuad(8);
                    }
                }
                if (y+height >= quadheight){
                    if (x <= quadwidth*3){
                        addQuad(64);
                    }
                    if (x+width >= quadwidth*3){
                        addQuad(128);
                    }
                }
            }
        }
        if (y+height >= quadheight*2) {
            if (x <= quadwidth*2){
                //3
                if (y <= quadheight*3){
                    if (x <= quadwidth){
                        addQuad(256);
                    }
                    if (x+width >= quadwidth){
                        addQuad(512);
                    }
                }
                if (y+height >= quadheight*3){
                    if (x <= quadwidth){
                        addQuad(4096);
                    }
                    if (x+width >= quadwidth){
                        addQuad(8192);
                    }
                }
            }            
            if (x+width >= quadwidth*2){
                //4
                if (y <= quadheight*3){
                    if (x <= quadwidth*3){
                        addQuad(1024);
                    }
                    if (x+width >= quadwidth*3){
                        addQuad(2048);
                    }
                }
                if (y+height >= quadheight*3){
                    if (x <= quadwidth*3){
                        addQuad(16382);
                    }
                    if (x+width >= quadwidth*3){
                        addQuad(32768);
                    }
                }
            }
        }
    }
    
    public int getQuad(){
        return QUAD_LOC;
    }
    
    public void addQuad(int amount){
        QUAD_LOC+=amount;
    }
    public void setQuad(int amount){
        QUAD_LOC = amount;
    }
}
