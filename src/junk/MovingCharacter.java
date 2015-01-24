/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package junk;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Dan Ford
 */
public class MovingCharacter extends DefaultCharacter {

    public boolean LEVEL_EDITOR = false;
    /**
     * Movement velocity in a direction
     */
    float L = 0, R = 0, U = 0, D = 0;
    
    /**
     * Prevents jumping when true
     */
    boolean fall = false;
    
    /**
     * Set screen bounds
     */
    int LEFT_BOUND = 0;
    int RIGHT_BOUND = 736;
    int CEILING_BOUND = 0;
    int FLOOR_BOUND = 600;
    private int xlocal = 0;
    private int ylocal = 0;
    public char LAST_COLLISION;
    public int SHOT_DIR = -1;
    
    public boolean ALIVE = true;
    float Left = 0;
    float Right = 0;
    float Up = 0;
    float Down = 0;
    
    private Audio jumpEffect;
    private Audio landEffect;
    /**
     * Set default gravity value
     */
    float GRAVITY = 0.04f;
    boolean gravityWorking;
    
    /**
     * Creates player variables, given value in state test
     */
    float ACCEL_SPEED, 
            DECEL_SPEED, 
            DECEL_AMOUNT, 
            GRAVITY_AMOUNT, 
            JUMP_HEIGHT, 
            MAX_SPEED, 
            MAX_FALL_SPEED, 
            MAX_CEILING_CLING, 
            CEILING_CLING, 
            CLING_REDUC,
            JUMP_TIMER, 
            JUMP_COOLDOWN, 
            JUMP_DIR_PROPEL, 
            JUMP_UP_PROPEL, 
            SHOT_SPEED, 
            SHOT_COOLDOWN = 0,
            SHOT_COOLDOWN_MAX, 
            SHOT_SIZE, 
            SHOT_DAMAGE;
    
    public MovingCharacter(Point p, int width, int height) {
        super(p, width, height);
        xlocal = p.getX();
        ylocal = p.getY();

    }
    
    public MovingCharacter(Point p, int width, int height,  int health) {
        super(p, width, height, health);
        xlocal = p.getX();
        ylocal = p.getY();
    }
        
    public MovingCharacter(int x, int y, int width, int height,  int health) {
        super(x, y, width, height, health);
        xlocal = x;
        ylocal = y;
    }

    public MovingCharacter(int x, int y, int width, int height,  int health, State state) {
        super(x, y, width, height, health, state);
        xlocal = x;
        ylocal = y;
    }
    
    public MovingCharacter(int x, int y, int width, int height,  int health, String tex) {
        super(x, y, width, height, health, tex);
        xlocal = x;
        ylocal = y;
    }
    
    public MovingCharacter(int x, int y, int width, int height,  int health, int temper, String tex) {
        super(x, y, width, height, health, temper, tex);
        xlocal = x;
        ylocal = y;
    }
    
    public MovingCharacter(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        super(x, y, width, height, health, temperature, tex, edit);
        xlocal = x;
        ylocal = y;
        setRelativeX(x);
        setRelativeY(y);
    }
    
    public MovingCharacter(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, health, temperature, tex, edit);
        initSound();
        xlocal = x;
        ylocal = y;
        mylocalX = x * 1.0f;
        mylocalY = y * 1.0f;
        setRelativeX(x);
        setRelativeY(y);
        LEVEL_EDITOR = leveleditor;
        
        ACCEL_SPEED = 0.02f;
        DECEL_SPEED = 0.02f;
        JUMP_HEIGHT = 22;
        GRAVITY_AMOUNT = 2;
        gravityWorking = true;
        GRAVITY = 0.04f;
        DECEL_AMOUNT = 2;
        MAX_SPEED = 30;
        MAX_FALL_SPEED = 30;
        MAX_CEILING_CLING = 100;
        CLING_REDUC = 1f;
        JUMP_DIR_PROPEL = 30;
        JUMP_UP_PROPEL = 30;
        SHOT_COOLDOWN_MAX = 25;
        SHOT_SIZE = 20;
        SHOT_DAMAGE = 100;
    }
    
    public void initSound(){
        try {
            jumpEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("junkaudio/shot.wav"));
            landEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("junkaudio/land.wav"));
        } catch (IOException ex) {
            Logger.getLogger(MovingCharacter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void update(int delta, String collisionDirection){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        controls(delta, collisionDirection);
        //checkTemp();
    }
    
    public void update(int delta, String collisionDirection, char Direction){
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
        controls(delta, collisionDirection);
        //checkTemp();
    }
    
    public void controls(int delta, String collisionDirection) {
        //position.setX(Mouse.getX()-32);
        //position.setY(600-Mouse.getY()-32);
        //TODO Make missles work
        
        if (SHOT_COOLDOWN >= SHOT_COOLDOWN_MAX){
            //System.out.println(SHOT_COOLDOWN + ":" + SHOT_COOLDOWN_MAX);
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
                SHOT_DIR = 4;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
                SHOT_DIR = 5;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
                SHOT_DIR = 7;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_UP)){
                SHOT_DIR = 6;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
                SHOT_DIR = 3;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
                SHOT_DIR = 1;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
                SHOT_DIR = 0;
                SHOT_COOLDOWN = 0;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
                SHOT_DIR = 2;
                SHOT_COOLDOWN = 0;
            }
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)){
            if (gravityWorking){
                gravityWorking = false;
                fall = false;
                MAX_SPEED = 100;
            } else {
                gravityWorking = true;
                MAX_SPEED = 30;
            }
        }
        
        if (collisionDirection.contains("L")){
                JUMP_COOLDOWN = 0;
                fall = false;
                LAST_COLLISION = 'L';
                /*if (L > 0){
                    U += L * .1;
                }*/
        }
        if (collisionDirection.contains("R")){
                JUMP_COOLDOWN = 0;
                fall = false;
                LAST_COLLISION = 'R';
                /*if (R > 0){
                    U += R * .1;
                }*/
        }
        if (collisionDirection.contains("T")){
                if (gravityWorking){
                    U = 0;
                } else {
                    
                }
                D = 0;
                fall = true;
                LAST_COLLISION = 'T';
        }
        if (collisionDirection.contains("B")){
                //landEffect.playAsSoundEffect(1.0f, 1.0f, false);
                D = 0;
                CEILING_CLING = MAX_CEILING_CLING;
                JUMP_COOLDOWN = 0;
                //JUMP_COOLDOWN++;
                fall = false;
                LAST_COLLISION = 'B';
        }
        if (collisionDirection.contains("A")){
                fall = false;
                //JUMP_COOLDOWN++;
                JUMP_TIMER = 0;
                LAST_COLLISION = 'A';
        }
        if (collisionDirection.contains("G")){
                LAST_COLLISION = 'G';
        }
        if (collisionDirection.equals("")){
            JUMP_COOLDOWN += 1;
        }
        if ((LAST_COLLISION == 'L' || LAST_COLLISION == 'R') && JUMP_COOLDOWN >= 20){
            fall = true;
        } else if (!(LAST_COLLISION == 'L' || LAST_COLLISION == 'R') && JUMP_COOLDOWN >= 10){
            fall = true;
        }
        if (!collisionDirection.contains("B")){
            //JUMP_COOLDOWN= 5;
        }
        try {
            Keyboard.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(MovingCharacter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (L > MAX_SPEED){
            L = MAX_SPEED;
        } else if (R > MAX_SPEED){
            R = MAX_SPEED;
        }
        if (D > MAX_FALL_SPEED){
            D = MAX_FALL_SPEED;
        }
        if (SHOT_COOLDOWN < SHOT_COOLDOWN_MAX){
            SHOT_COOLDOWN+=1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            CEILING_CLING = 0;
        }
        
        if(!collisionDirection.contains("L")){
            if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                setTex("character");
                if (R > 0){
                    R-=DECEL_AMOUNT;
                    incXLocal((int)((DECEL_SPEED * R) * delta));
                }else if (R <= 0){
                    R=0;
                    if (L < MAX_SPEED){
                        L++;
                    }
                    decXLocal((int)((ACCEL_SPEED * L) * delta));
                }
            }
        }
        if(!collisionDirection.contains("R")){
            if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                setTex("character");
                if (L > 0){
                    L-=DECEL_AMOUNT;
                    decXLocal((int)((DECEL_SPEED * L) * delta));
                }else if (L <= 0){
                    L=0;
                    if (R <= MAX_SPEED){
                        R++;
                    }
                    incXLocal((int)((ACCEL_SPEED * R) * delta));
                }
            } 
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)){
            if(!collisionDirection.contains("L")){
                if (L > 0){
                    L-=DECEL_AMOUNT;
                    if(L < 0)
                        L=0;
                    decXLocal((int)((DECEL_SPEED * L) * delta));
                }
            }
            if(!collisionDirection.contains("R")){
                if (R > 0){
                    R-=DECEL_AMOUNT;
                    if(R < 0)
                        R=0;
                    incXLocal((int)((DECEL_SPEED * R) * delta));
                }
            }
        }
        if (LEVEL_EDITOR){
            if (Keyboard.isKeyDown(Keyboard.KEY_W)){
                if (D > 0){
                    D-=DECEL_AMOUNT;
                    incYLocal((int)((DECEL_SPEED * D) * delta));
                }else if (D <= 0){
                    D=0;
                    if (U < MAX_SPEED){
                        U++;
                    }
                    decYLocal((int)((ACCEL_SPEED * U) * delta));
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)){
                if (U > 0){
                    U-=DECEL_AMOUNT;
                    decYLocal((int)((DECEL_SPEED * U) * delta));
                }else if (U <= 0){
                    U=0;
                    if (D < MAX_FALL_SPEED){
                        D++;
                    }
                    incYLocal((int)((ACCEL_SPEED * D) * delta));
                }
            }
            if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)){
                if (D > 0){
                    D-=DECEL_AMOUNT;
                    incYLocal((int)((DECEL_SPEED * D) * delta));
                }else if (U > 0){
                    U-=DECEL_AMOUNT;
                    decYLocal((int)((DECEL_SPEED * U) * delta));
                }
            }
        } else if (!LEVEL_EDITOR){
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !fall && !collisionDirection.contains("T")){
                D = 0;
                U=JUMP_HEIGHT;
                jumpEffect.playAsSoundEffect(1.5f, 1.0f, false);
                fall=true;
                decYLocal((int)((GRAVITY * U) * delta));
                if (collisionDirection.contains("L")){
                    if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                        L = 0;
                        R += 20;
                    } else {
                        R += 10;
                    }
                } else if (collisionDirection.contains("R")){
                    if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                        R = 0;
                        L += 20;
                    } else {
                        L += 10;
                    }
                } else if (!collisionDirection.contains("B") && !collisionDirection.contains("L") 
                        && !collisionDirection.contains("R") && !collisionDirection.contains("T")
                        && !collisionDirection.contains("A") && !collisionDirection.contains("G")){
                    if (Keyboard.isKeyDown(Keyboard.KEY_A) && LAST_COLLISION == 'R'){
                        L += JUMP_DIR_PROPEL;
                        U += JUMP_UP_PROPEL;
                    } else if (Keyboard.isKeyDown(Keyboard.KEY_D) && LAST_COLLISION == 'L'){
                        R += JUMP_DIR_PROPEL;
                        U += JUMP_UP_PROPEL;
                    }
                }
            } else if ((U > 0 && !collisionDirection.contains("T"))){
                U-=GRAVITY_AMOUNT;
                decYLocal((int)((GRAVITY * U) * delta));
            } else if (U <= 0){
                JUMP_TIMER++;
                //System.out.println("Falling.");
                if (gravityWorking && collisionDirection.contains("T") && CEILING_CLING > 0){
                    CEILING_CLING -= CLING_REDUC;
                } else if (gravityWorking && (!collisionDirection.contains("T") || (CEILING_CLING <=0 && collisionDirection.contains("T")))){
                    //System.out.println(collisionDirection);
                    if (collisionDirection.contains("L") || collisionDirection.contains("R") || fall == false){
                        //System.out.println("SLOWED DOWN");
                        D+=GRAVITY_AMOUNT/10;
                        incYLocal((int)((GRAVITY * D) * delta));
                    } else {
                        D+=GRAVITY_AMOUNT;
                        incYLocal((int)((GRAVITY * D) * delta));
                    }
                } else {
                   U+=GRAVITY_AMOUNT;
                   fall = false;
                   decYLocal((int)((GRAVITY * U) * delta));
                }
            }
        }
    }
    
    public void setXLocal(int amount){
        xlocal = amount;
    }
    
    public void incXLocal(int amount){
        xlocal += amount;
    }
    
    public void decXLocal(int amount){
        xlocal -= amount;
    }
    
    public int getXLocal(){
        return xlocal;
    }
        
    public void setYLocal(int amount){
        ylocal = amount;
    }
    
    public void incYLocal(int amount){
        ylocal += amount;
    }
    
    public void decYLocal(int amount){
        ylocal -= amount;
    }
    
    public int getYLocal(){
        return ylocal;
    }
    
    public void setX(int amount){
        position.setX(amount);
    }
    
    public void incX(int amount){
        position.setX(position.getX() + amount);
    }
    
    public void decX(int amount){
                position.setX(position.getX() - amount);
    }
    
    public int getX(){
        return position.getX();
    }
    
    public void setY(int amount){
        position.setY(amount);
    }
    
    public void incY(int amount){
                position.setY(position.getY() + amount);
    }
    
    public void decY(int amount){
                position.setY(position.getY() - amount);
    }
    
    public int getY(){
        return position.getY();
    }
 
    public void alter(String tex){

    }
}
