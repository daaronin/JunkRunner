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
public class Enemy extends MovingCharacter{

    float randomInterval;
    float randomDirection;
    float speed;
    String type;
    int counter = 0;
    
    public Enemy(int x, int y, int width, int height, int health, int temperature, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, health, temperature, tex, edit, leveleditor);
        ALIVE = true;
        setRelativeX(x);
        setRelativeY(y);
        if (tex == "chaser"){
            position.setWidth(25);
            position.setHeight(25);
            setHealth(100);
            speed = 25;
            JUMP_HEIGHT = 20;
            R = 10;
            SHOT_DAMAGE = 100;
            type = "chaser";
            MAX_SPEED = 25;
        } else if (tex == "crawler"){
            position.setWidth(50);
            position.setHeight(50);
            SHOT_DAMAGE = 150;
            setHealth(250);
            MAX_SPEED = 10;
            gravityWorking = false;
            type = "crawler";
        } else if (tex == "bulker") {
            position.setWidth(75);
            position.setHeight(75);
            SHOT_DAMAGE = 200;
            setHealth(500);
            MAX_SPEED = 10;
            JUMP_HEIGHT = 0;
            type = "bulker";
        }
        
        ACCEL_SPEED = 0.02f;
        DECEL_SPEED = 0.02f;
        GRAVITY_AMOUNT = 2;
        gravityWorking = true;
        GRAVITY = 0.04f;
        DECEL_AMOUNT = 2;
        
        MAX_FALL_SPEED = 30;
        MAX_CEILING_CLING = 100;
        CLING_REDUC = 1f;
        JUMP_DIR_PROPEL = 30;
        JUMP_UP_PROPEL = 30;
        SHOT_SPEED = 50;
    }
    
    @Override
    public void update(int delta, String collisionDirection){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        controls(delta, collisionDirection);
    }
    
    @Override
    public void update(int delta, String collisionDirection, char Direction){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        if (type.equals("crawler")){
            crawlerControls(delta, collisionDirection);
        } else if (type.equals("chaser")){
            chaserControls(delta, collisionDirection, Direction);
        }else if (type.equals("bulker")){
            chaserControls(delta, collisionDirection, Direction);
        } else {
            genericControls(delta, collisionDirection);
        }
    }
    
    @Override
    public void controls(int delta, String collisionDirection){
        if (type.equals("crawler")){
            crawlerControls(delta, collisionDirection);
        } else if (type.equals("chaser")){
            chaserControls(delta, collisionDirection, ' ');
        }else{
            genericControls(delta, collisionDirection);
        }
    }
    
    public void chaserControls(int delta, String collisionDirection, char Direction) {
        if (collisionDirection.contains("L")){
                //ALIVE = false;
            L = 0;
            R += 10;
        }
        if (collisionDirection.contains("R")){
                //ALIVE = false;
            R = 0;
            L += 10;
        }
        if (collisionDirection.contains("T")){
                //ALIVE = false;
            U = 0;
            D += 5;
        }
        if (collisionDirection.contains("B")){
                //ALIVE = false;
            D = 1;
            U = JUMP_HEIGHT;
        }
        if (collisionDirection.contains("A")){
                //ALIVE = false;
        }
        if (collisionDirection.contains("G")){
                JUMP_TIMER = 0;
                LAST_COLLISION = 'G';
        }
        if (collisionDirection.equals("")){
                D++;
        }
        
        if (Direction == 'L'){
            L = 20;
            R = 0;
        } else if (Direction == 'R'){
            R = 20;
            L = 0;
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
        }
        if (U > 0){
            decRelativeY((int)((ACCEL_SPEED * U) * delta));
        }
        if (R > 0){
            incRelativeX((int)((ACCEL_SPEED * R) * delta));
        }
        if (D > 0){
            incRelativeY((int)((GRAVITY * D) * delta));
        }
    }
    
    public void crawlerControls(int delta, String collisionDirection) {
        if (collisionDirection.contains("L")){
            gravityWorking = false;
            
            U = 0;
            L = 5;
            D = 10;
            LAST_COLLISION = 'L';
            counter = 0;
        }
        if (collisionDirection.contains("R")){
            gravityWorking = false;
            D = 0;
            R = 5;
            U= 10;
            LAST_COLLISION = 'R';
            counter = 0;
        }
        if (collisionDirection.contains("T")){
            gravityWorking = false;
            
            U = 5;
            R = 0;
            L = 10;
            LAST_COLLISION = 'T';
            counter = 0;
        }
        if (collisionDirection.contains("B")){
            gravityWorking = false;
            D = 5;
            L = 0;
            R = 10;
            LAST_COLLISION = 'B';
            counter = 0;
        }
        if (collisionDirection.contains("A")){
                //ALIVE = false;
        }
        if (collisionDirection.contains("G")){

        }
        if (collisionDirection.equals("")){
            if (LAST_COLLISION == 'L'){
                L = 10;
                D = 0;
                U = 0;
                counter++;
                if (counter > 10){
                    LAST_COLLISION = 'T';
                    counter = 0;
                }
            } else if (LAST_COLLISION == 'R'){
                R = 10;
                U = 0;
                L = 0;
                counter++;
                if (counter > 10){
                    LAST_COLLISION = 'D';
                    counter = 0;
                }
            } else if (LAST_COLLISION == 'T'){
                U = 10;
                L = 0;
                D = 0;
                counter++;
                if (counter > 20){
                    LAST_COLLISION = 'R';
                    counter = 0;
                }
            } else if (LAST_COLLISION == 'B'){
                D = 10;
                R = 0;
                U = 0;
                counter++;
                if (counter > 20){
                    LAST_COLLISION = 'L';
                    counter = 0;
                }
            } else {
                U = 0;
                D++;
                LAST_COLLISION = ' ';
            }
            
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
        }
        if (U > 0){
            decRelativeY((int)((ACCEL_SPEED * U) * delta));
        }
        if (R > 0){
            incRelativeX((int)((ACCEL_SPEED * R) * delta));
        }
        if (D > 0){
            incRelativeY((int)((ACCEL_SPEED * D) * delta));
        }
        if (gravityWorking){
            D++;
        } else{
            D = 0;
        }
    }
    
    public void genericControls(int delta, String collisionDirection) {
        //System.out.println(collisionDirection);
        if (collisionDirection.contains("L")){
                //ALIVE = false;
            L = 0;
            R += 5;
        }
        if (collisionDirection.contains("R")){
                //ALIVE = false;
            R = 0;
            L += 5;
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
                //ALIVE = false;
        }
        if (collisionDirection.contains("G")){
                JUMP_TIMER = 0;
                LAST_COLLISION = 'G';
        }
        if (collisionDirection.equals("")){
                D++;
        }
        
        if (randomInterval <= 0){
            L = 0;
            R = 0;
            Random r = new Random();
            randomInterval = r.nextInt(20);
            randomDirection = r.nextInt(10);
            //System.out.println(randomDirection + "," + randomInterval);
        } else {
            if (randomDirection > 6){
                L = speed;
            } else if (randomDirection > 2){
                R = speed;
            } else {
                U = speed;
            }
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
        }
        if (R > 0){
            incRelativeX((int)((ACCEL_SPEED * R) * delta));
        } 
        if (D > 0){
            incRelativeY((int)((GRAVITY * D) * delta));
        }
        if (U > 0){
            decRelativeY((int)((ACCEL_SPEED * U) * delta));
            U--;
        }
        if (U == 0 && !collisionDirection.contains("B")){
            D++;
        }
        randomInterval--;
    }
}
