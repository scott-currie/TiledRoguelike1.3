package trl.gamestate;

import java.awt.Color;
import java.awt.Graphics;

import trl.entity.Entity;
import trl.entity.actor.ActorQueue;
import trl.entity.enemy.EnemyGroup;
import trl.entity.player.Barbarian;
import trl.entity.player.Player;
import trl.entity.player.Ranger;
import trl.entity.player.Thief;
import trl.entity.player.Wizard;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;

public class GameplayState extends GameState{
//	public static final int ROWS = 40, COLUMNS = 40;  //Map rows and columns
//    public static final int W_ROWS = 11, W_COLS =11;     //Displayed nodes
//    public static int SCALE = 2;
//    public static final int TILE_SIZE = 30;
//    public static final int SCALED_TILE_SIZE = TILE_SIZE * SCALE;
//    public static final int Game.W_WIDTH = W_COLS * TILE_SIZE * SCALE;
//    public static final int Game.W_HEIGHT = W_ROWS * TILE_SIZE * SCALE;
//    public static final int MSG_HEIGHT = 40 * SCALE;

    public static final int TURN_DELAY = 10;
    public static int tickTimer;
    private static EnemyGroup eg;
    private static ActorQueue aq;
    private static Player player;
    public static Map map;
    private int classChoice;
    public static int turnCounter;
	public static int dungeonLevel;
	public static int addEnemyInterval = 70; 	//Number of turns on each level after which an enemy is added.
	public static boolean tickEnemies;
	
	public GameplayState(int classChoice) {
		this.classChoice = classChoice;
		dungeonLevel = 1;
		init();
	}
	
	public void init() {
		//If we're building the state from scratch
		if (dungeonLevel == 1) {
		    map = new Map();
		    eg = new EnemyGroup(map);
		    aq = new ActorQueue();
		    switch (classChoice) {
	        	case 0: {
		        	player = new Barbarian(map);
		        	break;
		        }
		        case 1: {
		        	player = new Thief(map);
		        	break;
		        }
		        case 2: {
		        	player = new Wizard(map);
		        	break;
		        }
		        case 3: {
		        	player = new Ranger(map);
		        	break;
		        }
		    } 
		    aq.addActor(player);
		    eg.spawnEnemies();
		}
		
		else if (dungeonLevel == 5) {
			map.init();
//			map = new Map();
			eg.flush();
		    eg = new EnemyGroup(map);
		    aq = new ActorQueue();
		    player.refresh();
		    aq.addActor(player);
		    eg.spawnEnemies();
		    /*For now, we will randomly place the player in the newly created
		     * map. Later, we will add logic to ensure that the player enters
		     * the new map at the same coordinates he left the previous level
		     * from. This will involve modifying room generation to make sure
		     * that a room is generated around the location of the down stairway
		     * such that it doesn't end up in a wall or void space. 
		     */
		    player.initActor();
		    player.refresh();
		    if (player instanceof trl.entity.player.Thief) {
		    	map.revealAll();
		    }
		    player.endTurn(aq.getActor(0));
		}
		
		else {
//			map = new Map();
			System.out.println("Refresh dungeon level " + dungeonLevel + ".");
			eg.flush();
			eg = null;
			aq.flush();
			aq = null;
			map.init();
		    eg = new EnemyGroup(map);
		    aq = new ActorQueue();
		    //Set turns on level to zero before spawning enemies.
//		    player.setTurnsOnLevel(0);
		    aq.addActor(player);
		    eg.spawnEnemies();
		    /*For now, we will randomly place the player in the newly created
		     * map. Later, we will add logic to ensure that the player enters
		     * the new map at the same coordinates he left the previous level
		     * from. This will involve modifying room generation to make sure
		     * that a room is generated around the location of the down stairway
		     * such that it doesn't end up in a wall or void space. 
		     */
		    player.initActor();
		    player.refresh();
		    if (player instanceof trl.entity.player.Thief) {
		    	map.revealAll();
		    }
		    player.endTurn(aq.getActor(0));
		}
		map.updateDisplayedNodes();
		map.updateVisibleToPlayer();
		map.updateImageMap();
		tickTimer = 0;
		System.out.println("Player turns on level = " + player.getTurnsOnLevel());
    	int maxEnemies = dungeonLevel + (GameplayState.getPlayer().getTurnsOnLevel() / GameplayState.addEnemyInterval);
		System.out.println("Max enemies: " + dungeonLevel + " + (" + player.getTurnsOnLevel() + " / " + addEnemyInterval + ")");
//		System.out.println("Enemy group size = " + eg.getEnemies().size());
//		System.out.println("Actor queue size = ");
		eg.listEnemies();
		
	}	
	
	public void tick() {
		if (tickEnemies) {
			tickEnemies = false;
		}
		else {
			tickEnemies = true;
		}
		if (!player.isAlive()) {
			Game.getGameStateManager().addGameState(2, new LoseGameState(player.getEnemiesDefeated()));
			Game.getGameStateManager().setGameState(2);
		}
		aq.tick();
	}
	
	public void render(Graphics g) {
		map.render(g);
		player.render(g);
		eg.render(g);
		drawStatus(g);
		drawMiniMap(g);
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public static ActorQueue getActorQueue() {
		return aq;
	}
	
	public void drawStatus(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, Game.W_HEIGHT + Game.SCALE, Game.W_WIDTH, Game.MSG_HEIGHT);
        g.setColor(Color.yellow);
        g.drawString("Level:" + player.getLevel(), 0, Game.W_HEIGHT + g.getFont().getSize());
        g.drawString("Player HP: " + player.getHP() , 0, Game.W_HEIGHT + 2 * g.getFont().getSize() + 2);
        g.drawString("Dungeon Level: " + dungeonLevel, 0, Game.W_HEIGHT + 3 * g.getFont().getSize() + 2);
        g.drawString("Player XP: " + player.getXPEarned(), 0, Game.W_HEIGHT + 4 * g.getFont().getSize() + 2);
        g.drawString("Enemies: " + player.getEnemiesDefeated() , 0, Game.W_HEIGHT + 5 * g.getFont().getSize() + 2);

        if (player instanceof trl.entity.player.Barbarian) {
    		if (player.getTimers()[0] <= 0) {
    			g.setColor(Color.green);
            	g.drawString("(s) Shout", 200, Game.W_HEIGHT + g.getFont().getSize());    			
    		}
    		else {
    			g.setColor(Color.red);
    			g.drawString("(s) Shout (" + player.getTimers()[0] + ")", 200, Game.W_HEIGHT + g.getFont().getSize());
    		}
        }
        if (player instanceof trl.entity.player.Ranger) {
//    		if (player.getTimers()[0] <= 0) {
    			g.setColor(Color.green);
            	g.drawString("(f) Fire Arrow", 200, Game.W_HEIGHT + g.getFont().getSize());    			
//    		}
//    		else {
//    			g.setColor(Color.red);
//    			g.drawString("(f) Fire Arrow (" + player.getTimers()[0] + ")", 200, Game.W_HEIGHT + 16);
//    		}
        }
        if (player instanceof trl.entity.player.Wizard) {
    		if (player.getTimers()[0] <= 0) {
    			g.setColor(Color.green);
            	g.drawString("(b) Blink", 200, Game.W_HEIGHT + g.getFont().getSize());
    		}
    		else {
    			g.setColor(Color.red);       	
    			g.drawString("(b) Blink (" + player.getTimers()[0] + ")", 200, Game.W_HEIGHT + g.getFont().getSize());
    		}
        	
    		if (player.getTimers()[1] <= 0) {
    			g.setColor(Color.green); 
    			g.drawString("(e) Explode", 200, Game.W_HEIGHT + 2 * g.getFont().getSize() + 2);   		
    		}
    		else {
    			g.setColor(Color.red);
    			g.drawString("(e) Explode (" + player.getTimers()[1] + ")", 200, Game.W_HEIGHT + 2 * g.getFont().getSize() + 2);
    		}
    		
    		if (player.getTimers()[2] <= 0) {
    			if (player.getHP() > 4) {
    				g.setColor(Color.green);
    			}
    			else {
    				g.setColor(Color.red);
    			}
    			g.drawString("(q) Quicken", 200, Game.W_HEIGHT + 3 * g.getFont().getSize() + 2);
    		}
    		else {
    			g.setColor(Color.red);
    			g.drawString("(q) Quicken (" + player.getTimers()[2] + ")", 200, Game.W_HEIGHT + 3 * g.getFont().getSize() + 2);
    		}
        }
        g.setColor(Color.green);
        g.drawString("(c) Close Door", 300, Game.W_HEIGHT + g.getFont().getSize());
        
        if (player.getHasKey() && !player.getOpenedLock()) {
        	g.drawImage(Game.getImageManager().key, 400, Game.W_HEIGHT, Game.TILE_SIZE, Game.TILE_SIZE, null);
        }
        if (player.getOpenedLock()) {
        	g.drawImage(Game.getImageManager().lockOpen, 400, Game.W_HEIGHT, Game.TILE_SIZE, Game.TILE_SIZE, null);
        }
    
    }
    
    public void drawMiniMap(Graphics g) {
        Color trBlack = new Color (0, 0, 0, 64);
        Color trGray = new Color(128, 128, 128, 196);
        Color trWhite = new Color(255, 255, 255, 196);
        Color brown = new Color(128, 69, 19);
    	int scale = 3;
        int startX = Game.W_WIDTH - (Game.COLUMNS * scale) - Game.W_ROWS;
        
        for (int x = 0; x < Game.COLUMNS; x++) {
            for (int y = 0; y < Game.ROWS; y++) {
            	//Default color. Carries through if no other condition matches
            	g.setColor(trBlack);
            	
            	//Set borders white
            	if (x == 0 || y == 0 || x == Game.COLUMNS - 1 || y == Game.ROWS - 1) {
            		g.setColor(trWhite);            		
            	}
            	Node node = map.getNode(x, y); 
            	if (node != null) {
	            	if (node.seenByPlayer()) {
	            		if (node.getFeature().isPassable()) {
	            			g.setColor(trGray);	 
	            		}
	            		if (node.isStairDown()) {
            				g.setColor(Color.PINK);
	            		}	
	            		if (node.isClosedDoor()) {
	            			g.setColor(brown);
	            		}
	            		if (node.getEntities() != null && node.getEntities().size() > 0) { 
		                	for (Entity entity : node.getEntities()) {
		                        if (entity.seenByPlayer()) {                  	
			                    	if (entity instanceof trl.entity.item.Potion) {
			                            g.setColor(Color.GREEN);
			                        }
			                        if (entity instanceof trl.entity.player.Player) {
			                        	g.setColor(Color.WHITE);
			                        }
			                        if (entity instanceof trl.entity.enemy.Enemy) {
			                        	g.setColor(Color.RED);
			                        }
			                        if (entity instanceof trl.entity.item.Hammer) {
			                        	g.setColor(Color.GREEN);
			                        }
			                        if (entity instanceof trl.entity.item.Key) {
			                        	g.setColor(Color.YELLOW);
			                        }
			                        if (entity instanceof trl.entity.item.Lock) {
			                        	g.setColor(Color.YELLOW);
			                        }
		                        }
		                    }
		                }	            		
	                }
            	}            	
//            	if (g.getColor() == null) {
//            		System.out.println("Color NULL");
//            		g.setColor(trBlack);
//            	}
                g.fillRect(startX + (x * scale), (Game.ROWS * scale) - y * scale, scale, scale);
            }
        }
    }
    
    public static EnemyGroup getEnemyGroup() {
    	return eg;
    }
    
    public static Map getMap() {
    	return map;
    }
    
//    public static ImageManager getImageManager() {
//    	return im;
//    }
    
    public int getDungeonLevel() {
    	return dungeonLevel;
    }
    
    public void setDungeonLevel(int dungeonLevel) {
    	this.dungeonLevel = dungeonLevel;
    }
}
