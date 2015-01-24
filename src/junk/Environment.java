/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package junk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.openal.AL;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
/**
 *
 * @author Dan Ford
 */
public class Environment {

    public boolean LEVEL_EDITOR = false;
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
    private ArrayList<Integer> levels;
    private int character_count = 0;
    private int scenery_count = 0;
    private int threat_count = 0;
    private int level_loc = 0;
    private int LEVEL_TEMPERATURE = 500;
    private boolean paused = false;
    public float xloc = -400;
    public float yloc = -400;
    
    private int PLAYEROFFSET = 1;
    private int OFFSET = 2;
    Point startingPoint;
    private int DEATH_COUNT = 0;
    private int KILL_COUNT = 0;
    
    public int SHOTFIRED_DIR = -1;
    public MovingCharacter shootingPlayer;
    public int deadPlayer = -1;
    public int pickedUpItem = -1;
    public int doorToRemove = -1;
    public int numOfLevels = 21;
    private int DIFFICULTY;
    public MovingCharacter boomToCreate;
    private int boomSize;
    
    private int invincibilityCooldown = 0;
    private int INV_COOLDOWN_LENGTH = 120;
    
    public int ITEM_SIZE = 10;
    
    private boolean DOOR_EXISTS = true;
    private int OFFSET_GENERATOR = 0;
    private int PLAYER_LOC = -6300;
    private String lastRand = "";
    private int offsetLevelGen = 0;
    private int numOfLevelsGenerated = 0;
    
    public int level = 1;
    public int previouslevel = 1;
    
    public float SIZE_CHANGE = 1f;
    
    /** The ogg sound effect */
    private Audio oggEffect;
    /** The wav sound effect */
    private Audio shotEffect;
    private Audio hitEffect;

    /** The aif source effect */
    private Audio aifEffect;
    /** The ogg stream thats been loaded */
    private Audio oggStream;
    /** The mod stream thats been loaded */
    private Audio wavStream;
    
    public void initSound() {
 
        try {
        // you can play oggs by loading the complete thing into 
        // a sound
        //oggEffect = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("testdata/restart.ogg"));
             
        // or setting up a stream to read from. Note that the argument becomes
        // a URL here so it can be reopened when the stream is complete. Probably
        // should have reset the stream by thats not how the original stuff worked
        //oggStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("testdata/bongos.ogg"));
             
        // can load mods (XM, MOD) using ibxm which is then played through OpenAL. MODs
        // are always streamed based on the way IBXM works
        //wavStream = AudioLoader.getStreamingAudio("WAV", ResourceLoader.getResource("junkaudio/backing.wav"));
 
        // playing as music uses that reserved source to play the sound. The first
        // two arguments are pitch and gain, the boolean is whether to loop the content
        //wavStream.playAsMusic(1.0f, 1.0f, true);
             
        // you can play aifs by loading the complete thing into 
        // a sound
        //aifEffect = AudioLoader.getAudio("AIF", ResourceLoader.getResourceAsStream("testdata/burp.aif"));
 
        // you can play wavs by loading the complete thing into 
        // a sound
        shotEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("junkaudio/shot.wav"));
        hitEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("junkaudio/splosion.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void destroyAL(){
        AL.destroy();
    }

    public String getDebugText() {
        String debugstring = "";
        
        debugstring = "CharacterLocation:";
        
        return debugstring;
    }

    private void createBoom(MovingCharacter player, int size) {
        players.add(new Particle(player.getRelativeX(), player.getRelativeY(), "boom", size, size*5, "lightmissle", false, false));
        raiseCharacterCount();
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
     
    public void setDifficulty(String diff){
        if (diff.equals("easy")){
            DIFFICULTY = 5;
        } else if (diff.equals("normal")){
            DIFFICULTY = 10;
        } else if (diff.equals("hard")){
            DIFFICULTY = 25;
        }
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
    
    public ArrayList<Integer> getLevels(){
        return levels;
    }
    
    public void setLevels(ArrayList<Integer> newlevels){
        levels = newlevels;
    }
    
    public int getLevelLoc(){
        return level_loc;
    }
    
    public void raiseLevelLoc(){
        level_loc++;
    }
    
    public void lowerLevelLoc(){
        level_loc--;
    }
    
    public int getPlayerHealth(){
        return players.get(0).getHealth();
    }
    
    public int getPlayerTemperature(){
        return players.get(0).getTemperature();
    }
    
    public Environment(boolean leveledit) {
        LEVEL_EDITOR = leveledit;
        players = new ArrayList<MovingCharacter>();
        sceneries = new ArrayList<Scenery>();
        threats = new ArrayList<Threat>();
        levels = new ArrayList<Integer>();
        {
            addObject(CharacterType.PLAYABLECHARACTER, 568, 469, 31, 50, 1000, 500, "character", true);
            if (level == 1){
                levelOne();
            }
        }
        setDifficulty("easy");
        initSound();
    }

    public Point onClockTick(int delta) {
        
        /* //This is for advancing levels
        if (level != previouslevel){
            if (level == 1){
                
            }
        }
        previouslevel = level; */
        
        String collisionDirection = "";
        char seekDir = ' ';
        int counter = -1;
        int enemycounter = 0;
        /**
         * Iterates through all players
         * This allows them to compare against scenery, threats, and other moving items
         * Main loop of setting x and y locations to later draw on screen
         */
        for (MovingCharacter player : players) {
            collisionDirection = "";
            counter++;
            player.setEditor(getLevelEditor());
            if ((player instanceof PlayableCharacter == false) && player.ALIVE == false){
                    deadPlayer = counter;
            } else {
                /* If not the main character, it sets the location on screen, adjusting for the screen sliding */
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
                        enemycounter++;
                    } else {
                        player.position.setX(((int)((player.getRelativeX() + xloc))));
                        player.position.setY(((int)((player.getRelativeY() + yloc))));
                    }
                }

                /* Sets the shot direction from the player so that you can later shoot it*/
                if (player.SHOT_DIR != -1){
                    shootingPlayer = player;
                    SHOTFIRED_DIR = player.SHOT_DIR;
                    player.SHOT_DIR = -1;
                }

                int scenerycount = -1;
                /**
                 * ITERATE THROUGH ALL SCENERY OBJECTS
                 * Scenery includes ground, blocks, and other stationary items that do not move
                 * Also currently includes items
                 */
                for (Scenery scenery : sceneries){
                    scenerycount++;
                    if (scenery.getTexture() == "woodblock" && DOOR_EXISTS == false){
                        doorToRemove = scenerycount;
                    }
                    if(player instanceof PlayableCharacter){
                        scenery.position.setX(((int)(scenery.getRelativeX() + xloc)));
                        scenery.position.setY(((int)(scenery.getRelativeY() + yloc)));
                    }
                    scenery.update(delta, "");
                    if (!paused){
                        if ((player instanceof PlayableCharacter && scenery instanceof Item) || scenery instanceof Item == false){
                            String collisionDir = compareLocationBetter(player, scenery);
                            if (collisionDir != ""){
                                collisionDirection += collisionDir;
                                if (player instanceof PlayableCharacter){
                                     if (scenery instanceof Item){
                                        player.alter(scenery.getTexture());
                                        pickedUpItem = scenerycount;
                                    }
                                } else if (player instanceof Projectile){
                                    boomToCreate = player;
                                    boomSize = 2;
                                    player.ALIVE = false;
                                }
                            }
                        }
                    }
                }
                /**
                 * ITERATE THROUGH ALL THREATS
                 * Threats include fire and other stationary dangers the player may face
                 */
                for (Threat threat : threats){
                    if(player instanceof PlayableCharacter){
                        threat.position.setX((int)(threat.getRelativeX() + xloc));
                        threat.position.setY((int)(threat.getRelativeY() + yloc));
                    }
                    threat.update(delta, "");
                    threat.updateIterator(delta);
                    //boolean possibleCollision = compareQuads(player.getQuad(), threat.getQuad());
                    //if (possibleCollision){
                        if (!paused){
                            String collisionDir = compareLocationBetter(player, threat);
                            if (collisionDir != ""){
                                collisionDirection = collisionDir;
                                player.incTemperature(((threat.getTemperature() - player.getTemperature())));
                                player.lowerHealth(1);
                            }
                        }
                    //}
                }
                /**
                 * Iterates through all movable items
                 * This allows players to collide with enemies, for shots to hit enemies, 
                 * and for other movable items to hit each other
                 */
                for (MovingCharacter player2 : players){
                    if (player != player2){
                        if (player2 instanceof PlayableCharacter && player instanceof Projectile){
                            if (player.mylocalX == null){
                                player.mylocalX = xloc;
                            }
                            if (player.mylocalY == null){
                                player.mylocalY = yloc;
                            }
                            //player.position.setX(((int)((player.getRelativeX() - (player.mylocalX-xloc)))));
                            //player.position.setY(((int)((player.getRelativeY() - (player.mylocalY-yloc)))));
                        }else if (player instanceof PlayableCharacter && player2 instanceof Projectile){
                            if (player2.mylocalX == null){
                                player2.mylocalX = xloc;
                            }
                            if (player2.mylocalY == null){
                                player2.mylocalY = yloc;
                            }
                            //player2.position.setX(((int)((player2.getRelativeX() - (player2.mylocalX-xloc)))));
                            //player2.position.setY(((int)((player2.getRelativeY() - (player2.mylocalY-yloc)))));
                        } else if (player instanceof Projectile && player2 instanceof Projectile){

                        } else if (player instanceof Particle || player2 instanceof Particle){
                            
                        } else {
                            if (!paused){
                                String collisionDir;
                                if (player instanceof PlayableCharacter && player2 instanceof Enemy){
                                     collisionDir = compareLocationBetter(player, player2);
                                     if (player2.getTexture().equals("chaser") || player.getTexture().equals("bulker")){
                                         seekDir = seek(player2, player);
                                     }
                                }else if (player2 instanceof PlayableCharacter && player instanceof Enemy){
                                     collisionDir = compareLocationBetter(player2, player);
                                     if (player.getTexture().equals("chaser") || player.getTexture().equals("bulker")){
                                         seekDir = seek(player, player2);
                                     }
                                } else {
                                    collisionDir = compareLocationBetter(player, player2);
                                }
                                if (collisionDir != ""){
                                    collisionDirection = collisionDir;
                                    /* if projectile hits enemy, damages them. if health 0, they are cued to be removed */
                                    //Random r = new Random();
                                    //float pitch = r.nextFloat();
                                    if (player instanceof Enemy && player2 instanceof PlayableCharacter){
                                        if (invincibilityCooldown <= 0){
                                            System.out.println("HIT2");
                                            player2.lowerHealth((int)player.SHOT_DAMAGE);
                                            invincibilityCooldown = INV_COOLDOWN_LENGTH;
                                        }
                                        hitEffect.playAsSoundEffect(.2f, 0.5f, false);
                                        boomToCreate = player2;
                                        boomSize = 2;
                                        if (player2.getHealth() <= 0){
                                            player2.ALIVE = false;
                                        }
                                        //TODO
                                        //player.Seek();
                                    } else if (player2 instanceof Enemy && player instanceof PlayableCharacter){
                                        if (invincibilityCooldown <= 0){
                                            System.out.println("HIT");
                                            player.lowerHealth((int)player2.SHOT_DAMAGE);
                                            invincibilityCooldown = INV_COOLDOWN_LENGTH;
                                        }
                                        hitEffect.playAsSoundEffect(.2f, 0.5f, false);
                                        boomToCreate = player;
                                        boomSize = 2;
                                        if (player.getHealth() <= 0){
                                            player.ALIVE = false;
                                        }
                                        //TODO
                                        //player.Seek();
                                    }else if ((player instanceof Enemy && player2 instanceof Projectile)){
                                        shotEffect.playAsSoundEffect(.2f, 1.5f, false);
                                        player.lowerHealth((int)player2.SHOT_DAMAGE);
                                        player2.ALIVE = false; 
                                        boomToCreate = player2;
                                        boomSize = 5;
                                        if (player.getHealth() <= 0){
                                            boomToCreate = player;
                                            boomSize = 7;
                                            player.ALIVE = false;
                                        }
                                   /* if projectile hits enemy, damages them. if health 0, they are cued to be removed */
                                   } else if ((player2 instanceof Enemy && player instanceof Projectile)){
                                        shotEffect.playAsSoundEffect(.2f, 1.5f, false);
                                        player2.lowerHealth((int)player.SHOT_DAMAGE);
                                        player.ALIVE = false;
                                        boomToCreate = player;
                                        boomSize = 5;
                                        if (player2.getHealth() <= 0){
                                            boomToCreate = player2;
                                            boomSize = 7;
                                            player2.ALIVE = false;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            if ((player.ALIVE == false || yloc < -1500) && player instanceof PlayableCharacter){
                player.setHealth(1000);
                player.ALIVE = true;
                player.setXLocal(startingPoint.getX());
                xloc = startingPoint.getX();
                player.setYLocal(startingPoint.getY());
                yloc = startingPoint.getY();
                player.fall = true;
                DEATH_COUNT++;
                System.out.println("---------------------");
                System.out.println("Death Count:" + DEATH_COUNT + "\nKill Count:" + KILL_COUNT);
                System.out.println("---------------------");
                KILL_COUNT = 0;
            } else if (player.getRelativeY() > 1500 && player instanceof Enemy){
                player.ALIVE= false;
            }

            if (!paused){
                if (player.getTexture().equals("chaser") || player.getTexture().equals("bulker")){
                    player.update(delta, collisionDirection, seekDir);
                } else {
                    player.update(delta, collisionDirection);
                }
                if (player instanceof PlayableCharacter){
                    if ( invincibilityCooldown > 0){
                        if (invincibilityCooldown % 10 == 0){
                            if (!player.getTexture().equals("darkmissle") ){
                                player.setTex("darkmissle");
                            } else {
                                player.setTex("darkmissle");
                            }
                        }
                    } else if (invincibilityCooldown <= 0){
                        player.setTex("character");
                    }
                    invincibilityCooldown--;
                    xloc = -(player.getXLocal());
                    yloc = -(player.getYLocal());
                    if (player.getTemperature() > LEVEL_TEMPERATURE){
                        player.decTemperature(1);
                    } else if (player.getTemperature() < LEVEL_TEMPERATURE){
                        player.incTemperature(1);
                    }
                }
            }
        }
        /**
         * Removes a dead enemy if cue called earlier
         * Adds item to screen
         * Removes the dead enemy from array 
         */
        if (deadPlayer != -1){
            if (players.get(deadPlayer) instanceof Enemy){
                KILL_COUNT++;
                Random rand = new Random();
                int itemchance = rand.nextInt(100);
                if (itemchance < 10){
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
            }
            players.remove(deadPlayer);
            lowerCharacterCount();
            deadPlayer = -1;
            counter -= 1;
        }
        /* Picks up item if given cue earlier, removes cue and item from array */
        if (pickedUpItem != -1){
            if (sceneries.get(pickedUpItem).getTexture() == "darkitem"){
                DOOR_EXISTS = false;
            }
            if (sceneries.get(pickedUpItem).position.getWidth() >= 60){
                startingPoint = new Point(sceneries.get(pickedUpItem).getRelativeX() - 600,
                        sceneries.get(pickedUpItem).getRelativeY() - 1000);
            }
            
            sceneries.remove(pickedUpItem);
            lowerSceneryCount();
            pickedUpItem = -1;
        }
        if (doorToRemove != -1){
            generateLevel(OFFSET_GENERATOR);
            hitEffect.playAsSoundEffect(1.3f, 1f, false);
            sceneries.remove(doorToRemove);
            lowerSceneryCount();
            doorToRemove = -1;
            DOOR_EXISTS = true;
        }
        if (xloc > PLAYER_LOC){
            generateLevel(OFFSET_GENERATOR);
            PLAYER_LOC += 1000;
        }
        /**
         * Creates a bullet/projectile if given cue earlier in code
         * Creates projectile, and gets rid of call to create another
         * adds projectile to array
         */
        if (SHOTFIRED_DIR != -1){
            players.add(new Projectile(shootingPlayer.position.getX() + shootingPlayer.position.getWidth()/2-(int)(shootingPlayer.SHOT_SIZE/2), 
                shootingPlayer.position.getY() + shootingPlayer.position.getHeight()/2-(int)(shootingPlayer.SHOT_SIZE/2), (int)shootingPlayer.SHOT_SIZE, 
                (int)shootingPlayer.SHOT_SIZE, (int)shootingPlayer.SHOT_DAMAGE, SHOTFIRED_DIR, "missle", false, false));
            raiseCharacterCount();
            shotEffect.playAsSoundEffect(2-shootingPlayer.SHOT_SIZE/20, 1.0f, false);
            players.get(counter+1).setRelativeX(shootingPlayer.position.getX() + shootingPlayer.position.getWidth()/2-(int)(shootingPlayer.SHOT_SIZE/2) -(int)xloc);
            players.get(counter+1).setRelativeY(shootingPlayer.position.getY() + shootingPlayer.position.getHeight()/2-(int)(shootingPlayer.SHOT_SIZE/2) -(int)yloc);
            
            boomToCreate = players.get(counter+1);
            boomSize = 2;
            
            SHOTFIRED_DIR = -1;
            players.get(counter+1).mylocalX = xloc;
            players.get(counter+1).mylocalY = yloc;
        }
        
        if (boomToCreate != null){
            createBoom(boomToCreate, boomSize);
            boomToCreate = null;
            boomSize = 0;
        }
        updateBackground(xloc,yloc);
        Point locs = new Point((int)xloc, (int)yloc);
        return locs;
    }
    
    private char seek(MovingCharacter chaser, MovingCharacter player){
        int chaserrightx, chaserleftx;
        int chaserbottomy, chasertopy;
        int playerrightx, playerleftx;
        int playerbottomy, playertopy;
        
        chaserrightx = chaser.position.getX() + chaser.position.getWidth();
        chaserbottomy = chaser.position.getY() + chaser.position.getHeight();
        chaserleftx = chaser.position.getX();
        chasertopy = chaser.position.getY();
        
        playerleftx = player.position.getX();
        playerrightx = player.position.getX() + player.position.getWidth();
        playerbottomy = player.position.getY() + player.position.getHeight();
        playertopy = player.position.getY();
        
        if (playerbottomy > chasertopy - 100 && playertopy < chaserbottomy + 100){
            if (playerrightx < chaserleftx && chaserleftx - playerrightx < 200){
                return 'L';
            } else if (playerleftx > chaserrightx && playerleftx - chaserrightx < 200) {
                return 'R';
            } else {
                return ' ';
            }
        } else {
            return ' ';
        }
    }
   
    private String compareLocationBetter(MovingCharacter character, DefaultCharacter object) {
        String direction = "";
        int characterrightx, characterleftx;
        int characterbottomy, charactertopy;
        int objectrightx, objectleftx;
        int objectbottomy, objecttopy; 
        
        if (character instanceof PlayableCharacter){
            characterleftx = character.position.getX();
            characterrightx = character.position.getX() + character.position.getWidth();
            characterbottomy = character.position.getY() + character.position.getHeight();
            charactertopy = character.position.getY();
            if (object instanceof Scenery){
                objectrightx = object.position.getX() + object.position.getWidth();
                objectbottomy = object.position.getY() + object.position.getHeight();
                objectleftx = object.position.getX();
                objecttopy = object.position.getY();
            } else if (object instanceof Enemy || object instanceof Projectile){
                objectrightx = object.position.getX() + object.position.getWidth();
                objectbottomy = object.position.getY() + object.position.getHeight();
                objectleftx = object.position.getX();
                objecttopy = object.position.getY();
            } else {
                objectrightx = object.position.getX() + object.position.getWidth();
                objectbottomy = object.position.getY() + object.position.getHeight();
                objectleftx = object.position.getX();
                objecttopy = object.position.getY();
            }
        } else if (character instanceof Enemy) {
            characterleftx = character.getRelativeX();
            characterrightx = character.getRelativeX() + character.position.getWidth();
            characterbottomy = character.getRelativeY() + character.position.getHeight();
            charactertopy = character.getRelativeY();
            
            objectrightx = object.getRelativeX() + object.position.getWidth();
            objectleftx = object.getRelativeX();
            objectbottomy = object.getRelativeY() + object.position.getHeight();
            objecttopy = object.getRelativeY();
        } else if (character instanceof Projectile) {
            characterleftx = character.getRelativeX();
            characterrightx = character.getRelativeX() + character.position.getWidth();
            characterbottomy = character.getRelativeY() + character.position.getHeight();
            charactertopy = character.getRelativeY();
            
            objectrightx = object.getRelativeX() + object.position.getWidth();
            objectleftx = object.getRelativeX();
            objectbottomy = object.getRelativeY() + object.position.getHeight();
            objecttopy = object.getRelativeY();
        }else {
            characterleftx = character.getRelativeX();
            characterrightx = character.getRelativeX() + character.position.getWidth();
            characterbottomy = character.getRelativeY() + character.position.getHeight();
            charactertopy = character.getRelativeY();
            
            objectrightx = object.position.getX() + object.position.getWidth();
            objectbottomy = object.position.getY() + object.position.getHeight();
            objectleftx = object.position.getX();
            objecttopy = object.position.getY();
        }
        
        if(((characterleftx > objectleftx) && (characterleftx < objectrightx))
                ||((characterrightx > objectleftx) && (characterrightx < objectrightx)) 
                || (characterleftx < objectleftx && characterrightx > objectleftx)
                || (characterleftx < objectrightx && characterrightx > objectrightx)){
            if(characterbottomy >= objecttopy && characterbottomy <= objectbottomy){
                //DOWN
                if(charactertopy < objecttopy){
                    //if (object instanceof Scenery){
                        if (character instanceof Enemy || character instanceof Projectile){
                            if ((characterbottomy - objecttopy) >= 0){
                                character.decRelativeY((characterbottomy - objecttopy));
                            }
                        } else {
                            if ((characterbottomy - objecttopy) >= 5*PLAYEROFFSET+1){
                                character.setYLocal(character.getYLocal()-PLAYEROFFSET*5);
                            } else if (characterbottomy - objecttopy >= 1) {
                                character.setYLocal(character.getYLocal()-PLAYEROFFSET);
                            }
                        }
                    //}
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        if (objectrightx-characterrightx < 15){
                            //character.setXLocal(character.getXLocal()-(characterrightx-objectleftx));
                            character.R = 35;
                        } else if (characterleftx-objectleftx < 15) {
                            //character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                            character.L = 35;
                        } else {
                            character.U +=20;
                        }
                        character.U = 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'B';
                        character.D = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                }else if (characterrightx > objectleftx && characterleftx< objectleftx){
                    //RIGHT
                    if (object instanceof Scenery){
                        if (characterrightx > objectleftx){
                            if (character instanceof Enemy || character instanceof Projectile){
                                character.decRelativeX((characterrightx - objectleftx));
                            } else {
                                character.setXLocal(character.getXLocal()-(characterrightx - objectleftx));
                            }
                        }
                    }
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        //character.setXLocal(character.getXLocal()-(characterrightx-objectleftx));
                        character.R = 35;
                        character.U = 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'R';
                        character.R = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                } else if (characterleftx < objectrightx && characterrightx > objectrightx){
                    //LEFT
                    if (object instanceof Scenery){
                        if (characterleftx < objectrightx){
                            if (character instanceof Enemy || character instanceof Projectile){
                                character.incRelativeX((objectrightx-characterleftx));
                            } else {
                                character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                            }
                        }
                    }
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        //character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                        character.L = 35;
                        character.U = 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'L';
                        character.L = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                } else if(charactertopy >= objecttopy){
                    if (object instanceof Scenery){
                        if (characterbottomy >= objecttopy){
                            if (character instanceof Enemy || character instanceof Projectile){
                                if ((characterbottomy - objecttopy) >= 0){
                                    character.decRelativeY((characterbottomy - objecttopy));
                                }
                            } else {
                                if ((characterbottomy - objecttopy) >= 5*PLAYEROFFSET+1){
                                    character.setYLocal(character.getYLocal()-PLAYEROFFSET*5);
                                } else if (characterbottomy - objecttopy >= 1) {
                                    character.setYLocal(character.getYLocal()-PLAYEROFFSET);
                                }
                            }
                        }
                    }
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        if (objectrightx-characterrightx < 15){
                            //character.setXLocal(character.getXLocal()-(characterrightx-objectleftx));
                            character.R = 35;
                        } else if (characterleftx-objectleftx < 15) {
                            //character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                            character.L = 35;
                        } else {
                            character.U +=20;
                        }
                        character.U += 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'B';
                        character.D = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                }
            } else if (charactertopy <= objectbottomy && charactertopy >= objecttopy){
                //UP
                //if (object instanceof Scenery){
                    if (charactertopy < objectbottomy){
                        if (character instanceof Enemy || character instanceof Projectile){
                            if ((objectbottomy-charactertopy) >= 0){
                                character.incRelativeY(objectbottomy-charactertopy);
                            }
                        } else {
                            if ((objectbottomy-charactertopy) >= 5*PLAYEROFFSET){
                                character.setYLocal(character.getYLocal()+PLAYEROFFSET*5);
                            } else {
                                character.setYLocal(character.getYLocal()+PLAYEROFFSET);
                            }
                        }
                    }
                //}
                if (object instanceof Item == false){
                        direction += 'T';
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
            } else if (charactertopy <= objecttopy && characterbottomy >= objectbottomy){
                if (characterrightx > objectleftx && characterrightx < objectrightx){
                    //RIGHT
                    if (object instanceof Scenery){
                        if (characterrightx > objectleftx){
                            if (character instanceof Enemy || character instanceof Projectile){
                                character.decRelativeX((characterrightx - objectleftx));
                            } else {
                                character.setXLocal(character.getXLocal()-(characterrightx - objectleftx));
                            }
                        }
                    }
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        //character.setXLocal(character.getXLocal()-(characterrightx-objectleftx));
                        character.L = 35;
                        character.U = 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'R';
                        character.R = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                } else if (characterleftx < objectrightx && characterleftx > objectleftx){
                    //LEFT
                    if (object instanceof Scenery){
                        if (characterleftx < objectrightx){
                            if (character instanceof Enemy || character instanceof Projectile){
                                character.incRelativeX((objectrightx-characterleftx));
                            } else {
                                character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                            }
                        }
                    }
                    if (character instanceof PlayableCharacter  && object instanceof Enemy){
                        //character.setXLocal(character.getXLocal()+(objectrightx-characterleftx));
                        character.R = 35;
                        character.U = 20;
                    }
                    if (object instanceof Item == false){
                        direction += 'L';
                        character.L = 0;
                    } else if (object instanceof Item){
                        direction += 'G';
                    } else {
                        direction += 'A';
                    }
                }
            }
        }
       
        return direction;
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
    
    public void generateLevel(int offseter){
        
        Random r = new Random();
        int rand = r.nextInt(10);
        int offsetMultiplier = 1;
        String text = "";
        if (rand < 3){
            text = "block";
        } else if (rand < 6){
            text = "tallblock";
        } else if (rand < 9){
            text = "darkblock";
        } else {
            text = "woodblock";
        }
        rand = r.nextInt(numOfLevels);
        if (!levels.isEmpty()){
            if (levels.size() > 10){
                offsetLevelGen++;
            } else if (levels.size() == 10){
                offsetLevelGen = 5;
            }
            if (numOfLevelsGenerated % DIFFICULTY == 0){
                rand = 21;
            } else {
                boolean breaker = false;
                while (breaker == false){
                    boolean found = false;
                    if (levels.size() - offsetLevelGen > 10){
                        offsetLevelGen += 5;
                    }
                    for (int i = offsetLevelGen; i < levels.size(); i ++){
                        //System.out.println("lastrand" + levels.get(i) + ", rand:" + rand);
                        if (levels.get(i) == rand){
                            found = true;
                            //System.out.println("FOUND");
                        }
                    }
                    if (found == false){
                        breaker = true;
                        //System.out.println("BREAKER TRUE");
                    } else {
                        rand = r.nextInt(numOfLevels);
                        //System.out.println("BREAKER FALSE");
                    }
                }
            }
        } else {
            offsetLevelGen = 0;
        }
        if (rand == 0){
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 200, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6521-offseter, 934, 200, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6921-offseter, 934, 200, 400, 500, 500, "floor", false);
        }else if (rand == 1){
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6883-offseter, 858, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6638-offseter, 717, 62, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6521-offseter, 775, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6356-offseter, 861, 100, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6210-offseter, 643, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6206-offseter, 643, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6136-offseter, 913, 50, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6536-offseter, 539, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6440-offseter, 564, 150, 37, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6649-offseter, 491, 15, 15, 500, 500, "item", false);
        } else if (rand == 2){
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6169-offseter, 844, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6867-offseter, 727, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6541-offseter, 847, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6276-offseter, 655, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6680-offseter, 665, 100, 25, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6413-offseter, 429, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6356-offseter, 407, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6533-offseter, 511, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6169-offseter, 514, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6233-offseter, 378, 18, 150, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6419-offseter, 360, 15, 15, 500, 500, "item", false);
        } else if (rand == 3){
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 7079-offseter, 469, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6132-offseter, 469, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6148-offseter, 469, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6448-offseter, 469, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6748-offseter, 469, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6789-offseter, 469, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6866-offseter, 626, 75, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6302-offseter, 614, 75, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6575-offseter, 616, 75, 75, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6600-offseter, 758, 20, 20, 500, 500, "item", false);
        } else if (rand == 4) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6767-offseter, 732, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6455-offseter, 876, 93, 93, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6655-offseter, 844, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6464-offseter, 552, 200, 200, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6558-offseter, 466, 20, 20, 500, 500, "item", false);
        } else if (rand == 5) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6836-offseter, 833, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6711-offseter, 908, 50, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6544-offseter, 883, 75, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6203-offseter, 797, 18, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6206-offseter, 797, 62, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6352-offseter, 912, 37, 37, 500, 500, text, false);
        } else if (rand == 6) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6525-offseter, 395, 75, 600, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6565-offseter, 771, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6357-offseter, 542, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6857-offseter, 861, 100, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6545-offseter, 184, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6261-offseter, 668, 187, 187, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6289-offseter, 600, 18, 150, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6554-offseter, 143, 15, 15, 500, 500, "item", false);
        } else if (rand == 7) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6972-offseter, 728, 62, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6746-offseter, 728, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6738-offseter, 728, 18, 150, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6777-offseter, 815, 15, 15, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 6334-offseter, 837, 150, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6761-offseter, 696, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6785-offseter, 671, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6811-offseter, 655, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6266-offseter, 837, 100, 25, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6846-offseter, 626, 75, 75, 500, 500, text, false);
        } else if (rand == 8) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6755-offseter, 846, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6384-offseter, 878, 93, 93, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6604-offseter, 904, 62, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6211-offseter, 904, 62, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6644-offseter, 636, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6443-offseter, 699, 250, 62, 500, 500, text, false);
        } else if (rand == 9) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 6920-offseter, 1284, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6665-offseter, 1216, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6358-offseter, 1284, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6080-offseter, 1237, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5828-offseter, 1284, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5527-offseter, 1233, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5226-offseter, 1284, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5121-offseter, 1085, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 7089-offseter, 1034, 31, 250, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6168-offseter, 993, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6730-offseter, 1169, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6436-offseter, 1234, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6170-offseter, 1189, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5900-offseter, 1189, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5596-offseter, 1141, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5286-offseter, 1192, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6138-offseter, 1074, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6407-offseter, 1098, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6782-offseter, 1066, 75, 75, 500, 500, "bulker", false);
        } else if (rand == 10) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6872-offseter, 811, 50, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6357-offseter, 873, 50, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5974-offseter, 790, 50, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5649-offseter, 699, 75, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5210-offseter, 445, 150, 600, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6164-offseter, 227, 75, 600, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5857-offseter, 153, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5558-offseter, 153, 300, 75, 500, 500, text, false);
            addObject(CharacterType.ITEM, 5709-offseter, 91, 25, 25, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6175-offseter, 155, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5817-offseter, 90, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5589-offseter, 94, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5710-offseter, 33, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6112-offseter, 850, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5822-offseter, 763, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5456-offseter, 752, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6613-offseter, 832, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6370-offseter, 822, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5684-offseter, 653, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5983-offseter, 743, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6881-offseter, 767, 25, 25, 500, 500, "chaser", false);
        } else if (rand == 11) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 6780-offseter, 535, 100, 800, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5345-offseter, 535, 100, 800, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5445-offseter, 536, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5980-offseter, 536, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6472-offseter, 511, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5587-offseter, 510, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5916-offseter, 79, 375, 375, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6087-offseter, 1, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6221-offseter, 20, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5940-offseter, 16, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5634-offseter, 453, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6521-offseter, 449, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6058-offseter, 470, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6805-offseter, 475, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5362-offseter, 476, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5863-offseter, 500, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6254-offseter, 492, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5984-offseter, 500, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6158-offseter, 498, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6085-offseter, 47, 25, 25, 500, 500, "chaser", false);
        } else if (rand == 12) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 6920-offseter, 934, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6611-offseter, 906, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6278-offseter, 1041, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5964-offseter, 1213, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6253-offseter, 1040, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6587-offseter, 906, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5616-offseter, 1045, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5815-offseter, 1045, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5176-offseter, 919, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5375-offseter, 918, 31, 250, 500, 500, text, false);
            addObject(CharacterType.ITEM, 5424-offseter, 1105, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ITEM, 5856-offseter, 1187, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ITEM, 6216-offseter, 1172, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ITEM, 6546-offseter, 1050, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ITEM, 6042-offseter, 1165, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6318-offseter, 970, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6660-offseter, 829, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6037-offseter, 1072, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5713-offseter, 958, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6115-offseter, 1159, 50, 50, 500, 500, "crawler", false);
        } else if (rand == 13) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 5183-offseter, 845, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5531-offseter, 867, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5949-offseter, 829, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6332-offseter, 866, 125, 125, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6593-offseter, 803, 187, 187, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6941-offseter, 888, 93, 93, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6174-offseter, 695, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5638-offseter, 886, 400, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5403-offseter, 908, 62, 62, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6183-offseter, 634, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6814-offseter, 839, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6492-offseter, 834, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6231-offseter, 834, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6085-offseter, 818, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5774-offseter, 765, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5318-offseter, 770, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6000-offseter, 676, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6659-offseter, 669, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6386-offseter, 736, 25, 25, 500, 500, "chaser", false);

        } else if (rand == 14) {
            OFFSET_GENERATOR += 1000;
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
            addObject(CharacterType.SCENERY, 5449-offseter, 1305, 100, 120, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5611-offseter, 1083, 75, 75, 500, 500, text, false);
            addObject(CharacterType.ENEMY, 6921-offseter, 1119, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6021-offseter, 1007, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5831-offseter, 1118, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5559-offseter, 1346, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5750-offseter, 443, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6657-offseter, 446, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5465-offseter, 337, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6292-offseter, 860, 37, 37, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6540-offseter, 857, 37, 37, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5889-offseter, 861, 37, 37, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6140-offseter, 863, 37, 37, 500, 500, "chaser", false);
        } else if (rand == 15) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6121-offseter, 582, 1000, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5121-offseter, 582, 1000, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5765-offseter, 269, 1000, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5767-offseter, -226, 1000, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5576-offseter, 192, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6712-offseter, 124, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5767-offseter, -640, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6671-offseter, -640, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5971-offseter, -840, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5767-offseter, -840, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5716-offseter, -653, 100, 25, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6728-offseter, -657, 100, 25, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6220-offseter, -577, 75, 75, 500, 500, text, false);
            addObject(CharacterType.ENEMY, 6164-offseter, -402, 150, 150, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6428-offseter, -397, 150, 150, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5928-offseter, -408, 150, 150, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6097-offseter, -565, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6370-offseter, -564, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6232-offseter, -634, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5561-offseter, 120, 50, 50, 500, 500, "crawler", false);
        } else if (rand == 16) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 6621-offseter, 833, 500, 500, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5121-offseter, 833, 500, 500, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6180-offseter, 504, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6696-offseter, 815, 125, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5382-offseter, 504, 800, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6142-offseter, 622, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5862-offseter, 963, 600, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6341-offseter, 826, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5926-offseter, 829, 25, 200, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6146-offseter, 908, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 5910-offseter, 762, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6325-offseter, 755, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5573-offseter, 419, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6520-offseter, 422, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6092-offseter, 428, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6713-offseter, 727, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5435-offseter, 745, 75, 75, 500, 500, "bulker", false);
        } else if (rand == 17) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6781-offseter, 868, 250, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6087-offseter, 864, 250, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6507-offseter, 745, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5672-offseter, 730, 100, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5231-offseter, 877, 250, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5942-offseter, 864, 250, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5883-offseter, 267, 500, 500, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6296-offseter, -28, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5951-offseter, -31, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6036-offseter, 248, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6103-offseter, 215, 75, 75, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6119-offseter, 150, 40, 40, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 6207-offseter, -61, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5864-offseter, -65, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6019-offseter, -105, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6517-offseter, 596, 75, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5687-offseter, 576, 75, 75, 500, 500, text, false);
            addObject(CharacterType.ENEMY, 6526-offseter, 524, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5699-offseter, 500, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5968-offseter, 823, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6047-offseter, 819, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6124-offseter, 818, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6198-offseter, 822, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6275-offseter, 823, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6651-offseter, 808, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6394-offseter, 784, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5810-offseter, 808, 75, 75, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 5531-offseter, 796, 75, 75, 500, 500, "bulker", false);
        } else if (rand == 18) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6146-offseter, 414, 75, 600, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5883-offseter, 319, 600, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6482-offseter, 319, 25, 200, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6419-offseter, 599, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6569-offseter, 236, 50, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6219-offseter, 137, 400, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6106-offseter, -9, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5883-offseter, 226, 150, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5810-offseter, 226, 75, 600, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5468-offseter, 895, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5939-offseter, -282, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6278-offseter, -291, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6530-offseter, -79, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6528-offseter, -538, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6084-offseter, -547, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6308-offseter, -871, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6227-offseter, -520, 200, 50, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6314-offseter, -571, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6265-offseter, -359, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5929-offseter, -341, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6093-offseter, -81, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6518-offseter, -610, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6073-offseter, -621, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6297-offseter, -935, 50, 50, 500, 500, "crawler", false);
        } else if (rand == 19) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 5992-offseter, 567, 50, 400, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6123-offseter, 304, 400, 100, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6448-offseter, 626, 150, 37, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6517-offseter, 566, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 6598-offseter, 364, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6227-offseter, 145, 187, 187, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6431-offseter, 626, 18, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5742-offseter, 567, 250, 62, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5853-offseter, 113, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5578-offseter, 110, 31, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5179-offseter, 213, 250, 62, 500, 500, text, false);
            addObject(CharacterType.ITEM, 5287-offseter, 148, 20, 20, 500, 500, "item", false);
            addObject(CharacterType.ENEMY, 6136-offseter, 238, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6435-offseter, 227, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5844-offseter, 37, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5569-offseter, 32, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6293-offseter, 68, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5757-offseter, 716, 150, 150, 500, 500, "bulker", false);
            addObject(CharacterType.ENEMY, 6149-offseter, 872, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6087-offseter, 887, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 5905-offseter, 514, 25, 25, 500, 500, "chaser", false);
        } else if (rand == 20) {
            OFFSET_GENERATOR += 1000;
            addObject(CharacterType.SCENERY, 5121-offseter, 934, 2000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6821-offseter, 705, 250, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6413-offseter, 497, 250, 250, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6116-offseter, 497, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6476-offseter, 241, 75, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5563-offseter, 185, 600, 150, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6408-offseter, 440, 75, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 5319-offseter, 492, 300, 75, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6116-offseter, 465, 200, 50, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6116-offseter, 445, 150, 37, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6126-offseter, -47, 37, 300, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6162-offseter, -47, 100, 100, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6165-offseter, -198, -30, 20, 500, 500, "item", false);
            addObject(CharacterType.SCENERY, 5317-offseter, 215, 50, 200, 500, 500, text, false);
            addObject(CharacterType.ENEMY, 5962-offseter, 128, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6148-offseter, -270, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 5493-offseter, 409, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6199-offseter, 367, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6588-offseter, 897, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6699-offseter, 887, 25, 25, 500, 500, "chaser", false);
            addObject(CharacterType.ENEMY, 6413-offseter, 895, 25, 25, 500, 500, "chaser", false);
        } else if (rand == 21) {
            addObject(CharacterType.SCENERY, 6121-offseter, 934, 1000, 400, 500, 500, "floor", false);
            addObject(CharacterType.SCENERY, 6594-offseter, 223, 100, 800, 500, 500, text, false);
            addObject(CharacterType.SCENERY, 6374-offseter, -1477, 400, 2500, 500, 500, text, false);
            addObject(CharacterType.ITEM, 6532-offseter, -1597, 80, 80, 500, 500, "lightitem", false);
            addObject(CharacterType.ENEMY, 6262-offseter, 853, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6143-offseter, 870, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6389-offseter, -1582, 50, 50, 500, 500, "crawler", false);
            addObject(CharacterType.ENEMY, 6670-offseter, -1561, 50, 50, 500, 500, "crawler", false);
        }
        numOfLevelsGenerated++;
        levels.add(rand);
        for (Integer i : levels){
            System.out.print(i +",");
        }
        System.out.println(".");
        OFFSET_GENERATOR += 1000 * offsetMultiplier;
    }
    
    private void levelOne() {
            
        startingPoint = new Point(8470, 335);
        players.get(0).setXLocal(startingPoint.getX());
        players.get(0).setYLocal(startingPoint.getY());

        GAME_BACKGROUND = "sky";
        GAME_FOREGROUND = "sky";
        GAME_SHADOW = "night";
        GAME_BACKGROUND_RECT = new Rectangle((int)(xloc*BACK_SCROLL), (int)(yloc*BACK_SCROLL), 1800, 1215);
        GAME_FOREGROUND_RECT = new Rectangle((int)(xloc*FORE_SCROLL), (int)(yloc*FORE_SCROLL), 1800, 1204);
        GAME_SHADOW_RECT = new Rectangle((int)xloc, (int)yloc, 1800, 1215);
        
     /*   addObject(CharacterType.SCENERY, -1590, 935, 1000, 400, 500, 500, "floor", false);
        addObject(CharacterType.SCENERY, -590, 735, 680, 100, 500, 500, "floor", false);
        
        addObject(CharacterType.SCENERY, 90, 935, 4000, 400, 500, 500, "floor", false);
        
        addObject(CharacterType.SCENERY, 990, 725, 250, 250, 500, 500, "block", false);
        addObject(CharacterType.SCENERY, 1490, 725, 100, 250, 500, 500, "tallblock", false);        
        addObject(CharacterType.SCENERY, 1990, 725, 100, 250, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 2490, 675, 150, 150, 500, 500, "block", false);
        addObject(CharacterType.SCENERY, 2990, 675, 150, 150, 500, 500, "block", false);
        addObject(CharacterType.SCENERY, 3490, 725, 100, 100, 500, 500, "block", false);
        
        addObject(CharacterType.SCENERY, 4090, 935, 2000, 400, 500, 500, "floor", false);
        
        addObject(CharacterType.SCENERY, 4190, 475, 200, 500, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 4190, -175, 200, 500, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 4790, 475, 200, 500, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 4790, -175, 200, 500, 500, 500, "tallblock", false);
        
        addObject(CharacterType.ENEMY, 1800, 169, 50, 50, 200, 500, "lightenemy", false);
        addObject(CharacterType.ENEMY, 1900, 169, 50, 50, 200, 500, "enemy", false);
        addObject(CharacterType.ENEMY, 2200, 169, 50, 50, 200, 500, "lightenemy", false);
        addObject(CharacterType.ENEMY, 2000, 169, 50, 50, 200, 500, "lightenemy", false);
        addObject(CharacterType.ENEMY, 1900, 169, 50, 50, 200, 500, "enemy", false);
        addObject(CharacterType.ENEMY, 2100, 169, 50, 50, 200, 500, "lightenemy", false);
        addObject(CharacterType.ENEMY, 2400, 169, 50, 50, 200, 500, "lightenemy", false);
        addObject(CharacterType.ENEMY, 2300, 169, 50, 50, 200, 500, "enemy", false);
        addObject(CharacterType.ENEMY, 2500, 169, 50, 50, 200, 500, "lightenemy", false);
        */
        addObject(CharacterType.SCENERY, 8231, 698, 50, 300, 500, 500, "woodblock", false);
        addObject(CharacterType.SCENERY, 8121, 934, 1000, 400, 500, 500, "floor", false);
        addObject(CharacterType.SCENERY, 9121, 934, 1000, 400, 500, 500, "floor", false);
        addObject(CharacterType.SCENERY, 8131, 198, 200, 600, 500, 500, "tallblock", false);

        addObject(CharacterType.SCENERY, 9909, 197, 200, 800, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8953, 913, 150, 37, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8992, 894, 75, 75, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8224, 667, 300, 75, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8246, 523, 187, 187, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9232, 884, 75, 75, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9382, 857, 100, 100, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9539, 803, 156, 156, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9793, 712, 250, 62, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9426, 462, 400, 100, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9814, 271, 200, 50, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9591, -70, 75, 600, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9908, -512, 200, 800, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9787, -305, 200, 50, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9253, 142, 200, 50, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9058, 142, 200, 50, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 9022, 142, 50, 600, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8853, -169, 50, 800, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8757, -189, 250, 62, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8705, 689, 200, 50, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8604, 495, 31, 250, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8777, 329, 100, 25, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8131, -65, 37, 300, 500, 500, "tallblock", false);
        addObject(CharacterType.SCENERY, 8131, -66, 100, 25, 500, 500, "tallblock", false);
        
        addObject(CharacterType.ITEM, 9262, 849, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9422, 816, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9605, 759, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9851, 677, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9875, 497, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9742, 434, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9682, 271, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9679, 129, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9625, -96, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9557, 17, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9556, 207, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9549, 422, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9491, 418, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9514, 391, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9441, 427, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9461, 372, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9327, 102, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 9096, 110, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8911, 195, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8917, 371, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8999, 524, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8859, 663, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8652, 609, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8576, 610, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8375, 497, 15, 15, 500, 500, "item", false);
        addObject(CharacterType.ITEM, 8229, 168, 15, 15, 500, 500, "darkitem", false);
        addObject(CharacterType.ITEM, 8285, 170, 15, 15, 500, 500, "item", false);
        
        addObject(CharacterType.ENEMY, 8572, 18, 50, 29, 200, 500, "crawler", false);
        //addObject(CharacterType.ENEMY, 8572, 48, 50, 50, 200, 500, "chaser", false);
        //addObject(CharacterType.ENEMY, 8572, 108, 50, 50, 200, 500, "chaser", false);
        //addObject(CharacterType.ENEMY, 8572, 168, 50, 50, 200, 500, "chaser", false);
        //addObject(CharacterType.ENEMY, 8572, 768, 50, 50, 200, 500, "bulker", false);
        
        addObject(CharacterType.SCENERY, 7121, 934, 1000, 400, 500, 500, "floor", false);
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
