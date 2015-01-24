/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package junkdisplay;

import java.awt.Font;
import junk.Decoration;
import junk.DefaultCharacter;
import junk.Environment;
import junk.HealingScenery;
import junk.PermeableScenery;
import junk.MovingCharacter;
import junk.Scenery;
import junk.Threat;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import junk.LevelEditor;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Dan Ford
 */
public class JunkDisplayLWJGL {

    Environment e;
    LevelEditor edit;
    private TextureMapper levelmap = null;
    private TextureMapper playermap = null;
    private TextureMapper scenerymap = null;
    private TextureMapper threatmap = null;
    private TextureMapper healthmap = null;
    private TextureMapper numbermap = null;
    private TextureMapper menumap = null;
    private TextureMapper healingmap = null;
    private TextureMapper temperaturemap = null;
    private TextureMapper leveleditormap = null;
    private TextureMapper interactivemap = null;
    private TextureMapper decorationmap = null;
    private TextureMapper moredecorationmap = null;
    private TextureMapper newscenerymap = null;
    private TextureMapper charactermap = null;

    /* choose between level editor or game */
    public boolean LEVEL_EDITOR = false;
    
    public boolean DEBUG = false;
    TrueTypeFont font;
    
    /* time at last frame */
    long lastFrame;

    /* frames per second */
    int fps;

    /* last fps time */
    long lastFPS;
    
    /* Screen Width and Height */
    private int SCREEN_WIDTH = 1200;
    private int SCREEN_HEIGHT = 800;
    
    private float BACK_SCROLL = 1f;
    private float FORE_SCROLL = 1f;
    
    private int counter = 0;
    
    private void initTextures() {

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        levelmap = new TextureMapper();

        levelmap.initSheet(this.getClass().getResource("/junkimages/sky_spritesheet_5_12_2014.png"), "PNG");
        levelmap.addSpriteLocation("bluesky", new Rectangle2D.Float(0f, 0f, .195f, .195f));
        levelmap.addSpriteLocation("cloud", new Rectangle2D.Float(0.39f, 0f, .195f, .195f));
        levelmap.addSpriteLocation("sky", new Rectangle2D.Float(0f, 0.39f, .195f, .195f));
        levelmap.addSpriteLocation("night", new Rectangle2D.Float(.39f, .39f, .195f, .195f));
        
        charactermap = new TextureMapper();

        charactermap.initSheet(this.getClass().getResource("/junkimages/scenery_spritesheet_5_12_2014.png"), "PNG");
        charactermap.addSpriteLocation("character", new Rectangle2D.Float(.388f, .194f, .194f, .194f));
        charactermap.addSpriteLocation("missle", new Rectangle2D.Float(.388f, 0f, .194f, .194f));
        charactermap.addSpriteLocation("lightmissle", new Rectangle2D.Float(.582f, 0f, .194f, .194f));
        charactermap.addSpriteLocation("darkmissle", new Rectangle2D.Float(.582f, .194f, .194f, .194f));
        charactermap.addSpriteLocation("chaser", new Rectangle2D.Float(.194f, .582f, .194f, .194f));
        charactermap.addSpriteLocation("crawler", new Rectangle2D.Float(.194f, .582f, .194f, .194f));
        charactermap.addSpriteLocation("bulker", new Rectangle2D.Float(0f, 0f, .194f, .194f));
        charactermap.addSpriteLocation("lightitem", new Rectangle2D.Float(0f, .194f, .194f, .194f));
        charactermap.addSpriteLocation("item", new Rectangle2D.Float(.194f, 0f, .194f, .194f));
        charactermap.addSpriteLocation("darkitem", new Rectangle2D.Float(.194f, .194f, .194f, .194f));
        
        /*charactermap.addSpriteLocation("character", new Rectangle2D.Float(.588f, .2f, .194f, .294f));
        charactermap.addSpriteLocation("missle", new Rectangle2D.Float(.788f, .2f, .09f, .09f));
        charactermap.addSpriteLocation("lightmissle", new Rectangle2D.Float(.832f, 0f, .084f, .084f));
        charactermap.addSpriteLocation("darkmissle", new Rectangle2D.Float(.582f, .194f, .194f, .194f));
        charactermap.addSpriteLocation("chaser", new Rectangle2D.Float(.194f, .582f, .194f, .194f));
        charactermap.addSpriteLocation("crawler", new Rectangle2D.Float(.204f, .592f, .19f, .11f));
        charactermap.addSpriteLocation("bulker", new Rectangle2D.Float(0f, 0f, .554f, .594f));
        charactermap.addSpriteLocation("lightitem", new Rectangle2D.Float(0f, .194f, .194f, .194f));
        charactermap.addSpriteLocation("item", new Rectangle2D.Float(.194f, 0f, .194f, .194f));
        charactermap.addSpriteLocation("darkitem", new Rectangle2D.Float(.194f, .194f, .194f, .194f)); */
        
        newscenerymap = new TextureMapper();

        newscenerymap.initSheet(this.getClass().getResource("/junkimages/scenery_spritesheet_5_12_2014.png"), "PNG");
        newscenerymap.addSpriteLocation("floor", new Rectangle2D.Float(0f, .392f, .194f, .194f));
        newscenerymap.addSpriteLocation("block", new Rectangle2D.Float(0f, .585f, .194f, .194f));
        newscenerymap.addSpriteLocation("tallblock", new Rectangle2D.Float(.197f, .393f, .192f, .191f));
        newscenerymap.addSpriteLocation("darkblock", new Rectangle2D.Float(.585f, .39f, .194f, .194f));
        newscenerymap.addSpriteLocation("woodblock", new Rectangle2D.Float(.388f, .582f, .194f, .194f));
        
        scenerymap = new TextureMapper();

        scenerymap.initSheet(this.getClass().getResource("/junkimages/scenery_spritesheet_18_11_2014.png"), "PNG");
        scenerymap.addSpriteLocation("mossflat", new Rectangle2D.Float(0f, 0f, .299f, .064f));
        scenerymap.addSpriteLocation("mosscurve", new Rectangle2D.Float(0f, .0643f, .3f, .064f));
        scenerymap.addSpriteLocation("platform", new Rectangle2D.Float(0f, .13f, .3f, .05f));
        scenerymap.addSpriteLocation("iceplatform", new Rectangle2D.Float(0f, .18f, .3f, .025f));
        scenerymap.addSpriteLocation("fatplatform", new Rectangle2D.Float(0f, .205f, .295f, .075f));
        scenerymap.addSpriteLocation("largestalactite", new Rectangle2D.Float(0f, 0.293f, .29f, .52f));
        scenerymap.addSpriteLocation("largestalagmite", new Rectangle2D.Float(.3f, 0f, .285f, .52f));
        scenerymap.addSpriteLocation("smallstalagmite", new Rectangle2D.Float(.267f, .518f, .238f, .33f));
        scenerymap.addSpriteLocation("smallstalactite", new Rectangle2D.Float(.585f, 0f, .238f, .33f));
        scenerymap.addSpriteLocation("squareplatform", new Rectangle2D.Float(.587f, .33f, .197f, .197f));
        scenerymap.addSpriteLocation("leftmoss", new Rectangle2D.Float(.5f, .5275f, .181f, .064f));
        scenerymap.addSpriteLocation("rightmoss", new Rectangle2D.Float(.5f, .5915f, .181f, .064f));
        
        interactivemap = new TextureMapper();
        
        interactivemap.initSheet(this.getClass().getResource("/junkimages/interactive_spritesheet_22_11_2014.png"), "PNG");
        interactivemap.addSpriteLocation("iceright", new Rectangle2D.Float(0f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("rockright", new Rectangle2D.Float(.1325f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("iceleft", new Rectangle2D.Float(.265f, 0f, .1325f, .85f));
        interactivemap.addSpriteLocation("rockleft", new Rectangle2D.Float(.3975f, 0f, .1325f, .85f));
        
        decorationmap = new TextureMapper();
        
        decorationmap.initSheet(this.getClass().getResource("/junkimages/decoration_spritesheet_18_11_2014.png"), "PNG");
        decorationmap.addSpriteLocation("grass", new Rectangle2D.Float(0f, 0f, .6f, .046f));
        decorationmap.addSpriteLocation("shroom1", new Rectangle2D.Float(0f, .05f, .205f, .162f));
        decorationmap.addSpriteLocation("shroom2", new Rectangle2D.Float(0f, .212f, .205f, .162f));
        decorationmap.addSpriteLocation("shroom3", new Rectangle2D.Float(.205f, .048f, .21f, .165f));
        decorationmap.addSpriteLocation("shroom4", new Rectangle2D.Float(.205f, .216f, .21f, .165f));
        decorationmap.addSpriteLocation("grass1", new Rectangle2D.Float(.415f, .05f, .153f, .37f));
        decorationmap.addSpriteLocation("grass2", new Rectangle2D.Float(.588f, 0f, .153f, .37f));
        decorationmap.addSpriteLocation("grass3", new Rectangle2D.Float(.75f, 0f, .153f, .37f));
        decorationmap.addSpriteLocation("grass4", new Rectangle2D.Float(0f, .375f, .1528f, .37f));
        decorationmap.addSpriteLocation("shroom", new Rectangle2D.Float(.153f, .375f, .152f, .162f));
        
        moredecorationmap = new TextureMapper();
        
        moredecorationmap.initSheet(this.getClass().getResource("/junkimages/moredecoration_spritesheet_3_12_2014.png"), "PNG");
        moredecorationmap.addSpriteLocation("grass", new Rectangle2D.Float(0f, 0f, .6f, .046f));
        
        threatmap = new TextureMapper();
        
        //threatmap.initSheet(this.getClass().getResource("/junkimages/fire_spritesheet.png"), "PNG");
        //threatmap.addSpriteLocation("fire1", new Rectangle2D.Float(.00f, 0f, .33f, 1f));
        //threatmap.addSpriteLocation("fire2", new Rectangle2D.Float(.298f, 0f, .333333f, 1f));
        //threatmap.addSpriteLocation("fire3", new Rectangle2D.Float(.61f, 0f, .333333f, 1f));
        threatmap.initSheet(this.getClass().getResource("/junkimages/fireice_spritesheet_16_11_2014.png"), "PNG");
        threatmap.addSpriteLocation("fire1", new Rectangle2D.Float(.00f, 0f, .156f, .41f));
        threatmap.addSpriteLocation("fire2", new Rectangle2D.Float(.156f, 0f, .156f, .41f));
        threatmap.addSpriteLocation("fire3", new Rectangle2D.Float(.312f, 0f, .156f, .41f));
        threatmap.addSpriteLocation("ice1", new Rectangle2D.Float(.47f, 0f, .15f, .41f));
        threatmap.addSpriteLocation("ice2", new Rectangle2D.Float(.0f, .4f, .15f, .41f));
        threatmap.addSpriteLocation("ice3", new Rectangle2D.Float(.1f, .4f, .15f, .41f));

        healingmap = new TextureMapper();
        
        healingmap.initSheet(this.getClass().getResource("/junkimages/pooldrip_spritesheet_16_11_2014.png"), "PNG");
        healingmap.addSpriteLocation("pool1", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool2", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool3", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool4", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("pool5", new Rectangle2D.Float(0f, 0f, .39f, .085f));
        healingmap.addSpriteLocation("drop1", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop2", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop3", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop4", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop5", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop6", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        healingmap.addSpriteLocation("drop7", new Rectangle2D.Float(.0f, 0f, .15f, .1f));
        
        healthmap = new TextureMapper();

        healthmap.initSheet(this.getClass().getResource("/junkimages/waterhealth_spritesheet_11_11_2014.png"), "PNG");
        healthmap.addSpriteLocation("10", new Rectangle2D.Float(0f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("20", new Rectangle2D.Float(0f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("30", new Rectangle2D.Float(.25f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("40", new Rectangle2D.Float(.5f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("50", new Rectangle2D.Float(.5f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("60", new Rectangle2D.Float(.75f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("70", new Rectangle2D.Float(.75f, .25f, .25f, .25f));
        healthmap.addSpriteLocation("80", new Rectangle2D.Float(0f, .5f, .25f, .25f));
        healthmap.addSpriteLocation("90", new Rectangle2D.Float(0f, .75f, .25f, .25f));
        healthmap.addSpriteLocation("100", new Rectangle2D.Float(.25f, 0f, .25f, .25f));
        healthmap.addSpriteLocation("0", new Rectangle2D.Float(.9f, .9f, .01f, .01f));
        healthmap.addSpriteLocation("porthole", new Rectangle2D.Float(.25f, .5f, .25f, .25f));
        
        numbermap = new TextureMapper();

        numbermap.initSheet(this.getClass().getResource("/junkimages/number_spritesheet_11_11_2014.png"), "PNG");
        numbermap.addSpriteLocation("0", new Rectangle2D.Float(0f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("1", new Rectangle2D.Float(.21f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("2", new Rectangle2D.Float(.42f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("3", new Rectangle2D.Float(.63f, 0f, .2f, .2f));
        numbermap.addSpriteLocation("4", new Rectangle2D.Float(0f, .206f, .2f, .2f));
        numbermap.addSpriteLocation("5", new Rectangle2D.Float(.21f, .206f, .2f, .2f));
        numbermap.addSpriteLocation("6", new Rectangle2D.Float(.42f, .206f, .2f, .206f));
        numbermap.addSpriteLocation("7", new Rectangle2D.Float(.63f, .206f, .2f, .206f));
        numbermap.addSpriteLocation("8", new Rectangle2D.Float(0f, .41f, .2f, .206f));
        numbermap.addSpriteLocation("9", new Rectangle2D.Float(.21f, .41f, .2f, .206f));
        numbermap.addSpriteLocation("blank", new Rectangle2D.Float(.63f, .41f, .2f, .206f));
        
        temperaturemap = new TextureMapper();

        temperaturemap.initSheet(this.getClass().getResource("/junkimages/temperature_spritesheet_17_11_2014.png"), "PNG");
        temperaturemap.addSpriteLocation("temp10", new Rectangle2D.Float(0f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp20", new Rectangle2D.Float(.18f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp30", new Rectangle2D.Float(.36f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp40", new Rectangle2D.Float(.54f, 0f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp50", new Rectangle2D.Float(0f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp60", new Rectangle2D.Float(.18f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp70", new Rectangle2D.Float(.36f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp80", new Rectangle2D.Float(.54f, .288f, .18f, .28f));
        temperaturemap.addSpriteLocation("temp90", new Rectangle2D.Float(0f, .576f, .18f, .28f));
        
        menumap = new TextureMapper();
        
        menumap.initSheet(this.getClass().getResource("/junkimages/menu_spritesheet_1_12_2014.png"), "PNG");
        menumap.addSpriteLocation("levelcomplete", new Rectangle2D.Float(0f, 0f, .5f, .15f));
        menumap.addSpriteLocation("darken", new Rectangle2D.Float(0f, .2f, .1f, .1f));
        menumap.addSpriteLocation("paused", new Rectangle2D.Float(0f, .44f, .39f, .27f));
        menumap.addSpriteLocation("pressspacedark", new Rectangle2D.Float(0f, .715f, .35f, .0618f));
        menumap.addSpriteLocation("pressspacelight", new Rectangle2D.Float(0f, .78f, .35f, .0601f));
        menumap.addSpriteLocation("optionsdark", new Rectangle2D.Float(0f, .84f, .17f, .12f));
        menumap.addSpriteLocation("optionslight", new Rectangle2D.Float(0.17f, 0.84f, .17f, .12f));
        menumap.addSpriteLocation("quitlight", new Rectangle2D.Float(0.64f, .695f, .09f, .13f));
        menumap.addSpriteLocation("quitdark", new Rectangle2D.Float(0.5f, 0f, .09f, .13f));
        menumap.addSpriteLocation("d", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("r", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("drip", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("i", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        menumap.addSpriteLocation("p", new Rectangle2D.Float(0.39f, 0f, .39f, 1f));
        
        leveleditormap = new TextureMapper();
       
        leveleditormap.initSheet(this.getClass().getResource("/junkimages/leveleditor_spritesheet_22_11_2014.png"), "PNG");        
        leveleditormap.addSpriteLocation("bottombar", new Rectangle2D.Float(0f, 0f, .78f, .195f));
        leveleditormap.addSpriteLocation("sidebar", new Rectangle2D.Float(0f, .195f, .195f, .78f));
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL11.GL_REPEAT);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    protected void initGLLWJGL() {
        
        if (!LEVEL_EDITOR){
            GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        } else {
            GL11.glViewport(0, 0, 1500, 800);
        }
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        if (!LEVEL_EDITOR){
            GL11.glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
        } else {
            GL11.glOrtho(0, 1500, 800, 0, 1, -1);
        }
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        initTextures();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        createModel();
    }

    protected void createModel() {
        if (LEVEL_EDITOR == true){
            edit = new LevelEditor(LEVEL_EDITOR);
        } else {
            e = new Environment(LEVEL_EDITOR);
        }
    }

    protected void renderGL(Point locs) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glPushMatrix();
        {
            drawBackground(locs);
            drawForeGround();
            if (LEVEL_EDITOR == false){
                if (e.LEVEL_COMPLETE == false){
                    //drawShadow(locs);
                }
            }
            if (DEBUG){
                drawDebug();
            }
            drawUI();
        }
        GL11.glPopMatrix();
    }
    
    protected void startLWJGL(){
        try{
            if (!LEVEL_EDITOR){
                Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            } else {
                Display.setDisplayMode(new DisplayMode(1500, 800));
            }
            Display.create();
	} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
        }
        Display.setTitle("Drip");
     //	Display.setResizable(true);
        initGLLWJGL();
        getDelta(); //initialize the last frame
        lastFPS = getTime();
        initDebug();

        // GAME INTRO HERE
        //intro();

        while( !Display.isCloseRequested()){
                int delta = getDelta();
                menuOptions();
                Point locs = onClockTick(delta);
                renderGL(locs);
                if (LEVEL_EDITOR == false){
                    if (e.LEVEL_COMPLETE){
                        showLevelComplete();
                        if (e.CLICKED_CONTINUE == true){
                            loadNextLevel();
                        }
                    }
                }
                Display.update();
                Display.sync(60);
        }
        e.destroyAL();
        Display.destroy();
    }
    
    private void drawBackground(Point locs) {
        if (LEVEL_EDITOR == false){
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            levelmap.getSheetID());
            drawTile(e.GAME_BACKGROUND_RECT, levelmap.getSpriteLocation(e.GAME_BACKGROUND)); 

            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            levelmap.getSheetID());
            drawTile(e.GAME_FOREGROUND_RECT, levelmap.getSpriteLocation(e.GAME_FOREGROUND));
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            levelmap.getSheetID());
            drawTile(edit.GAME_BACKGROUND_RECT, levelmap.getSpriteLocation(edit.GAME_BACKGROUND)); 

            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            levelmap.getSheetID());
            drawTile(edit.GAME_FOREGROUND_RECT, levelmap.getSpriteLocation(edit.GAME_FOREGROUND));
        }
    }

    private void drawForeGround() {
        String r;
        Rectangle bounds;
        ArrayList<Scenery> sceneries;
        if (LEVEL_EDITOR == false){
            sceneries = e.getSceneries();
        } else {
            sceneries = edit.getSceneries();
        }
        for (DefaultCharacter scenery : sceneries) {
            if(scenery.getEditor() == false){
                r = scenery.getTexture();
                bounds = scenery.getScreenPosition();
                if (scenery instanceof HealingScenery){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            healingmap.getSheetID());

                    drawTile(bounds, healingmap.getSpriteLocation(r));
                } else if (scenery instanceof PermeableScenery){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            interactivemap.getSheetID());
                    
                    drawTile(bounds, interactivemap.getSpriteLocation(r));
                } else if (scenery instanceof Decoration){
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                            decorationmap.getSheetID());
                    
                    drawTile(bounds, decorationmap.getSpriteLocation(r));
                } else {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            newscenerymap.getSheetID());

                    drawTile(bounds, newscenerymap.getSpriteLocation(r));
                }
            }
        }
        if(!LEVEL_EDITOR){
            ArrayList<MovingCharacter> characters = e.getCharacters();
            for (DefaultCharacter character : characters) {
                r = character.getTexture();
                bounds = character.getScreenPosition();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        charactermap.getSheetID());

                drawTile(bounds, charactermap.getSpriteLocation(r));
            }
        }
        ArrayList<Threat> threats;
        if (LEVEL_EDITOR == false){
           threats = e.getThreats();            
        } else {
           threats = edit.getThreats();
        }

        for (DefaultCharacter threat : threats) {
            if(threat.getEditor() == false){
                r = threat.getTexture();
                bounds = threat.getScreenPosition();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        threatmap.getSheetID());

                drawTile(bounds, threatmap.getSpriteLocation(r));
            }
        }
    }
    
    private void drawShadow(Point locs) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        levelmap.getSheetID());
        if (LEVEL_EDITOR == false){
            drawTile(e.GAME_SHADOW_RECT, levelmap.getSpriteLocation(e.GAME_SHADOW));
        } else {
            drawTile(edit.GAME_SHADOW_RECT, levelmap.getSpriteLocation(edit.GAME_SHADOW));
        }
    }
    
    private void drawUI() {
        if (!LEVEL_EDITOR){
            int health = e.getPlayerHealth();
            String texture, firstNumberTexture, secondNumberTexture;
            if (health == 1000){
                texture = "100";
            } else if (health >= 900){
                texture = "90";
            } else if (health >= 800){
                texture = "80";
            } else if (health >= 700){
                texture = "70";
            } else if (health >= 600){
                texture = "60";
            } else if (health >= 500){
                texture = "50";
            } else if (health >= 400){
                texture = "40";
            } else if (health >= 300){
                texture = "30";
            } else if (health >= 200){
                texture = "20";
            } else if (health >= 100){
                texture = "10";
            } else {
                texture = "0";
            }
            if (health > 100){
                Rectangle bounds = new Rectangle(10, 10, 64, 64);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                healthmap.getSheetID());
                drawTile(bounds, healthmap.getSpriteLocation(texture)); 
            }
            Rectangle bounds2 = new Rectangle(10, 10, 64, 64);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            healthmap.getSheetID());
            drawTile(bounds2, healthmap.getSpriteLocation("porthole"));

            int healthTens = (health/100)%100;
            switch (healthTens){
                case 1:
                    firstNumberTexture = "1";
                    break;
                case 2:
                    firstNumberTexture = "2";
                    break;
                case 3:
                    firstNumberTexture = "3";
                    break;
                case 4:
                    firstNumberTexture = "4";
                    break;
                case 5:
                    firstNumberTexture = "5";
                    break;
                case 6:
                    firstNumberTexture = "6";
                    break;
                case 7:
                    firstNumberTexture = "7";
                    break;
                case 8:
                    firstNumberTexture = "8";
                    break;
                case 9:
                    firstNumberTexture = "9";
                    break;
                case 0:
                    firstNumberTexture = "0";
                    break;
                default:
                    if (health == 1000){
                        firstNumberTexture = "0";
                    } else {
                        firstNumberTexture = "blank";
                    }
                    break;
            }
            int healthOnes = (health%100)/10;
            switch (healthOnes){
                case 1:
                    secondNumberTexture = "1";
                    break;
                case 2:
                    secondNumberTexture = "2";
                    break;
                case 3:
                    secondNumberTexture = "3";
                    break;
                case 4:
                    secondNumberTexture = "4";
                    break;
                case 5:
                    secondNumberTexture = "5";
                    break;
                case 6:
                    secondNumberTexture = "6";
                    break;
                case 7:
                    secondNumberTexture = "7";
                    break;
                case 8:
                    secondNumberTexture = "8";
                    break;
                case 9:
                    secondNumberTexture = "9";
                    break;
                case 0:
                    secondNumberTexture = "0";
                    break;
                default:
                    secondNumberTexture = "0";

            }
            if (health != 1000){
                        Rectangle num1 = new Rectangle(29, 28, 13, 26);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                numbermap.getSheetID());
                drawTile(num1, numbermap.getSpriteLocation(firstNumberTexture));

                Rectangle num2 = new Rectangle(42, 28, 13, 26);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                numbermap.getSheetID());
                drawTile(num2, numbermap.getSpriteLocation(secondNumberTexture));
            }

                int temper = e.getPlayerTemperature();
                String textemp;
                if (temper >= 800){
                    textemp = "temp90";                
                } else if (temper >= 700){
                    textemp = "temp80";                
                } else if (temper >= 600){
                    textemp = "temp70";                
                } else if (temper >= 500){
                    textemp = "temp60";                
                } else if (temper >= 400){
                    textemp = "temp50";                
                } else if (temper >= 350){
                    textemp = "temp40";                
                } else if (temper >= 300){
                    textemp = "temp30";                
                } else if (temper >= 200){
                    textemp = "temp20";                
                } else if (temper >= 0){
                    textemp = "temp10";                
                } else {
                    textemp = "temp10";
                } 

                Rectangle temperature = new Rectangle(16, 88, 23, 74);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                temperaturemap.getSheetID());
                drawTile(temperature, temperaturemap.getSpriteLocation(textemp));
        } else {
            /**
             * THIS IS WHERE LEVEL EDITOR SIDE BARS GO
             */
            Rectangle bottombar = new Rectangle(500, 600, 800, 200);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        leveleditormap.getSheetID());
            drawTile(bottombar, leveleditormap.getSpriteLocation("bottombar"));
            
            Rectangle sidebar = new Rectangle(1300, 0, 200, 800);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        leveleditormap.getSheetID());
            drawTile(sidebar, leveleditormap.getSpriteLocation("sidebar"));
            
            String r;
            Rectangle bounds;
            ArrayList<Scenery> sceneries;
            if (!LEVEL_EDITOR){
                sceneries = e.getSceneries();
            } else {
                sceneries = edit.getSceneries();
            }
            for (DefaultCharacter scenery : sceneries) {
                    if(scenery.getEditor() == true){
                        r = scenery.getTexture();
                        bounds = scenery.getScreenPosition();
                        if (scenery instanceof HealingScenery){
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                    healingmap.getSheetID());

                            drawTile(bounds, healingmap.getSpriteLocation(r));
                        }else if (scenery instanceof PermeableScenery){
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                                    interactivemap.getSheetID());

                            drawTile(bounds, interactivemap.getSpriteLocation(r));
                        } else if (scenery instanceof Decoration){
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                                decorationmap.getSheetID());

                            drawTile(bounds, decorationmap.getSpriteLocation(r));
                        } else {
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                                    newscenerymap.getSheetID());

                            drawTile(bounds, newscenerymap.getSpriteLocation(r));
                        }
                    }
                }
            if(!LEVEL_EDITOR){
                ArrayList<MovingCharacter> characters = e.getCharacters();
                for (DefaultCharacter character : characters) {
                    r = character.getTexture();
                    bounds = character.getScreenPosition();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            playermap.getSheetID());

                    drawTile(bounds, playermap.getSpriteLocation(r));
                }
            }
            ArrayList<Threat> threats;
            if (!LEVEL_EDITOR){
                threats = e.getThreats();
            } else {
                threats = edit.getThreats();
            }
            for (DefaultCharacter threat : threats) {
                if(threat.getEditor() == true){
                    r = threat.getTexture();
                    bounds = threat.getScreenPosition();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            threatmap.getSheetID());

                    drawTile(bounds, threatmap.getSpriteLocation(r));
                }
            }
        }
        boolean paused;
        if (!LEVEL_EDITOR){
            paused = e.getPaused();
        } else {
            paused = edit.getPaused();
        }
         if (paused == true){
            Rectangle grey = new Rectangle(0, 0, 800, 600);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        menumap.getSheetID());
            drawTile(grey, menumap.getSpriteLocation("darken"));
            
            Rectangle title = new Rectangle(300, 220, 200, 72);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                        menumap.getSheetID());
            drawTile(title, menumap.getSpriteLocation("paused"));
            
            Rectangle options = new Rectangle(357, 300, 86, 28);
            if (checkHover(options) == true){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(options, menumap.getSpriteLocation("optionslight"));
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(options, menumap.getSpriteLocation("optionsdark"));
            }
            
            Rectangle quit = new Rectangle(377, 335, 46, 32);
            if (checkHover(quit) == true){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(quit, menumap.getSpriteLocation("quitlight"));
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                            menumap.getSheetID());
                drawTile(quit, menumap.getSpriteLocation("quitdark"));
            }
        }
    }

    private void drawTile(Rectangle bounds, Rectangle2D.Float r) {
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(r.x, r.y + r.height);
        GL11.glVertex2f(bounds.getX(), bounds.getY() + bounds.getHeight());

        GL11.glTexCoord2f(r.x + r.width, r.y + r.height);
        GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());

        GL11.glTexCoord2f(r.x + r.width, r.y);
        GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY());

        GL11.glTexCoord2f(r.x, r.y);
        GL11.glVertex2f(bounds.getX(), bounds.getY());

        GL11.glEnd();
    }

    protected void destroyGL() {
       /**/
    }

    protected Point onClockTick(int delta) {
        Point locs;
        if (LEVEL_EDITOR == false){
            locs = e.onClockTick(delta);
        } else {
            locs = edit.onClockTick(delta);
        }
        updateFPS(delta); // update FPS Counter
        return locs;
    }

    protected void resetGL() {
        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public int getDelta(){
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public void updateFPS(int delta){
        if (getTime() - lastFPS > 1000){
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    /**
     * Get the accurate time system
     * 
     * @return 
     */
    public long getTime(){		
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public static void main(String[] args) {
        JunkDisplayLWJGL gl = new JunkDisplayLWJGL();
        gl.startLWJGL();
    }

    private void menuOptions() {
        if (LEVEL_EDITOR == false){
            while(Keyboard.next()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    if (e.getPaused() == false){
                        e.pause();
                    } else {
                        e.unpause();
                    }
                }
                if (e.LEVEL_COMPLETE == true){
                    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                        e.CLICKED_CONTINUE = true;
                    }
                } else if (Keyboard.isKeyDown(Keyboard.KEY_F3)){
                    if (DEBUG){
                        DEBUG = false;
                    } else {
                        DEBUG = true;
                    }
                }
            }
        } else {
            while(Keyboard.next()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    if (edit.getPaused() == false){
                        edit.pause();
                    } else {
                        edit.unpause();
                    }
                }
                if (edit.LEVEL_COMPLETE == true){
                    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                        edit.CLICKED_CONTINUE = true;
                    }
                }
            }
        }
    }

    private void showLevelComplete() {
        
        Rectangle title = new Rectangle(272, counter, 256, 39);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                    menumap.getSheetID());
        drawTile(title, menumap.getSpriteLocation("levelcomplete"));
        
        Rectangle pressspace = new Rectangle(311, counter+50, 179, 16);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,
                    menumap.getSheetID());
        drawTile(pressspace, menumap.getSpriteLocation("pressspacelight"));
        if (counter < 150){
            counter++;
        }
    }

    private void loadNextLevel() {
        e.level++;
        e.clearLevel();
        e.CLICKED_CONTINUE = false;
        e.LEVEL_COMPLETE = false;
        
    }
    
    private boolean checkHover(Rectangle rect) {
        int mousex = Mouse.getX();
        int mousey = 600-Mouse.getY();
        if (mousex >= rect.getX()
                && mousex <= rect.getX() + rect.getWidth()
                && mousey >= rect.getY()
                && mousey <= rect.getY() + rect.getHeight()){
             return true;
        }     
        return false;
    }

    private void drawDebug() {
        Color.white.bind();
        
        String text = e.getDebugText();
        font.drawString(50, 50, text, Color.white);
    }

    private void initDebug() {
        Font awtFont = new Font("Times New Roman", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, false);
    }
}
