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
public class Decoration extends Scenery {
    
    private int GROWTH = 0;
    private boolean BLOOMED = false;
    
    public Decoration(Point p, int width, int height) {
        super(p, width, height);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
    }
    
    public Decoration(Point p, int width, int height,  int health) {
        super(p, width, height, health);
        setRelativeX(p.getX());
        setRelativeY(p.getY());
    }
        
    public Decoration(int x, int y, int width, int height,  int health) {
        super(x, y, width, height, health);
        setRelativeX(x);
        setRelativeY(y);
    }

    public Decoration(int x, int y, int width, int height,  int health, State state) {
        super(x, y, width, height, health, state);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public Decoration(int x, int y, int width, int height,  int health, String tex) {
        super(x, y, width, height, health, tex);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public Decoration(int x, int y, int width, int height, int health, int temper, String tex) {
        super(x, y, width, height, health, temper, tex);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public Decoration(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        setRelativeX(x);
        setRelativeY(y);
    }
    
    @Override
    public boolean getBloomed(){
        return BLOOMED;
    }
    
    @Override
    public void setBloomed(boolean setting){
        BLOOMED = setting;
    }
    
    @Override
    public boolean isBloomable(){
        if (getTexture().equals("grass1")||getTexture().equals("grass2")||getTexture().equals("grass3")||getTexture().equals("grass4")
                ||getTexture().equals("shroom1")||getTexture().equals("shroom2")||getTexture().equals("shroom3")||getTexture().equals("shroom4")){
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void waterPlant(int amount){
        if (GROWTH < 301){
            GROWTH += amount;
            if (GROWTH < 100 && getTexture().equals("grass1")){
                setTex("grass1");
            }else if (GROWTH < 100 && getTexture().equals("shroom1")){
                setTex("shroom1");
            }else if (GROWTH < 200 && (getTexture().equals("grass1")||getTexture().equals("grass2"))){
                setTex("grass2");
            }else if (GROWTH < 200 && (getTexture().equals("shroom1")||getTexture().equals("shroom2"))){
                setTex("shroom2");
            }else if (GROWTH < 300 && (getTexture().equals("grass2")||getTexture().equals("grass3"))){
                setTex("grass3");
            }else if (GROWTH < 300 && (getTexture().equals("shroom2")||getTexture().equals("shroom3"))){
                setTex("shroom3");
            }else if (GROWTH >= 300 && (getTexture().equals("grass3")||getTexture().equals("grass4"))){
                setTex("grass4");
                BLOOMED = true;
            }else if (GROWTH >= 300 && (getTexture().equals("shroom3")||getTexture().equals("shroom4"))){
                setTex("shroom4");
                BLOOMED = true;
            }
        }
    }
}
