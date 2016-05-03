package rbadia.voidspace.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.Ship;
import rbadia.voidspace.sounds.SoundManager;
import rbadia.voidspace.model.EnemyShip;


/**
 * Handles general game logic and status.
 */
public class GameLogic {
	private GameScreen gameScreen;
	private GameStatus status;
	private SoundManager soundMan;
	
	private Ship ship;
	private List<Asteroid> asteroid;
	private List<Bullet> bullets;
	private List<EnemyShip> enemyShip;
	private List<Bullet> enemyBullet;
	
	/**
	 * Craete a new game logic handler
	 * @param gameScreen the game screen
	 */
	public GameLogic(GameScreen gameScreen){
		this.gameScreen = gameScreen;
		
		// initialize game status information
		status = new GameStatus();
		// initialize the sound manager
		soundMan = new SoundManager();
		
		// init some variables
		bullets = new ArrayList<Bullet>();
	}

	/**
	 * Returns the game status
	 * @return the game status 
	 */
	public GameStatus getStatus() {
		return status;
	}

	public SoundManager getSoundMan() {
		return soundMan;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Prepare for a new game.
	 */
	public void newGame(){
		status.setGameStarting(true);
		
		// init game variables
		bullets = new ArrayList<Bullet>();
		enemyShip = new ArrayList<EnemyShip>();
		enemyBullet = new ArrayList<Bullet>();
		asteroid = new ArrayList<Asteroid>();

		status.setShipsLeft(3);
		status.setGameOver(false);
		status.setAsteroidsDestroyed(0);
		status.setNewAsteroid(false);
				
		// init the ship and the asteroid
        newShip(gameScreen);
        //newAsteroid(gameScreen);
        
        // prepare game screen
        gameScreen.doNewGame();
        
        // delay to display "Get Ready" message for 1.5 seconds
		Timer timer = new Timer(1500, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameStarting(false);
				status.setGameStarted(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Check game or level ending conditions.
	 */
	public void checkConditions(){
		// check game over conditions
		if(!status.isGameOver() && status.isGameStarted()){
			if(status.getShipsLeft() == 0){
				gameOver();
			}
		}
	}
	
	/**
	 * Actions to take when the game is over.
	 */
	public void gameOver(){
		status.setGameStarted(false);
		status.setGameOver(true);
		gameScreen.doGameOver();
		
        // delay to display "Game Over" message for 3 seconds
		Timer timer = new Timer(3000, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				status.setGameOver(false);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * Fire a bullet from ship.
	 */
	public void fireBullet(){
		Bullet bullet = new Bullet(ship);
		bullets.add(bullet);
		soundMan.playBulletSound();
	}
	
	public void fireEnemyBullet(EnemyShip ship){
		Bullet bullet = new Bullet(ship);
		enemyBullet.add(bullet);
//		soundMan.stopEnemyBulletSound();
//		soundMan.playEnemyBulletSound();
	}
	
	public List<Bullet> getEnemyBullet()
	{
		return enemyBullet;
	}
	
	/**
	 * Move a bullet once fired.
	 * @param bullet the bullet to move
	 * @return if the bullet should be removed from screen
	 */
	public boolean moveBullet(Bullet bullet){
		if(bullet.getY() - bullet.getSpeed() >= 0){
			bullet.translate(0, -bullet.getSpeed());
			return false;
		}
		else{
			return true;
		}
	}
	
	/** 
	 * move enemy Bullet
	 * @param enemyBullet
	 * @return
	 */
	public boolean moveEnemyBullet(Bullet enemyBullet)
	{
		if(enemyBullet.getY() + enemyBullet.getSpeed() < gameScreen.getHeight()){
			enemyBullet.translate(0, enemyBullet.getSpeed());
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Create a new ship (and replace current one).
	 */
	public Ship newShip(GameScreen screen){
		this.ship = new Ship(screen);
		return ship;
	}
	
	/**
	 * Creates a new enemy ship
	 * @param screen
	 */
	public void newEnemyShip(GameScreen screen){
		this.enemyShip.add(new EnemyShip(screen));
	}
	
	/**
	 * Create a new asteroid.
	 */
	public void newAsteroid(GameScreen screen){
		
		asteroid.add(new Asteroid(screen));
	}
	
	/**
	 * Returns the ship.
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}
	
	/**
	 * Gets the enemy ship
	 * @return
	 */
	public List<EnemyShip> getEnemyShip(){
		return enemyShip;
	}
	

	/**
	 * Returns the asteroid.
	 * @return the asteroid
	 */
	public List<Asteroid> getAsteroid() {
		return asteroid;
	}

	/**
	 * Returns the list of bullets.
	 * @return the list of bullets
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}
}
