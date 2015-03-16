package trl.entity.actor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trl.entity.Entity;
import trl.entity.enemy.Enemy;
import trl.gamestate.GameplayState;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;
import trl.map.Room;
import trl.sound.SoundManager;

public abstract class Actor extends Entity{
    protected int hp, maxHP, atk;
    protected int damageTaken;
    protected int damageDealt;
    protected SoundManager sm;
    protected boolean acted;       //Did I do anything during my tick?
    protected boolean attacked;     //Did I attack this tick?
    protected boolean moved;        //Did I move this tick?
    protected boolean myTurn;      //Is it my turn to act (and nobody else's)?
    protected int[] timers;
    protected boolean[] stance;		//normal, attacking, hit, shooting
    protected List<Node> path;
    protected int initialPathSize;
    protected int level;
    protected Node previousNode;
    protected int turnsOnNode;
    
    public Actor(Map map) {
        super (map);
        initActor();
    }
    
    public void initActor() {
        this.loc = map.placeEntity(this, map.getRandomNodeInRoom());
        this.path = new ArrayList<Node>();
        stance = new boolean[]{true, false, false, false};
    }

    public int getHP() {
        return hp;
    }
    
    public void setHP(int hp) {
        this.hp = hp;
    }
    
    public boolean isAlive() {
        return hp > 0;
    }    
    
    public int getDamageTaken() {
        return damageTaken;
    }
    
    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }   

    protected void attack(Actor defender) {
        Random r = new Random();
        damageDealt = (int)Math.round(r.nextDouble() * atk) + level ;
        if (damageDealt > atk) {
        	damageDealt = atk;
        }
        defender.setHP(defender.getHP() - damageDealt);
        defender.setDamageTaken(damageDealt);
        this.setStance(false, true, false, false);
        if (damageDealt > 0) {
        	defender.setStance(false, false, true, false);
        }
    }
    
        
    public void setImage(BufferedImage image) {
        this.image = image;
    } 
    
    public void endTurn(Actor nextActor) {
//        System.out.print("Turn = " + Game.turnCounter);
    	myTurn = false;
        nextActor.setTurn(true);
        //Set turn delay if actor is visible.
        //tickTimer = TURN_DELAY if there are any enemies currently visible
        //to the player
        //If player is ending turn
        if (this instanceof trl.entity.player.Player) {
        	//Are there any currently visible enemies?
	        for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
//	        	if (enemy.isVisibleToPlayer()) {
	        	if (enemy.getVisibleToPlayer()) {
	        		Game.tickTimer = Game.TURN_DELAY;
	        		break;
	        	}
	        }
	        //Am I a wizard who just exploded (even though I am alone)
	        if (this instanceof trl.entity.player.Wizard && getTimers()[1] == 10) {
//	    		System.out.print(this.toString() + " Exploded wizard. ");
	    		Game.tickTimer = Game.TURN_DELAY;
	    	}
        }
        //If enemy ending its turn
        if (this instanceof trl.entity.enemy.Enemy) {
        	//Am I currently visible to the player?
//        	if (map.getVisibleToPlayer().contains(loc)) {
//        		Game.tickTimer = Game.TURN_DELAY;
//        	}
        	if (this.getVisibleToPlayer()) {
        		Game.tickTimer = Game.TURN_DELAY;
        	}
        }
    }
    
    public void setTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }
    
    public boolean getTurn() {
        return myTurn;
    }
    
    public void move(Node node) {
    	//Remove the actor from current node, set loc to argument node,
    	//add self to current loc, remove argument node from path.

    	if (node.getFeature().isPassable() && !node.hasEnemy()) {
            loc.removeEntity(this);
            loc = node;
            loc.addEntity(this);
            damageDealt = 0;
            path.remove(node);
    	}
    }
            
//    protected boolean actedThisTick() {
//        return acted;
//    }
//    
    protected void clearFlags() {
        acted = false;
        moved = false;
        attacked = false;
    }
    
    protected void playSound() {
        if (attacked && damageDealt > 0) {
            sm.playSound("strike");
        }
        if (attacked && damageDealt == 0) {
            sm.playSound("miss");
        }
    }
    
    public boolean getActed() {
    	if (moved || attacked || acted) {
    		return true;
    	}
    	return false;
    }
    
    public int[] getTimers() {
    	return timers;
    }
    
    public void decrementTimers() {
    	for (int i = 0; i < timers.length; i++) {
    		if (timers[i] > 0) {
    			timers[i]--;
    		}
    	}
    }
    
    public Node getNextPathNode() {
    	/* Next path node will always be index 0. Nodes are removed from path
    	 * list after any action is taken.
    	 */
    		return path.get(0);
    }
    
    public void setPath(List<Node> list) {
    	this.path = list;
    	initialPathSize = this.path.size();
    }
    
    public List<Node> getPath() {
    	return path;
    }
    
    public void setPathTo(Node node) {
    	path = map.findPath(this.loc, node);
    	initialPathSize = path.size();
    }
    
    public void setPathToConnectedRoom() {
    	Room current = null;
    	Room nextRoom = null;
    	
    	//If the player is currently in a room.
    	if (map.inRoom(loc)) {
    		current = getOccupiedRoom(loc);
    	}
    	//Else player is in a hallway. Get room in occupied column/row.
    	else {
    		current = map.getNearestRoom(loc);
    	}
    	
    	//If room only has one connection, only option is that room
    	if (current.getConnectedTo().size() == 1) {
    		nextRoom = current.getConnectedTo().get(0);
    	}
    	else {
    		nextRoom = current.getRandomConnectedRoom();
    	}

//    	System.out.println("setPathToConnectedRoom: nextRoom = " + nextRoom.toString());
    	Node destination = nextRoom.getRandomNodeInRoom();
//    	System.out.println("nextRoom col=" + nextRoom.getColumn() + ", row=" + nextRoom.getRow());
//    	System.out.println("nextRoom minX=" + nextRoom.getBoundary().getMinX() + ",maxX=" + nextRoom.getBoundary().getMaxX());
//    	System.out.println("nextRoom minY=" + nextRoom.getBoundary().getMinY() + ",maxY=" + nextRoom.getBoundary().getMaxY());
//    	System.out.println("setPathToConnectedRoom: destination = " + destination.toString() + ", x = " + destination.getX() + "," + destination.getY() + ", feature = " + destination.getFeature().toString());
    	path = map.findPath(loc, destination);
    }
    
    public void render(Graphics g) {    	
    	if (this.inDisplayedNodes()) {
    		//Row and column in displayed nodes    		
    		int wx = map.getDisplayedX(loc);
    		int wy = map.getDisplayedY(loc);
//    		System.out.println("wx = " + wx + ", wy = " + wy);

    		//Pixel x and y in game window
	        int px = wx *  Game.SCALED_TILE_SIZE;
	        int py = Game.W_HEIGHT  - (wy * Game.SCALED_TILE_SIZE) - Game.SCALED_TILE_SIZE;	    		        
//	        System.out.println("px = " + px + ", py = " + py);
	        
	        //Height and width in pixels
	        int width = Game.SCALED_TILE_SIZE;
	        int height = Game.SCALED_TILE_SIZE;
            
	        if (hp > 0) {
	        	if (this.getVisibleToPlayer()) {
		        	g.drawImage(image, px, py, width, height, null);            
	                renderHealthBar(g, px, py, width, height);     
                }
                //Draw damage indicator
                if (myTurn && Game.tickTimer > 0 && getDamageTaken() > 0) {
                    g.drawImage(Game.getImageManager().bang, px, py, width, height, null);
                    g.setColor(Color.red);
                    g.drawString(Integer.toString(getDamageTaken()), px + width / 2, py + height / 2);                                              
                }

                if (this instanceof trl.entity.player.Wizard && timers[1] == 10) {
                	renderExplosion(g, px, py);
                }
                
                //Draw target box
                if (this instanceof trl.entity.enemy.Enemy) {
                	if (((Enemy)this).getTargeted()) {
                		g.setColor(Color.YELLOW);
                		g.drawRect(px, py, width, height);
                		g.drawRect(px + 1, py + 1, width - 2, height - 2);
                	}
                }
                
                //If attacking
                if (stance[1]) {
                    g.drawImage(Game.getImageManager().swords, px, py, width, height, null);	
                }
                
                //If shooting
                if (stance[3]) {
                    g.drawImage(Game.getImageManager().arrows, px, py, width, height, null);	
                }
            }
            else {
                g.drawImage(Game.getImageManager().corpse, px, py, width, height, null);
            }          
        }
    }

    public void renderHealthBar(Graphics g, int x, int y, int width, int height) {
	    //Draw bar background
        Color trBlack = new Color (0, 0, 0, 64);
        Color trGray = new Color(128, 128, 128, 64);
        Color trGreen = new Color(0, 255, 0, 196);
        Color trYellow = new Color(255, 255, 0, 196);
        Color trRed = new Color(255, 0, 0, 196);
    	
        g.setColor(trGray);
	    g.fillRect(x + 1, y + (int)(width * .75) - 1, width - 2, 6);
	    //Draw border
	    g.setColor(trBlack);
	    g.drawRect(x + 1, y + (int)(height * .75) - 1, width - 2, 6);                        
	    
	    //Calculate width of bar
	    double percentHealth = (double)hp / (double)maxHP;
	    if (percentHealth > .75) { 
	    	g.setColor(trGreen);
		}
	    if (percentHealth > .25 && percentHealth <= .75) {
	    	g.setColor(trYellow);
	    }
	    if (percentHealth <= .25) {
//	        g.setColor (Color.red);
	    	g.setColor(trRed);
	    }
	    //Draw bar
	    g.fillRect(x + 2, y + (int)(height * .75) , (int)(percentHealth * (width - 4)), 4);                                               
    } 
    
    public void renderExplosion(Graphics g, int x, int y) {
    	int r = 0; //blast radius
    	if (this instanceof trl.entity.player.Wizard && this.timers[1] > 0 && Game.tickTimer > 0) {
    		//Determine blast radius at time = timers[1]
//    		System.out.println("Rendering explosion. Tick timer = " + Game.tickTimer);
    		if (Game.tickTimer == Game.TURN_DELAY) {
    			r = 0;   		
    		}

    		else if (Game.tickTimer >= Game.TURN_DELAY / 2) {
    			r = 1;
    		}
    		
    		else if (Game.tickTimer > 0) {
    			r = 2;
    		}
    		else {
    			return;
    		}
    		List<Node> blastArea = map.getAoENodes(loc, r);	
			for (Node node : blastArea) {
				g.drawImage(Game.getImageManager().fire, map.getDisplayedX(node) * Game.SCALED_TILE_SIZE, (Game.W_HEIGHT - Game.SCALED_TILE_SIZE) - (map.getDisplayedY(node) * Game.SCALED_TILE_SIZE), Game.SCALED_TILE_SIZE, Game.SCALED_TILE_SIZE, null);
			}
    	}
    }
    
    public void setStance(boolean normal, boolean attacking, boolean hit, boolean shooting) {
    	stance[0] = normal;
    	stance[1] = attacking;
    	stance[2] = hit;
    	stance[3] = shooting;
    }

    public void closeDoor(Node node) {
    	//If node has open door and no entities occupying node.
    	if (node.getFeature() instanceof trl.map.feature.DoorOpen && (node.getEntities() == null || node.getEntities().size() == 0)) {
    		node.makeClosedDoor();
    	}
    	if (this instanceof trl.entity.player.Player) {
    		GameplayState.getPlayer().close = false;
    		path.remove(node);
    	}
    }
    
    public void openDoor(Node node) {
    	if (node.getFeature() instanceof trl.map.feature.DoorClosed) {
    		node.makeOpenDoor();
    	}
    	if (this instanceof trl.entity.player.Player) {
    		path.remove(node);
    	}
    }
    
    public abstract void tick();
  
    public int getLevel() {
    	return level;
    }
    
    public void setActed(boolean acted) {
    	this.acted = acted;
    }
}
