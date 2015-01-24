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
public class Item extends Scenery{

    int counter = 0;
    int iterator = 1;
    int Width, originaly;
    int Height, originalx;
    public Item(int x, int y, int width, int height, int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
            setRelativeX(x);
            setRelativeY(y);
            Width = width;
            Height = height;
            originalx = x;
            originaly = y;
    }
    
    @Override
    public void update(int delta, String collisionDirection) {
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        flicker();
    }
    
    public void flicker(){
        if (counter == 0){
            counter++;
            iterator = -iterator;
        } else if (counter < 100){
            //position.setX((int)(position.getX()+(Width *.125)));
            //position.setY((int)(position.getY()+(Height *.125)));
            //position.setWidth((int)(Width/.75));
            //position.setHeight((int)(Height/.75));
            counter += iterator;
        } else if (counter < 150){
            //position.setWidth(Width);
            //position.setHeight(Height);
            counter += iterator;
        } else if (counter < 250){
            //position.setX((int)(position.getX()-(Width *.125)));
            //position.setY((int)(position.getY()-(Height *.125)));
            //position.setWidth((int)(Width/1.25));
            //position.setHeight((int)(Height/1.25));
            counter += iterator;
        } else if (counter == 250){
            counter--;
            iterator = -iterator;
        }
    }
    
}
