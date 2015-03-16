package trl.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import trl.gamestate.GameStateManager;
import trl.gamestate.GameplayState;
import trl.graphics.ImageLoader;
import trl.graphics.ImageManager;
import trl.graphics.SpriteSheet;
import trl.map.Map;

public class Game extends Canvas implements Runnable, MouseListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ROWS = 40, COLUMNS = 40;  //Map rows and columns
    public static final int W_ROWS = 11, W_COLS =11;     //Displayed nodes
    public static int SCALE = 2;
    public static final int TILE_SIZE = 30;
    public static final int SCALED_TILE_SIZE = TILE_SIZE * SCALE;
    public static final int W_WIDTH = W_COLS * TILE_SIZE * SCALE;
    public static final int W_HEIGHT = W_ROWS * TILE_SIZE * SCALE;
    public static final int MSG_HEIGHT = 40 * SCALE;
    public static double fps;
    private static double maxUpdateTime;
    
	private static final int NO_DELAYS_PER_YIELD = 16;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final int TARGET_FPS = 30;
    public static boolean running = false;
    final double ticksPerSecond = 15d;
    public static final int TURN_DELAY = 5;
    public static int tickTimer;
    public static Thread gameThread;
    private BufferedImage spriteSheet;
    private static ImageManager im;
    public static int turnCounter;
    private static GameStateManager gsm;
    
    public void init() {
        ImageLoader loader = new ImageLoader();
        spriteSheet = loader.load("/tiled_roguelike_sheet.png");
        SpriteSheet ss = new SpriteSheet(spriteSheet);
        im = new ImageManager(ss);
        gsm = new GameStateManager();
        gsm.setGameState(GameStateManager.MENU_STATE);
        this.addKeyListener(new KeyManager(gsm));
		this.addMouseListener(this);
        tickTimer = 0;
    }
    
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public synchronized void stop() {
        if(!running) {
            return;
        }
        running = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
	public void run() {
		init();
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		
		beforeTime = System.nanoTime();
		running = true;
		int period =  (1000 / TARGET_FPS) * 1000000;
		long elapsed = 0;
		long lastElapsed = 0;
//		long maxTime = 0;
		
		while (running) {
			gameUpdate();
			gameRender();
//			paintScreen();
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;
			
			if (sleepTime > 0) {
//				System.out.println("Sleep time > 0");
				try {
					Thread.sleep(sleepTime / 1000000L);
				}
				catch(InterruptedException e) {}
			
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}
			else {
//				System.out.println("No sleep time.");
				excess -= sleepTime;
				overSleepTime = 0L;
				
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
//					System.out.println("Forced yield thread.");
					Thread.yield();
					noDelays = 0;
				}
			}

			int skips = 0;
			while (excess > period && skips < MAX_FRAME_SKIPS) {
//				System.out.println("Skipping frame. " + skips);
				excess -= period;
				gameUpdate();
				skips++;
			}
			
			elapsed = System.nanoTime() - beforeTime;
			if (elapsed / 1000000 > maxUpdateTime) {
				maxUpdateTime = elapsed / 1000000;
			}

			fps = (1 / ((.5d * elapsed + .5d * lastElapsed) / 1000000)) * 1000;
			lastElapsed = elapsed;
			beforeTime = System.nanoTime();
		}
	}
    
    public void gameUpdate() {   
        if (tickTimer > 0) {
            tickTimer--;
        }     
        gsm.tick();
    }
    
    public void gameRender() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);

        gsm.render(g);
        g.drawString(Double.toString(fps), 0, 16);
        g.drawString(Double.toString(maxUpdateTime), 0, 32);
        //END RENDER
        g.dispose();
        bs.show();
    }
    
    public static void main(String[] args) {
        Game game = new Game();        
        game.setPreferredSize(new Dimension(W_WIDTH, W_HEIGHT));
        game.setMaximumSize(new Dimension(W_WIDTH, W_HEIGHT));
        game.setMinimumSize(new Dimension(W_WIDTH, W_HEIGHT));        
        JFrame frame = new JFrame("Tiled Roguelike 1.3");
        frame.setSize(W_WIDTH, (W_HEIGHT + ROWS) + (MSG_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
        game.setFocusable(true);
        game.requestFocus();

        game.start();
    }
    
    public static ImageManager getImageManager() {
    	return im;
    }
     
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {    	
    }
    
    public void mouseClicked(MouseEvent e) {
    	if (gsm.getGameState() == 1) {
	    	int x = e.getX() / SCALED_TILE_SIZE;
	    	int y = -((e.getY() / SCALED_TILE_SIZE) - W_HEIGHT / SCALED_TILE_SIZE + 1);
	    	int relX = Map.displayedNodesMinX + x;
	    	int relY = Map.displayedNodesMinY + y;
		    if (GameplayState.getMap().getNode(relX, relY) != null) {	
	    		if (GameplayState.getMap().getNode(relX, relY).getFeature().isPassable() || GameplayState.getMap().getNode(relX, relY).getFeature() instanceof trl.map.feature.DoorClosed) {
		    		if (GameplayState.getPlayer().getLoc().equals(GameplayState.getMap().getNode(relX, relY))) {
		    			GameplayState.getPlayer().wait = true;
		    		}
		    		else {
			    		GameplayState.getPlayer().setPath(GameplayState.getMap().findPath(GameplayState.getPlayer().getLoc(), GameplayState.getMap().getNode(relX, relY)));
		    		}
		    	}  
		    }
    	}
    }
    
    public void mouseExited(MouseEvent e) {  	
    }
    
    public void mouseReleased(MouseEvent e) {   	
    }
    
    public static GameStateManager getGameStateManager() {
    	return gsm;
    }
}


