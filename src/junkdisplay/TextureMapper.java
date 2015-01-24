package junkdisplay;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureMapper {

	Texture t = null;
        public static HashMap<String, Rectangle2D.Float> textureMap = new HashMap<String, Rectangle2D.Float>();
	
	
	public Texture initSheet(URL url, String imageFormat){
		InputStream stream = null;
            try {
                stream = url.openStream();
            } catch (IOException ex) {
                Logger.getLogger(TextureMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
		try {
			t = TextureLoader.getTexture(imageFormat, stream);
		} catch (IOException e) {
			System.err.println("Texture loader error");
			System.exit(-1);
		}
		
		return t;
	}
        
        public void addSpriteLocation(String name, Rectangle2D.Float p){
            textureMap.put(name, p);
        }
        
        public Rectangle2D.Float getSpriteLocation(String name){
            return textureMap.get(name);
        }
        
        public void setSpriteLocation(String name, Rectangle2D.Float p){
            textureMap.remove(name);
            textureMap.put(name,p);
        }
	
	public int getSheetID(){
		return t.getTextureID();
	}
	

}
