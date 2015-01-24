/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package junk;

import java.util.Random;

/**
 *
 * @author Dan
 */
public class Projectile extends MovingCharacter {
    
    public int DEATH_TIMER = 0;
    public float SHOT_SPREAD;
    
    public Projectile(int x, int y, int width, int height, int damage, int direction, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, damage, direction, tex, edit, leveleditor);
        gravityWorking = false;
        ALIVE = true;
        setRelativeX(x);
        setRelativeY(y);
        
        ACCEL_SPEED = 0.02f;
        DECEL_SPEED = 0.02f;
        JUMP_HEIGHT = 22;
        GRAVITY_AMOUNT = 2;
        gravityWorking = true;
        GRAVITY = 0.04f;
        DECEL_AMOUNT = 2;
        MAX_SPEED = 100;
        MAX_FALL_SPEED = 30;
        MAX_CEILING_CLING = 100;
        CLING_REDUC = 1f;
        JUMP_DIR_PROPEL = 30;
        JUMP_UP_PROPEL = 30;
        SHOT_SPEED = 100;
        SHOT_SPREAD = 10;
        SHOT_DAMAGE = damage;
        
        Random r = new Random();
        if (direction == 0){
            U += .8 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
            if (U > 45){
                L += r.nextInt((int)SHOT_SPREAD);
            } else {
                R += r.nextInt((int)SHOT_SPREAD);
            }
        } else if (direction == 1){
            R += .8 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
            if (R > 45){
                U += r.nextInt((int)(SHOT_SPREAD*1.5));
            } else {
                D += r.nextInt((int)SHOT_SPREAD/3);
            }
        } else if (direction == 2){
            D += .8 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
            if (D > 45){
                L += r.nextInt((int)SHOT_SPREAD);
            } else {
                R += r.nextInt((int)SHOT_SPREAD);
            }
        } else if (direction == 3){
            L += .8 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
            if (L > (SHOT_SPEED - SHOT_SPEED/10)){
                U += r.nextInt((int)(SHOT_SPREAD*1.5));
            } else {
                D += r.nextInt((int)SHOT_SPREAD/3);
            }
        } else if (direction == 4){
            R += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            D += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
        } else if (direction == 5){
            U += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            R += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
                        position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
        } else if (direction == 6){
            L += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            U += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
                        position.setWidth((int)(position.getWidth()));
            position.setHeight((int)(position.getHeight()));
        } else if (direction == 7){
            L += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
            D += .7 * SHOT_SPEED + r.nextInt((int)SHOT_SPEED/5);
        }
    }
    
    @Override
    public void update(int delta, String collisionDirection){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        controls(delta, collisionDirection);
        //checkTemp();
    }
    
    @Override
    public void controls(int delta, String collisionDirection) {
        if (DEATH_TIMER == 50){
            ALIVE = false;
        }
        if (collisionDirection.contains("L")){
                //ALIVE = false;
            L = 0;
        }
        if (collisionDirection.contains("R")){
                //ALIVE = false;
            R = 0;
        }
        if (collisionDirection.contains("T")){
                //ALIVE = false;
            U = 0;
        }
        if (collisionDirection.contains("B")){
                //ALIVE = false;
            D = 0;
        }
        if (collisionDirection.contains("A")){
                ALIVE = false;
        }
        if (collisionDirection.contains("G")){
                JUMP_TIMER = 0;
                LAST_COLLISION = 'G';
        }
        if (L > MAX_SPEED){
            L = MAX_SPEED;
        } else if (R > MAX_SPEED){
            R = MAX_SPEED;
        }
        if (D > MAX_FALL_SPEED){
            D = MAX_FALL_SPEED;
        }
        if (L > 0){
            decRelativeX((int)((ACCEL_SPEED * L) * delta));
            L-=DECEL_AMOUNT;
        } else {
            L = 0;
        }
        if (R > 0){
            incRelativeX((int)((ACCEL_SPEED * R) * delta));
            R-=DECEL_AMOUNT;
        } else {
            R = 0;
        }
        if (D > 0){
            incRelativeY((int)((GRAVITY * D) * delta));
        } else {
            D = 0;
        }
        if (U > 0){
            decRelativeY((int)((ACCEL_SPEED * U) * delta));
            U-=DECEL_AMOUNT;
        } else {
            U = 0;
        }
        if (!collisionDirection.contains("R") &&!collisionDirection.contains("L") &&!collisionDirection.contains("B") &&!collisionDirection.contains("T")){
            DEATH_TIMER+= .5;
        }
        if (U == 0 && L == 0 && R == 0){
            if (!collisionDirection.contains("B")){
                D++;
                if (position.getY() > 2000){
                    ALIVE = false;
                }
            } else {
                DEATH_TIMER++;
            }
        }
    }
}
