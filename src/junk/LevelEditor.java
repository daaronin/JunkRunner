/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package junk;

import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

/**
 *
 * @author Dan
 */
public class LevelEditor {
    public boolean LEVEL_EDITOR = true;
    public boolean LEVEL_COMPLETE = false;
    public boolean CLICKED_CONTINUE = false;
    
    public String GAME_BACKGROUND = "";
    public String GAME_FOREGROUND = "";
    public String GAME_SHADOW = "";
    public Rectangle GAME_BACKGROUND_RECT;
    public Rectangle GAME_FOREGROUND_RECT;
    public Rectangle GAME_SHADOW_RECT;
    private float BACK_SCROLL = 1f;
    private float FORE_SCROLL = 1f;
    
    private ArrayList<MovingCharacter> players;
    private ArrayList<Scenery> sceneries;
    private ArrayList<Threat> threats;
    private int character_count = 0;
    private int scenery_count = 0;
    private int threat_count = 0;
    private int LEVEL_TEMPERATURE = 500;
    private boolean paused = false;
    public float xloc = -400;
    public float yloc = -400;
    
    private int PLAYEROFFSET = 1;
    private int OFFSET = 2;
    Point startingPoint;
    private int DEATH_COUNT = 0;
    
    public int SHOTFIRED_DIR = -1;
    public MovingCharacter shootingPlayer;
    public int deadPlayer = -1;
    public int pickedUpItem = -1;
    
    public int ITEM_SIZE = 10;
    
    boolean PICKED_UP = false;
    boolean FIRST = false;
    DefaultCharacter CLICKED;
    public int CLICKED_HEIGHT;
    public int CLICKED_WIDTH;
    
    public int level = 1;
    public int previouslevel = 1;
    
    public float SIZE_CHANGE = 1f;
    
    private boolean checkClick(DefaultCharacter object, int mousex, int mousey) {
        if (Mouse.isButtonDown(0)){
               if (mousex >= object.position.getX() 
                       && mousex <= object.position.getX() + object.position.getWidth() 
                       && mousey >= object.position.getY() 
                       && mousey <= object.position.getY() + object.position.getHeight()){
                    CLICKED = object;
                    CLICKED_WIDTH = object.position.getWidth();
                    CLICKED_HEIGHT = object.position.getHeight();
                    return true;
               }     
        }
        return false;
    }

    public enum CharacterType {

        PLAYER, ENEMY, THREAT, NPC, OBJECT, SCENERY, PERMEABLESCENERY, HEALINGSCENERY, DECORATION, PROJECTILE, PLAYABLECHARACTER, ITEM;
    }

     private void addObject(CharacterType TYPE, int x, int y, int width, int height, int health, int temperature, String tex, boolean editor) {
        if (TYPE == CharacterType.PROJECTILE) {
            players.add(new Projectile(x, y, width, height, health, temperature, tex, editor, LEVEL_EDITOR));
            raiseCharacterCount();
        } else if (TYPE == CharacterType.PLAYABLECHARACTER) {
            players.add(new PlayableCharacter(x, y, width, height, health, temperature, tex, editor, LEVEL_EDITOR));
            raiseCharacterCount();
        } else if (TYPE == CharacterType.ENEMY) {
            players.add(new Enemy(x, y, width, height, health, temperature, tex, editor, LEVEL_EDITOR));
            raiseCharacterCount();
        } else if (TYPE == CharacterType.PLAYER) {
            players.add(new MovingCharacter(x, y, width, height, health, temperature, tex, editor, LEVEL_EDITOR));
            raiseCharacterCount();
        } else if (TYPE == CharacterType.SCENERY) {
            sceneries.add(new Scenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.ITEM) {
            sceneries.add(new Item(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.PERMEABLESCENERY) {
            sceneries.add(new PermeableScenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.HEALINGSCENERY) {
            sceneries.add(new HealingScenery(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.DECORATION) {
            sceneries.add(new Decoration(x, y, width, height, health, temperature, tex, editor));
            raiseSceneryCount();
        } else if (TYPE == CharacterType.THREAT) {
            threats.add(new Threat(x, y, width, height, health, temperature, tex, editor));
            raiseThreatCount();
        }
        //}

    }
     
    public void setLevelEditor(boolean set){
        LEVEL_EDITOR = set;
    }
    
    public boolean getLevelEditor(){
        return LEVEL_EDITOR;
    }
    
    public boolean getPaused(){
        return paused;
    }
    
    public void pause(){
        paused = true;
    }
    
    public void unpause(){
        paused = false;
    }
     
    public ArrayList<MovingCharacter> getCharacters(){
        return players;
    }
    
    public void setCharacters(ArrayList<MovingCharacter> characters){
        this.players = characters;
    }
    
    public void raiseCharacterCount(){
        character_count++;
    }
    
    public void lowerCharacterCount(){
        character_count--;
    }
    
    public int getCharacterCount(){
        return character_count;
    }
    
    public ArrayList<Scenery> getSceneries(){
        return sceneries;
    }
    
    private void setScenery(ArrayList<Scenery> sceneries){
        this.sceneries = sceneries;
    }
    
    private void raiseSceneryCount() {
        scenery_count++;
    }
    
    private void lowerSceneryCount() {
        scenery_count--;
    }
    
    public int getSceneryCount(){
        return scenery_count;
    }
    
    public void setSceneryCount(int num){
        scenery_count = num;
    }
    
    public ArrayList<Threat> getThreats(){
        return threats;
    }
        
    private void raiseThreatCount() {
        threat_count++;
    }
    
    private void lowerThreatCount() {
        threat_count--;
    }
    
    public int getThreatCount(){
        return threat_count;
    }
    
    public void setThreatCount(int num){
        threat_count = num;
    }
    
    public int getPlayerHealth(){
        return players.get(0).getHealth();
    }
    
    public int getPlayerTemperature(){
        return players.get(0).getTemperature();
    }
    
    public LevelEditor(boolean leveledit) {
        LEVEL_EDITOR = leveledit;
        players = new ArrayList<MovingCharacter>();
        sceneries = new ArrayList<Scenery>();
        threats = new ArrayList<Threat>();
        {
            addObject(CharacterType.PLAYABLECHARACTER, 568, 469, 50, 50, 1000, 500, "character", true);
            if (LEVEL_EDITOR){
                levelEditor();
            }
            if (level == 1){
                levelOne();
            } else if (level == 2){
                levelTwo();
            }
        }
    }

    public Point onClockTick(int delta) {
        
        if (level != previouslevel){
            if (level == 1){
                
            } else if (level == 2){
                levelTwo();
            } else if (level ==  3){
                levelThree();
            } else if (level == 4){
                levelFour();
            } else if (level == 5){
                levelFive();
            }
        }
        previouslevel = level;
        
        int mousex = 0;
        int mousey = 0;
        if (LEVEL_EDITOR){
            mousex = Mouse.getX();
            mousey = 800-Mouse.getY();
        }
            if (Keyboard.isKeyDown(Keyboard.KEY_0)){
                SIZE_CHANGE = 4f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_1)){
                SIZE_CHANGE = .25f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_2)){
                SIZE_CHANGE = .5f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_3)){
                SIZE_CHANGE = .75f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_4)){
                SIZE_CHANGE = 1f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_5)){
                SIZE_CHANGE = 1.25f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_6)){
                SIZE_CHANGE = 1.5f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_7)){
                SIZE_CHANGE = 2f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_8)){
                SIZE_CHANGE = 3f;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_9)){
                SIZE_CHANGE = 4f;
            }

        String collisionDirection = "";
        int counter = -1;
            for (MovingCharacter player : players) {
                collisionDirection = "";
                counter++;
                player.setEditor(getLevelEditor());
                if ((player instanceof PlayableCharacter == false) && player.ALIVE == false){
                        deadPlayer = counter;
                } else {
                    if(player instanceof PlayableCharacter == false){
                        if (player.mylocalX == null){
                            player.mylocalX = xloc;
                        }
                        if (player.mylocalY == null){
                            player.mylocalY = yloc;
                        }
                        if (player instanceof Enemy){
                            player.position.setX(((int)((player.getRelativeX() + xloc))));
                            player.position.setY(((int)((player.getRelativeY() + yloc))));
                        } else {
                            player.position.setX(((int)((player.getRelativeX() + xloc))));
                            player.position.setY(((int)((player.getRelativeY() + yloc))));
                        }
                    }
                    
                    int scenerycount = -1;
                    for (Scenery scenery : sceneries){
                        scenerycount++;
                        
                        if (player.SHOT_DIR != -1){
                            shootingPlayer = player;
                            SHOTFIRED_DIR = player.SHOT_DIR;
                            player.SHOT_DIR = -1;
                        }

                        if(scenery.getEditor() == false && player instanceof PlayableCharacter){
                            scenery.position.setX(((int)(scenery.getRelativeX() + xloc)));
                            scenery.position.setY(((int)(scenery.getRelativeY() + yloc)));
                        }
                        scenery.update(delta, "");
                        if (LEVEL_EDITOR){
                            if (Mouse.isButtonDown(0)&& !PICKED_UP){
                                PICKED_UP = checkClick(scenery, mousex, mousey);
                                FIRST = true;
                            }
                            if (scenery == CLICKED && PICKED_UP && !FIRST){
                                scenery.position.setX(mousex-scenery.position.getWidth()/2);
                                scenery.position.setY(mousey-scenery.position.getHeight()/2);

                                scenery.position.setWidth((int)(CLICKED_WIDTH*SIZE_CHANGE));
                                scenery.position.setHeight((int)(CLICKED_HEIGHT*SIZE_CHANGE));

                                if (Mouse.isButtonDown(1)){
                                    scenery.setRelativeX(mousex-(scenery.position.getWidth()/2)-(int)xloc);
                                    scenery.setRelativeY(mousey-(scenery.position.getHeight()/2)-(int)yloc);
                                    scenery.setEditor(false);
                                    printOutLoc(scenery, mousex, mousey, xloc, yloc);
                                    CLICKED = null;
                                    PICKED_UP = false;
                                }
                            }
                        } else if (!LEVEL_EDITOR){
                            if (!paused){
                                ////String collisionDir = compareLocationBetter(player, scenery);
                               // if (collisionDir != ""){
                                    //collisionDirection = collisionDir;
                                    if (player instanceof PlayableCharacter){
                                         if (scenery instanceof Item){
                                            player.alter(scenery.getTexture());
                                            pickedUpItem = scenerycount;
                                        }
                                    }
                                //}
                            }
                        }
                    }
                    for (Threat threat : threats){
                        if(threat.getEditor() == false && player instanceof Projectile == false && player instanceof Enemy == false){
                            threat.position.setX((threat.getRelativeX() + ((int)xloc)));
                            threat.position.setY((threat.getRelativeY() + ((int)yloc)));
                        }
                        threat.update(delta, "");
                        threat.updateIterator(delta);
                        if (LEVEL_EDITOR){
                            if (Mouse.isButtonDown(0)&& !PICKED_UP){
                                PICKED_UP = checkClick(threat, mousex, mousey);
                                FIRST = true;
                            }
                            if (threat == CLICKED && PICKED_UP && !FIRST){
                                threat.position.setX(mousex-threat.position.getWidth()/2);
                                threat.position.setY(mousey-threat.position.getHeight()/2);

                                threat.position.setWidth((int)(CLICKED_WIDTH*SIZE_CHANGE));
                                threat.position.setHeight((int)(CLICKED_HEIGHT*SIZE_CHANGE));

                                if (Mouse.isButtonDown(1)){
                                    threat.setRelativeX(mousex-(threat.position.getWidth()/2)-(int)xloc);
                                    threat.setRelativeY(mousey-(threat.position.getHeight()/2)-(int)yloc);
                                    threat.setEditor(false);
                                    printOutLoc(threat, mousex, mousey, xloc, yloc);
                                    CLICKED = null;
                                    PICKED_UP = false;
                                }
                            }
                        }else if (!LEVEL_EDITOR){
                            //boolean possibleCollision = compareQuads(player.getQuad(), threat.getQuad());
                            //if (possibleCollision){
                                if (!paused){
                                    //String collisionDir = compareLocationBetter(player, threat);
                                    //if (collisionDir != ""){
                                        //collisionDirection = collisionDir;
                                        player.incTemperature(((threat.getTemperature() - player.getTemperature())));
                                        player.lowerHealth(1);
                                    //}
                                }
                            //}
                        }
                    }
                    for (MovingCharacter player2 : players){
                        if (player2 instanceof PlayableCharacter && player instanceof Projectile){
                            if (player.mylocalX == null){
                                player.mylocalX = xloc;
                            }
                            if (player.mylocalY == null){
                                player.mylocalY = yloc;
                            }
                            player.position.setX(((int)((player.getRelativeX() - (player.mylocalX-xloc)))));
                            player.position.setY(((int)((player.getRelativeY() - (player.mylocalY-yloc)))));
                        }else if (player instanceof PlayableCharacter && player2 instanceof Projectile){
                            if (player2.mylocalX == null){
                                player2.mylocalX = xloc;
                            }
                            if (player2.mylocalY == null){
                                player2.mylocalY = yloc;
                            }
                            player2.position.setX(((int)((player2.getRelativeX() - (player2.mylocalX-xloc)))));
                            player2.position.setY(((int)((player2.getRelativeY() - (player2.mylocalY-yloc)))));
                        } else if (player instanceof Projectile && player2 instanceof Projectile){
                        
                        } else {
                            if (player instanceof Enemy && player2 instanceof PlayableCharacter){
                                //TODO
                                //player.Seek();
                            }
                            if (!paused){
                                //String collisionDir = compareLocationBetter(player, player2);
                                //if (collisionDir != ""){
                                    //System.out.println("COLLISION");
                                    ///collisionDirection = collisionDir;
                                    if ((player instanceof Enemy && player2 instanceof Projectile)){
                                        player.lowerHealth((int)player2.SHOT_DAMAGE);
                                        if (player.getHealth() <= 0){
                                            player.ALIVE = false;
                                        }
                                        player2.ALIVE = false;
                                    } else if ((player2 instanceof Enemy && player instanceof Projectile)){
                                        player2.lowerHealth((int)player.SHOT_DAMAGE);
                                        if (player2.getHealth() <= 0){
                                            player2.ALIVE = false;
                                        }
                                        player.ALIVE = false;
                                    }

                                //}
                            }
                        }
                    }
                }
                if (FIRST){
                    if (PICKED_UP && CLICKED.getEditor() == true){
                        DefaultCharacter object = CLICKED;
                        if (object instanceof HealingScenery){
                            addObject(CharacterType.HEALINGSCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        } else if (object instanceof PermeableScenery){
                            addObject(CharacterType.PERMEABLESCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);                       
                        } else if (object instanceof Decoration){
                            addObject(CharacterType.DECORATION, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);                       
                        } else if (object instanceof Threat){
                            addObject(CharacterType.THREAT, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        } else if (object instanceof Scenery){
                            addObject(CharacterType.SCENERY, object.position.getX(), object.position.getY(), object.position.getWidth(), object.position.getHeight(), object.getHealth(), object.getTemperature(), object.getTexture(), true);
                        }
                    }
                    FIRST = false;
                }
                
                if (yloc < -1500 && player instanceof PlayableCharacter){
                    player.setXLocal(startingPoint.getX());
                    xloc = startingPoint.getX();
                    player.setYLocal(startingPoint.getY());
                    yloc = startingPoint.getY();
                    player.fall = true;
                    DEATH_COUNT++;
                } else if (player.getRelativeY() > 1000 && player instanceof Enemy){
                    player.ALIVE= false;
                }
                
                if (!paused){
                    player.update(delta, collisionDirection);
                    if (player instanceof PlayableCharacter){
                        xloc = -(player.getXLocal()+ player.position.getWidth());
                        yloc = -(player.getYLocal() + player.position.getHeight());
                        if (player.getTemperature() > LEVEL_TEMPERATURE){
                            player.decTemperature(1);
                        } else if (player.getTemperature() < LEVEL_TEMPERATURE){
                            player.incTemperature(1);
                        }
                    }
                }
            }
        if (deadPlayer != -1){
            if (players.get(deadPlayer) instanceof Enemy){
                int enemyx = players.get(deadPlayer).position.getX()-(int)xloc;
                int enemyy = players.get(deadPlayer).position.getY()-(int)yloc;
                String text = "";
                Random r = new Random();
                int type = r.nextInt(3);
                if (type == 2){
                    text = "lightitem"; 
                } else if (type == 1){
                    text = "item";
                } else if (type == 0){
                    text = "darkitem";
                }
                sceneries.add(new Item(enemyx + players.get(deadPlayer).position.getWidth()/2-(int)(players.get(deadPlayer).SHOT_SIZE/2), 
                    enemyy + players.get(deadPlayer).position.getHeight()/2-(int)(players.get(deadPlayer).SHOT_SIZE/2), ITEM_SIZE, 
                    ITEM_SIZE, (int)players.get(deadPlayer).SHOT_DAMAGE, SHOTFIRED_DIR, text, false));
                raiseSceneryCount();
            }
            players.remove(deadPlayer);
            lowerCharacterCount();
            deadPlayer = -1;
            counter -= 1;
        }
        if (pickedUpItem != -1){
            sceneries.remove(pickedUpItem);
            lowerSceneryCount();
            pickedUpItem = -1;
        }
        if (SHOTFIRED_DIR != -1){
            players.add(new Projectile(shootingPlayer.position.getX() + shootingPlayer.position.getWidth()/2-(int)(shootingPlayer.SHOT_SIZE/2), 
                shootingPlayer.position.getY() + shootingPlayer.position.getHeight()/2-(int)(shootingPlayer.SHOT_SIZE/2), (int)shootingPlayer.SHOT_SIZE, 
                (int)shootingPlayer.SHOT_SIZE, (int)shootingPlayer.SHOT_DAMAGE, SHOTFIRED_DIR, "missle", false, false));
            raiseCharacterCount();

            SHOTFIRED_DIR = -1;
            players.get(counter+1).mylocalX = xloc;
            players.get(counter+1).mylocalY = yloc;
            
        }
        updateBackground(xloc,yloc);
        Point locs = new Point((int)xloc, (int)yloc);
        return locs;
    }
    
    public void printOutLoc(DefaultCharacter object, int mousexx, int mouseyy, float xlocx, float ylocy){
        if (object instanceof HealingScenery){
            System.out.println("addObject(CharacterType.HEALINGSCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", text, "+false+");");
        } else if (object instanceof PermeableScenery){
            System.out.println("addObject(CharacterType.PERMEABLESCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", text, "+false+");");
        } else if (object instanceof Decoration){
            System.out.println("addObject(CharacterType.DECORATION, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", text, "+false+");");
        } else if (object instanceof Threat){
            System.out.println("addObject(CharacterType.THREAT, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", text, "+false+");");
        } else if (object instanceof Item){
            System.out.println("addObject(CharacterType.ITEM, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \""+object.getTexture()+"\", "+false+");");
        } else if (object instanceof Scenery){
            if (object.getTexture() == "woodblock"){
                if (object.position.getWidth() < 50){
                    System.out.println("addObject(CharacterType.ENEMY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \"chaser\", "+false+");");                    
                } else if (object.position.getWidth() > 50) {
                    System.out.println("addObject(CharacterType.ENEMY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \"bulker\", "+false+");");
                } else {
                    System.out.println("addObject(CharacterType.ENEMY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", \"crawler\", "+false+");");
                }
            } else {
            System.out.println("addObject(CharacterType.SCENERY, "+(int)(mousexx-(object.position.getWidth()/2)-xlocx)+"-offseter, "+(int)(mouseyy-(object.position.getHeight()/2)-ylocy)+", "+object.position.getWidth()+", "+object.position.getHeight()+", "+object.getHealth()+", "+object.getTemperature()+", text, "+false+");");
            }
        }
    }
   
    public boolean compareQuads(int x, int y){
        boolean collision = false;
        int highestQuad = 32768;
        while (x > 0 && y > 0 && !collision){
            if (x >= highestQuad && y >= highestQuad){
                collision = true;
            } else {
                if (x >= highestQuad){
                    x -= highestQuad;
                }
                if (y >= highestQuad){
                    y -= highestQuad;
                }
            }
            highestQuad = highestQuad/2;
        }
        return collision;
    }
    
        public void clearLevel(){
            for (int i = getSceneryCount()-1; i >= 0; i--){
                sceneries.remove(i);
            }
            setSceneryCount(0);
            for (int i = getThreatCount()-1; i >= 0; i--){
                threats.remove(i);
            }
            setThreatCount(0);
        }
    
        private void levelEditor(){
            /*
            addObject(CharacterType.SCENERY, 840, 40, 51, 11, 500, 500, "mossflat", true);
            addObject(CharacterType.SCENERY, 910, 40, 51, 11, 500, 500, "mosscurve", true);

            addObject(CharacterType.SCENERY, 810, 70, 30, 11, 500, 500, "leftmoss", true);
            addObject(CharacterType.SCENERY, 850, 70, 30, 11, 500, 500, "rightmoss", true);

            addObject(CharacterType.SCENERY, 890, 70, 50, 15, 500, 500, "fatplatform", true);
            addObject(CharacterType.SCENERY, 950, 70, 30, 30, 500, 500, "squareplatform", true);

            addObject(CharacterType.SCENERY, 815, 100, 50, 9, 500, 500, "platform", true);

            addObject(CharacterType.SCENERY, 875, 105, 50, 5, 500, 500, "iceplatform", true);

            addObject(CharacterType.SCENERY, 815, 120, 50, 88, 500, 500, "largestalactite", true);
            addObject(CharacterType.SCENERY, 875, 120, 50, 88, 500, 500, "largestalagmite", true);

            addObject(CharacterType.SCENERY, 935, 120, 25, 44, 500, 500, "smallstalactite", true);
            addObject(CharacterType.SCENERY, 960, 120, 25, 44, 500, 500, "smallstalagmite", true);

            addObject(CharacterType.PERMEABLESCENERY, 815, 220, 17, 53, 500, 500, "iceleft", true);
            addObject(CharacterType.PERMEABLESCENERY, 845, 220, 17, 53, 500, 500, "rockright", true);
            addObject(CharacterType.PERMEABLESCENERY, 875, 220, 17, 53, 500, 500, "iceleft", true);
            addObject(CharacterType.PERMEABLESCENERY, 905, 220, 17, 53, 500, 500, "rockleft", true);

            addObject(CharacterType.THREAT, 40, 620, 20, 50, 500, 900, "fire1", true);
            addObject(CharacterType.THREAT, 70, 620, 20, 50, 500, 100, "ice1", true);

            addObject(CharacterType.DECORATION, 150, 620, 153, 12, 500, 500, "grass", true);

            addObject(CharacterType.DECORATION, 40, 680, 53, 43, 500, 500, "shroom1", true);
            addObject(CharacterType.DECORATION, 100, 680, 53, 43, 500, 500, "shroom2", true);
            addObject(CharacterType.DECORATION, 160, 680, 53, 43, 500, 500, "shroom3", true);
            addObject(CharacterType.DECORATION, 220, 680, 53, 43, 500, 500, "shroom4", true);

            addObject(CharacterType.DECORATION, 40, 735, 37, 40, 500, 500, "shroom", true);

            addObject(CharacterType.DECORATION, 285, 620, 39, 93, 500, 500, "grass1", true);
            addObject(CharacterType.DECORATION, 330, 620, 39, 93, 500, 500, "grass2", true);
            addObject(CharacterType.DECORATION, 390, 620, 39, 93, 500, 500, "grass3", true);
            addObject(CharacterType.DECORATION, 450, 620, 39, 93, 500, 500, "grass4", true);

            addObject(CharacterType.HEALINGSCENERY, 90, 735, 100, 22, 500, 500, "pool1", true);
            */
            addObject(CharacterType.SCENERY, 540, 680, 250, 100, 500, 500, "floor", true);
            addObject(CharacterType.SCENERY, 800, 620, 125, 125, 500, 500, "block", true);
            addObject(CharacterType.SCENERY, 950, 620, 50, 50, 500, 500, "block", true);
            addObject(CharacterType.SCENERY, 950, 680, 200, 50, 500, 500, "floor", true);
            addObject(CharacterType.SCENERY, 1320, 200, 100, 250, 500, 500, "tallblock", true);
            addObject(CharacterType.SCENERY, 1430, 200, 50, 200, 500, 500, "tallblock", true);
            addObject(CharacterType.SCENERY, 1330, 470, 25, 200, 500, 500, "tallblock", true);
            addObject(CharacterType.ITEM, 1360, 470, 20, 20, 500, 500, "item", true);
            addObject(CharacterType.SCENERY, 1360, 570, 50, 50, 500, 500, "woodblock", true);
            
            
        }
    
        private void levelOne() {
            
        startingPoint = new Point(6048, 269);
        players.get(0).setXLocal(startingPoint.getX());
        players.get(0).setYLocal(startingPoint.getY());

        GAME_BACKGROUND = "sky";
        GAME_FOREGROUND = "sky";
        GAME_SHADOW = "night";
        GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
        GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
        GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        
        int offseter = 0;
        String text = "floor";
        
        //addObject(CharacterType.SCENERY, 7121, 934, 1000, 400, 500, 500, "floor", false);
        //addObject(CharacterType.SCENERY, 5121, 934, 2000, 400, 500, 500, "floor", false);
        
        //addObject(CharacterType.SCENERY, 7120, 934, 50, 400, 500, 500, "floor", false);
        //addObject(CharacterType.SCENERY, 5071, 934, 50, 400, 500, 500, "floor", false);
        //addObject(CharacterType.SCENERY, 5888, 934, 50, 400, 500, 500, "floor", false);
        //addObject(CharacterType.SCENERY, 6511, 934, 50, 400, 500, 500, "floor", false);
        //addObject(CharacterType.SCENERY, 5467, 934, 50, 400, 500, 500, "floor", false);
        
            addObject(CharacterType.SCENERY, 6873-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6512-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6123-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5719-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5370-offseter, 934, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5121-offseter, 1272, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5488-offseter, 1084, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5655-offseter, 1084, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6026-offseter, 1084, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5781-offseter, 1183, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6158-offseter, 1171, 600, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6918-offseter, 1187, 62, 62, 500, 500, text, false);
            addObject(CharacterType.ITEM, 7059-offseter, 1026, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ITEM, 5145-offseter, 1024, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 6614-offseter, 570, 50, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6539-offseter, 541, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6219-offseter, 570, 50, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5812-offseter, 570, 50, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6145-offseter, 540, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6235-offseter, 442, 400, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5619-offseter, 934, 18, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5596-offseter, 1405, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5449-offseter, 1405, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5742-offseter, 541, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5836-offseter, 442, 400, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5465-offseter, 429, 50, 400, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6228-offseter, 342, 40, 40, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 6735-offseter, 1187, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5449-offseter, 1305, 100, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5611-offseter, 1083, 75, 75, 500, 500, text, false);



    }
        
        private void levelTwo(){
            startingPoint = new Point(-260,300);
            players.get(0).setXLocal(startingPoint.getX());
            players.get(0).setYLocal(startingPoint.getY());
            
            GAME_BACKGROUND = "blank";
            GAME_FOREGROUND = "blank";
            GAME_SHADOW = "blank";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        }
        
        private void levelThree(){
            players.get(0).setXLocal(500);
            players.get(0).setYLocal(-500);
            
            GAME_BACKGROUND = "cavebackground";
            GAME_FOREGROUND = "caveforeground";
            GAME_SHADOW = "cavebackgrounddark";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        }
        
        private void levelFour(){
            players.get(0).setXLocal(500);
            players.get(0).setYLocal(-500);
            
            GAME_BACKGROUND = "cavebackground";
            GAME_FOREGROUND = "caveforeground";
            GAME_SHADOW = "cavebackgrounddark";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        }
        
        private void levelFive(){
            players.get(0).setXLocal(500);
            players.get(0).setYLocal(-500);
            
            GAME_BACKGROUND = "cavebackground";
            GAME_FOREGROUND = "caveforeground";
            GAME_SHADOW = "cavebackgrounddark";
            GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
            GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
            GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        }
        
        private void updateBackground(float xlocx, float ylocy){
            if (level == 1){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            } else if (level == 2){
                GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
                GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
                GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
            }
        }
}
