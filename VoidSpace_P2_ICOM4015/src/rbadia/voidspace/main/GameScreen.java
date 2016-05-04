package rbadia.voidspace.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.EnemyShip;


import rbadia.voidspace.model.Ship;

import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class GameScreen extends JPanel {
	private static final long serialVersionUID = 1L;

	private BufferedImage backBuffer;
	private Graphics2D g2d;

	private static final int NEW_SHIP_DELAY = 1000;
	private static final int NEW_ASTEROID_DELAY = 1000;
	private static final int SCORE_SHIP=500;
	private static final int SCORE_ASTEROID=100;
	private static final int FIRE_DELAY=500;

	private long lastShipTime;
	private long lastAsteroidTime;
	private long lastEnemyShip;
	private long lastBulletTime;

	private Rectangle asteroidExplosion;
	private Rectangle shipExplosion;
	private Rectangle enemyShipExplosion;

	private JLabel shipsValueLabel;
	private JLabel destroyedValueLabel;
	private JLabel scoreLabel;
	private JLabel scoreValueLabel;

	private Random rand;

	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;

	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;

	private ArrayList<Boolean> astDestroyed=new ArrayList<Boolean>();

	private ArrayList<Boolean> enemyShipDestroyed=new ArrayList<Boolean>();

	private Boolean newEnemyShip = true;

	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();

		initialize();

		// init graphics manager
		graphicsMan = new GraphicsManager();

		// init back buffer image
		backBuffer = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	private void initialize() {
		// set panel properties
		this.setSize(new Dimension(500, 400));
		this.setPreferredSize(new Dimension(500, 400));
		this.setBackground(Color.BLACK);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}

	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		Ship ship = gameLogic.getShip();
		List<Asteroid> asteroid = gameLogic.getAsteroid();
		List<Bullet> bullets = gameLogic.getBullets();
		List<EnemyShip> enemyShip=gameLogic.getEnemyShip();
		List<Bullet> enemyBullet= gameLogic.getEnemyBullet();

		// set orignal font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		// erase screen
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);

		// draw 50 random stars
		drawStars(15);

		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){
			drawGetReady();
			return;
		}

		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if((currentTime - lastShipTime) < NEW_SHIP_DELAY){
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
			return;
		}

		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			initialMessage();
			return;
		}

		//Asteroid variables to be manipulated 
		gameLogic.newAsteroid(this);
		gameLogic.newAsteroid(this);



	
		gameLogic.newAsteroid(this);
		gameLogic.newAsteroid(this);
		astDestroyed.add(false);
		astDestroyed.add(false);

		for(int i=0; i<asteroid.size()-1;i++)
		{
			Asteroid ast = asteroid.get(i);
			if(i==0)
			{
				if(astDestroyed.get(i))
				{
					long currentTime = System.currentTimeMillis();
					if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
						// draw a new asteroid
						lastAsteroidTime = currentTime;
						ast.setLocation(rand.nextInt(getWidth() - ast.width), (int) -ast.getHeight());
						astDestroyed.set(i, false);
					}
					else{
						// draw explosion
						graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
					}
				}
				else{if(ast.getY() + ast.getSpeed() < this.getHeight()){
					ast.translate(0, ast.getSpeed());
					graphicsMan.drawAsteroid(asteroid.get(i), g2d, this);
				}
				else{
					ast.setLocation(rand.nextInt(400 - 50 + 1) + 50, (int) -ast.getHeight());
				}
				}
			}
			
			if(i==1)
			{
				if(astDestroyed.get(i))
				{
					long currentTime = System.currentTimeMillis();
					if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
						// draw a new asteroid
						lastAsteroidTime = currentTime;
						ast.setLocation(rand.nextInt(getWidth() - ast.width), (int) -ast.getHeight());
						astDestroyed.set(i, false);
					}
					else{
						// draw explosion
						graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
					}
				}
				else{if(ast.getY() + ast.getSpeed() < this.getHeight()){
					ast.translate(2, ast.getSpeed());
					graphicsMan.drawAsteroid(asteroid.get(i), g2d, this);
				}
				else{
					ast.setLocation(rand.nextInt(400 - 50 + 1) + 50, (int) -ast.getHeight());
				}
				}
			}
			
		}

		if (newEnemyShip){
			gameLogic.newEnemyShip(this);
			enemyShipDestroyed.add(false);
			newEnemyShip = false;
		}

		//Draw enemy Ship
		for(int i=0;i<enemyShip.size();i++)
		{
			EnemyShip eShip=enemyShip.get(i);
			if(enemyShipDestroyed.get(i))
			{
				long currentTime = System.currentTimeMillis();
				if((currentTime - lastEnemyShip) > NEW_ASTEROID_DELAY){
					// draw a new asteroid
					lastEnemyShip = currentTime;
					enemyShipDestroyed.set(i, false);
				}
				else{
					// draw explosion
					graphicsMan.drawAsteroidExplosion(enemyShipExplosion, g2d, this);
				}
			}
			else{
				if(eShip.getY()+eShip.getEnemyShipSpeed()<this.getHeight())
				{
					eShip.translate(0, eShip.getEnemyShipSpeed());
					graphicsMan.drawEnemyShip(eShip, g2d, this);
				}
				else{
					eShip.setLocation(rand.nextInt(getWidth() - eShip.width), (int) -eShip.getHeight());
				}
			}
		}

		for(int i=0;i<enemyShip.size();i++)
		{
			int r=rand.nextInt(enemyShip.size());
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBulletTime) > FIRE_DELAY){
				if(enemyShip.get(r).getX()>0)
				{
					lastBulletTime = currentTime;
					gameLogic.fireEnemyBullet(enemyShip.get(r));
				}
			}
		}

		//draw enemy bullet
		for(int i=0;i<enemyBullet.size();i++){
			Bullet bullet = enemyBullet.get(i);
			graphicsMan.drawEnemyBullet(bullet, g2d, this);

			boolean remove = gameLogic.moveEnemyBullet(bullet);
			if(remove){
				enemyBullet.remove(i);
				i--;
			}
		}

		// draw bullets
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			graphicsMan.drawBullet(bullet, g2d, this);

			boolean remove = gameLogic.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}

		//check bullet-enemy ship collisions
		checkCollision(bullets,enemyShip,SCORE_SHIP);
		// check bullet-asteroid collisions
		checkCollision(bullets,asteroid,SCORE_ASTEROID);

		//check enemyBullet-ship collision
		ArrayList<Ship> ships = new ArrayList<Ship>();
		ships.add(ship);




		// draw ship
		if(!status.isNewShip()){
			// draw it in its current location
			if(InputHandler.shiftIsPressed){
				graphicsMan.drawShipThruster(ship, g2d, this);
				//	soundMan.playThrusterSound();
			}
			else{
				//soundMan.stopThrusterSound();
				graphicsMan.drawShip(ship, g2d, this);
			}
		}
		else{
			// draw a new one
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastShipTime) > NEW_SHIP_DELAY){
				lastShipTime = currentTime;
				status.setNewShip(false);
				ship = gameLogic.newShip(this);
			}
			else{
				// draw explosion
				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			}
		}

		
		// check bullet-asteroid collisions
/*		for(int i=0; i<asteroid.size(); i++){
			Asteroid ast = asteroid.get(i);
			
			for(int j= 0; j< bullets.size(); j++){
				Bullet bullet = bullets.get(j);
				if(ast.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);

				// "remove" asteroid
				asteroidExplosion = new Rectangle(
						ast.x,
						ast.y,
						ast.width,
						ast.height);
				ast.setLocation(-ast.width, -ast.height);
				status.setNewAsteroid(true);
				lastAsteroidTime = System.currentTimeMillis();

				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();

				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	}*/
		
		
		// check ship-asteroid collisions

		for (int i=0; i<asteroid.size();i++){
			Asteroid ast = asteroid.get(i);
			if(ast.intersects(ship)){
				// decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);

				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);

				// "remove" asteroid
				asteroidExplosion = new Rectangle(
						ast.x,
						ast.y,
						ast.width,
						ast.height);
				ast.setLocation(-ast.width, -ast.height);
				status.setNewAsteroid(true);
				lastAsteroidTime = System.currentTimeMillis();
				astDestroyed.set(i,true);
				// "remove" ship
				shipExplosion = new Rectangle(
						ship.x,
						ship.y,
						ship.width,
						ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);
				lastShipTime = System.currentTimeMillis();

				// play ship explosion sound
				soundMan.playShipExplosionSound();
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
			}

		}

		// check ship-enemyShip collisions

		for (int i=0; i<enemyShip.size();i++){
			EnemyShip ast = enemyShip.get(i);
			if(ast.intersects(ship)){
				// decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);



				// "remove" asteroid
				asteroidExplosion = new Rectangle(
						ast.x,
						ast.y,
						ast.width,
						ast.height);
				ast.setLocation(-ast.width, -ast.height);

				lastEnemyShip = System.currentTimeMillis();
				enemyShipDestroyed.set(i, true);

				// "remove" ship
				shipExplosion = new Rectangle(
						ship.x,
						ship.y,
						ship.width,
						ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);
				lastShipTime = System.currentTimeMillis();

				// play ship explosion sound
				soundMan.playShipExplosionSound();
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
			}

		}
		
		for (int i=0; i<enemyBullet.size();i++){
			Bullet ast = enemyBullet.get(i);
			if(ast.intersects(ship)){
				// decrease number of ships left
				status.setShipsLeft(status.getShipsLeft() - 1);

				

				// "remove" asteroid
				asteroidExplosion = new Rectangle(
						ast.x,
						ast.y,
						ast.width,
						ast.height);
				ast.setLocation(-ast.width, -ast.height);
				
				
				enemyShipDestroyed.set(i, true);
				
				// "remove" ship
				shipExplosion = new Rectangle(
						ship.x,
						ship.y,
						ship.width,
						ship.height);
				ship.setLocation(this.getWidth() + ship.width, -ship.height);
				status.setNewShip(true);
				lastShipTime = System.currentTimeMillis();

				// play ship explosion sound
				soundMan.playShipExplosionSound();
				// play asteroid explosion sound
				soundMan.playAsteroidExplosionSound();
			}
		}

		// update asteroids destroyed label
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));

		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));

		//update score label
		scoreValueLabel.setText(Integer.toString(status.getScore()));
	}

	/**
	 * Draws the "Game Over" message.
	 */
	private void drawGameOver() {
		String gameOverStr = "GAME OVER";
		soundMan.stopGameSong();
		soundMan.gameOverSound();
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.WHITE);
		g2d.drawString(gameOverStr, strX, strY);
	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */
	private void drawGetReady() {
		String readyStr = "Get Ready!";
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	private void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	private int titleX = -300;
	private void initialMessage() {
		soundMan.playGameSong();
		String gameTitleStr = "Void Space";
		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (3*this.getHeight() - 2*ascent)/10;
		if(titleX != 10){
			titleX += 2;
			graphicsMan.drawGameTitle(new Rectangle(titleX,strY,1,1), g2d, this);
		} else {
			graphicsMan.drawGameTitle(new Rectangle(titleX,strY,1,1), g2d, this);
		}
		
		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);
		
		fm = g2d.getFontMetrics();
		String viewScoresString = "Press <H> to View High Scores.";
		strWidth = fm.stringWidth(viewScoresString);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(viewScoresString, strX, strY);
	}

	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
	}

	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param destroyedValueLabel the label to set
	 */
	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}

	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}

	/**
	 * Sets the label that displays the score
	 * @param scoreValueLabel
	 */
	public void setscoreValueLabel(JLabel scoreValueLabel) {
		this.scoreValueLabel = scoreValueLabel;
	}

	public void checkCollision(List<Bullet> aBullet,List<?> aList, int score)
	{
		List<Rectangle> anEnemy = (List<Rectangle>)aList;
		for(int i=0;i<aBullet.size();i++)
		{
			Bullet bullet = aBullet.get(i);
			for(int j=0;j<anEnemy.size();j++)
			{
				if(anEnemy.get(j).intersects(bullet)){
					// increase enemies destroyed count


					// "remove" enemy
					if(anEnemy.get(j) instanceof EnemyShip)
					{
						status.addScore(score);
						enemyShipExplosion = new Rectangle(
								anEnemy.get(j).x,
								anEnemy.get(j).y,
								anEnemy.get(j).width,
								anEnemy.get(j).height);
						anEnemy.get(j).setLocation(-anEnemy.get(j).width, -anEnemy.get(j).height);
						lastEnemyShip= System.currentTimeMillis();

						// play asteroid explosion sound
						soundMan.playShipExplosionSound();

						enemyShipDestroyed.set(j, true);
						// remove bullet
						aBullet.remove(i);
						break;
					}
					else if (anEnemy.get(j) instanceof Asteroid)
					{
						status.addScore(score);
						asteroidExplosion = new Rectangle(
								anEnemy.get(j).x,
								anEnemy.get(j).y,
								anEnemy.get(j).width,
								anEnemy.get(j).height);
						anEnemy.get(j).setLocation(-anEnemy.get(j).width, -anEnemy.get(j).height);
						lastAsteroidTime= System.currentTimeMillis();
						// play asteroid explosion sound
						soundMan.playShipExplosionSound();
						status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 1);
					
						astDestroyed.set(j,true);
						// remove bullet
						aBullet.remove(i);
						break;
					}
				}
			}
		}	
	}


}
