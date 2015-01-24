/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package junk;

import org.lwjgl.util.Point;


/**
 *
 * @author Dan
 */
public class Threat extends DefaultCharacter {
        
    private int spriteIterator;
 
    public Threat(Point p, int width, int height) {
        super(p, width, height);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
        spriteIterator = 0;
    }
    
    public Threat(Point p, int width, int height,  int health) {
        super(p, width, height, health);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
        spriteIterator = 0;
    }
        
    public Threat(int x, int y, int width, int height,  int health) {
        super(x, y, width, height, health);
        setRelativeX(x);
        setRelativeY(y);
        spriteIterator = 0;
    }

    public Threat(int x, int y, int width, int height,  int health, State state) {
        super(x, y, width, height, health, state);
        setRelativeX(x);
        setRelativeY(y);
        spriteIterator = 0;
    }
    
    public Threat(int x, int y, int width, int height,  int health, String tex) {
        super(x, y, width, height, health, tex);
        setRelativeX(x);
        setRelativeY(y);
        
        if (tex.matches("fire1")){
            spriteIterator = 0;
        } else if (tex.matches("fire2")){
            spriteIterator = 100;
        } else {
            spriteIterator = 200;
        }
    }
    
    public Threat(int x, int y, int width, int height,  int health, int temper, String tex) {
        super(x, y, width, height, health, tex);
        setRelativeX(x);
        setRelativeY(y);
        
        setTemperature(temper);
        if (tex.matches("fire1")){
            spriteIterator = 0;
        } else if (tex.matches("fire2")){
            spriteIterator = 100;
        } else if (tex.matches("fire3")) {
            spriteIterator = 200;
        } else {
            spriteIterator = 1000;
        }
    }
    
    public Threat(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        setRelativeX(x);
        setRelativeY(y);
        
        setTemperature(temperature);
        if (tex.matches("fire1")){
            spriteIterator = 0;
        } else if (tex.matches("fire2")){
            spriteIterator = 100;
        } else if (tex.matches("fire3")) {
            spriteIterator = 200;
        } else {
            spriteIterator = 1000;
        }
    }
    
    public void updateIterator(int delta){
        spriteIterator += delta;
        if (spriteIterator > 400 && spriteIterator != 1000){
            spriteIterator = 0;
        }
        if (spriteIterator < 100 && getTexture().equals("fire2")){
            setTex("fire1");
        } else if (spriteIterator < 200 && getTexture().equals("fire1")){
            setTex("fire2");
        } else if (spriteIterator < 300 && getTexture().equals("fire2")){
            setTex("fire3");
        } else if (spriteIterator <= 400 && getTexture().equals("fire3")){
            setTex("fire2");
        }
        
    }
    
    public int getIterator(){
        return spriteIterator;
    }
}
