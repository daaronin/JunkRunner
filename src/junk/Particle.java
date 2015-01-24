/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package junk;

/**
 *
 * @author Dan
 */
public class Particle extends MovingCharacter{
    
    private int counter;
    private int counter_max;
    private String TYPE;
    private int SIZE;
    private int SPEED;
    
    public Particle(int x, int y, int width, int height, int health, int temperature, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, health, temperature, tex, edit, leveleditor);
        counter_max = 50;
        counter = 0;
    }
    
    public Particle(int x, int y, String type, int size, int speed, String tex, boolean edit, boolean leveleditor) {
        super(x, y, size, size, 10, 100, tex, edit, leveleditor);
        counter_max = speed;
        counter = 0;
        TYPE = type;
        SIZE = size;
        SPEED = speed;
    }
 
    @Override
    public void update(int delta, String collisionDirection){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        if (TYPE == "boom"){
            flicker(SPEED);
        } else {
            
        }
    }
    
    @Override
    public void update(int delta, String collisionDirection, char Direction){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        if (TYPE == "boom"){
            flicker(SPEED);
        } else {
            
        }
    }
    
    public void flicker(int speed){
        if (counter < speed){
            if (counter < speed/2){
                setRelativeX(getRelativeX()-2);
                setRelativeY(getRelativeY()-2);
                position.setWidth(position.getWidth()+4);
                position.setHeight(position.getHeight()+4);
            } else {
                setRelativeX(getRelativeX()+2);
                setRelativeY(getRelativeY()+2);
                position.setWidth(position.getWidth()-4);
                position.setHeight(position.getHeight()-4);
            }
        } else {
            ALIVE = false;
        }
        counter++;
    }
}
