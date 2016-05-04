package rbadia.voidspace.sounds;

import java.applet.Applet;
import java.applet.AudioClip;

import rbadia.voidspace.main.GameScreen;

/**
 * Manages and plays the game's sounds.
 */
public class SoundManager {
	private static final boolean SOUND_ON = true;
	
	 private AudioClip gameSong = Applet.newAudioClip(GameScreen.class.getResource(
			    "/rbadia/voidspace/sounds/GalagaSong.wav"));
    private AudioClip shipExplosionSound = Applet.newAudioClip(GameScreen.class.getResource(
    "/rbadia/voidspace/sounds/shipExplosion.wav"));
    private AudioClip thrusterSound = Applet.newAudioClip(GameScreen.class.getResource(
    	    "/rbadia/voidspace/sounds/ThrusterSound.wav"));
    private AudioClip bulletSound = Applet.newAudioClip(GameScreen.class.getResource(
    "/rbadia/voidspace/sounds/laser.wav"));
    private AudioClip gameOverSound = Applet.newAudioClip(GameScreen.class.getResource(
    	    "/rbadia/voidspace/sounds/gameOver.wav"));
    
    
    
    /**
     * Plays the game song.
     */
    public void playGameSong(){
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				gameSong.play();
    			}
    		}).start();
    	}
    }
    /**
     * Plays the Game Over Sound
     */
    public void gameOverSound(){
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				gameOverSound.play();
    			}
    		}).start();
    	}
    }
    /**
     * Plays sound for bullets fired by the ship.
     */
    public void playBulletSound(){
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				bulletSound.play();
    			}
    		}).start();
    	}
    }
    
    /**
     * Plays sound for ship explosions.
     */
    public void playShipExplosionSound(){
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				shipExplosionSound.play();
    			}
    		}).start();
    	}
    }
    
    /**
     * Plays sound for thruster
     */
    public void playThrusterSound(){
		// play sound for asteroid explosions
    	if(SOUND_ON){
    		new Thread(new Runnable(){
    			public void run() {
    				thrusterSound.loop();
    			}
    		}).start();
    	}
    }
    
    /**
     * Plays sound for asteroid explosions.
     */
    public void playAsteroidExplosionSound(){
		// play sound for asteroid explosions
    	if(SOUND_ON){
    		
    	}
    }
    
    public void stopGameSong(){
    	gameSong.stop();
    }
    public void stopThrusterSound(){
    	thrusterSound.stop();
    }
}
