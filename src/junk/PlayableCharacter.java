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
public class PlayableCharacter extends MovingCharacter {

    public PlayableCharacter(int x, int y, int width, int height, int health, int temperature, String tex, boolean edit, boolean leveleditor) {
        super(x, y, width, height, health, temperature, tex, edit, leveleditor);
    }
    
    @Override
    public void alter(String tex){
        if ("lightitem".equals(tex)){
            SHOT_SIZE = SHOT_SIZE - SHOT_SIZE/10;
            SHOT_DAMAGE = SHOT_DAMAGE - SHOT_DAMAGE/10;
            SHOT_COOLDOWN_MAX = SHOT_COOLDOWN_MAX - SHOT_COOLDOWN_MAX/10;
        } else if ("item".equals(tex)){
            SHOT_SIZE = 20;
            SHOT_DAMAGE = 100;
            SHOT_COOLDOWN_MAX = 25;
        } else if ("darkitem".equals(tex)){
            SHOT_SIZE = SHOT_SIZE + SHOT_SIZE/10;
            SHOT_DAMAGE = SHOT_DAMAGE + SHOT_DAMAGE/10;
            SHOT_COOLDOWN_MAX = SHOT_COOLDOWN_MAX + SHOT_COOLDOWN_MAX/10;
        }
    }
}
