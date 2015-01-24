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
public class DefaultCharacter{

    Location position = null;
    private int relativeX = 0;
    private int relativeY = 0;
    public Float mylocalX;
    public Float mylocalY;
    
    /**
     * State of keys pressed
     */    
    private int ID;
    private int HEALTH;
    private int HEIGHT;
    private int WIDTH;
    private String tex;
    private int temperature;
    private boolean EDITOR = false;

    State state;
    private String[] textures;
    
    
    public enum State {
        IDLE(0), MOVING(1), ATTACKING(2), BEING_DAMAGED(3);
        
        private int value;
        
        private State(int value) {
            this.value = value;
        }
        
        public static State fromValue(int value){
            switch(value){
                case 0:
                    return State.IDLE;
                case 1:
                    return State.MOVING;
                case 2:
                    return State.ATTACKING;
                case 3:
                    return State.BEING_DAMAGED;
                default: 
                    return State.IDLE;
            }
        }
        
    }

    public DefaultCharacter() {
        this.state = State.IDLE;
        
    }

    public DefaultCharacter(Point p) {
        this();
        position = new Location(p, WIDTH, HEIGHT);
        HEALTH = 100;
        temperature = 500;        
    }
    
    public DefaultCharacter(Point p, int width, int height) {
        this();
        position = new Location(p, width, height);
        HEALTH = 100;
        this.state = State.IDLE;
        temperature = 500;        
    }

    public DefaultCharacter(Point p, int width, int height,  int health) {
        this();
        position = new Location(p, width, height);
        HEALTH = health;
        this.state = State.IDLE;
        temperature = 500;        
    }

    public DefaultCharacter(int x, int y, int width, int height,  int health) {
        this();
        position = new Location(x, y, width, height);
        HEALTH = health;
        this.state = State.IDLE;
        temperature = 500;
    }
    
    public DefaultCharacter(int x, int y, int width, int height,  int health, State state) {
        this();
        position = new Location(x, y, width, height);
        HEALTH = health;
        this.state = state;
        temperature = 500;        
    }
    
    public DefaultCharacter(int x, int y, int width, int height,  int health, String tex) {
        this();
        position = new Location(x, y, width, height);
        HEALTH = health;
        this.tex = tex;
        temperature = 500;
    }
    
    public DefaultCharacter(int x, int y, int width, int height,  int health, int temperature, String tex) {
        this();
        position = new Location(x, y, width, height);
        HEALTH = health;
        this.tex = tex;
        this.temperature = temperature;
    }
    
    public DefaultCharacter(int x, int y, int width, int height,  int health, int temperature, String tex, boolean edit) {
        this();
        position = new Location(x, y, width, height);
        HEALTH = health;
        this.tex = tex;
        this.temperature = temperature;
        EDITOR = edit;
    }
    
    public Rectangle getLocation() {
        return position.getScreenLocation();
    }

    void onClockTick(int delta, String collisionDirection) {
        update(delta, collisionDirection);
    }

    public void update(int delta, String collisionDirection) {
        position.updateQuad(position.getX(),position.getY(),position.getWidth(),position.getHeight());
    }

    public void setTexture(String name) {
        textures[state.value] = name;
    }
    
    public void setTex(String name){
        this.tex = name;
    }

    public Rectangle getScreenPosition() {
        return position.getScreenLocation();
    }
    
    public int getID(){
        return ID;
    }
    
    public int getState(){
        return state.value;
    }
    
    public int getQuad(){
        return position.getQuad();
    }
    
    public int getHealth(){
        return HEALTH;
    }
    
    public void setHealth(int health){
        HEALTH = health;
    }
    
    public void raiseHealth(int amount){
        if (HEALTH < 1000){
            HEALTH += amount;
        }
    }
    
    public void lowerHealth(int amount){
        if (HEALTH > 0){
            HEALTH -= amount;
        }
    }
    
    public int getWidth(){
        return WIDTH;
    }

    public int getHeight(){
        return HEIGHT;
    }
    
    public String getTexture(){
        return tex;
    }
    
    public void setPosition(Location position) {
        this.position = position;
    }

    
    public void setState(State state) {
        this.state = state;
    }

    
    public void setID(int id){
        this.ID = id;
    }

    public void setTemperature(int amount){
        temperature = amount;
    }
    
    public void incTemperature(int amount){
        if (temperature < 1000){
            temperature += amount;
        } else {
            temperature = 1000;
        }
    }
    
    public void decTemperature(int amount){
        if (temperature > 0){
            temperature -= amount;
        } else {
            temperature = 0;
        }
    }
    
    public int getTemperature(){
        return temperature;
    }
    
    public void setRelativeX(int amount){
        relativeX = amount;
    }
    
    public void setRelativeY(int amount){
        relativeY = amount;
    }
    
    public void incRelativeX(int amount){
        relativeX += amount;
    }
    public void decRelativeX(int amount){
        relativeX -= amount;
    }
    
    public void incRelativeY(int amount){
        relativeY += amount;
    }
    
    public void decRelativeY(int amount){
        relativeY -= amount;
    }
    
    public int getRelativeX(){
        return relativeX;
    }
    
    public int getRelativeY(){
        return relativeY;
    }
    
    public boolean getEditor(){
        return EDITOR;
    }
    
    public void setEditor(boolean setting){
        EDITOR = setting;
    }
    
    public void waterPlant(int amount){        

    }
    
    public boolean getBloomed(){
        return false;
    }
    
    public void setBloomed(boolean setting){
    
    }
    
    public boolean isBloomable(){
        return false;
    }
}
