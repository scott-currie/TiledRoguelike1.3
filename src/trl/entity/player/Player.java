package trl.entity.player;

import java.util.List;
import java.util.Random;

import trl.entity.Entity;
import trl.entity.actor.Actor;
import trl.entity.enemy.Enemy;
import trl.entity.item.Goal;
import trl.entity.item.Hammer;
import trl.entity.item.Lock;
import trl.entity.item.Potion;
import trl.gamestate.GameplayState;
import trl.gamestate.WinGameState;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;
import trl.map.feature.StairDown;

public abstract class Player extends Actor {
	protected int enemiesDefeated;
	protected boolean hasKey = false;
	protected boolean newEnemies = false; 	//if new enemy became visible, set true
	protected boolean openedLock = false;
	protected Enemy target;
	protected List<Enemy> targets;
	protected int turnsOnLevel;
	protected int turnsSinceCombat;
	public boolean up = false, dn = false, lt = false, rt = false, ur = false,
			dr = false, dl = false, ul = false, wait = false, shout = false,
			blink = false, explode = false, close = false, quicken = false,
			targetEnemy = false, fireArrow = false, gotTargets = false,
			nextTarget = false, previousTarget = false, getTargets = false,
			cancel = false;
	protected double xpEarned;
	public Player(Map map) {
		super(map);
		level = 1;
	}

	public boolean adjacentEnemy() {
		for (int x = -1; x <= 1; x++) {
			for (int y = 1; y >= 0; y--) {
				if (x >= 0 && x < Game.COLUMNS && y >= 0 && y < Game.ROWS) {					
					Node adjacent = map.getNode(getX() + x, getY() + y);
					if (adjacent != null) {
						if (adjacent.checkEntityByID((byte) 0)) {
							System.out.println("Adjacent enemy. Can't shoot.");
							return true;
						}
					}
				}
			}
		}
		System.out.println("No adjacent enemies found.");
		return false;
	}
	
	public void alertEnemies() {
		for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
			if (map.isVisibleToPlayer(enemy.getLoc())) {
				enemy.setAwareOfPlayer(true);
			}
		}
	}

	public void clearAllTargetingFlags() {
		targetEnemy = false;
		fireArrow = false;
		gotTargets = false;
		nextTarget = false;
		previousTarget = false;
		getTargets = false;
//		if (targets != null) {	//Will be null if player hasn't targeted anything before
			targets = null;
//		}
		if (target != null) {
			target.setTargeted(false);
		}
		target = null;
	}

	public void gainXP(double xp) {
		xpEarned += xp;
	}

	public int getEnemiesDefeated() {
		return enemiesDefeated;
	}
	
	public boolean getHasKey() {
		return hasKey;
	}
	
	public boolean getOpenedLock() {
		return openedLock;
	}
	
	public int getTurnsOnLevel() {
		return turnsOnLevel;
	}
	
	public double getXPEarned() {
		return xpEarned;
	}

	public void handleMovementResult() {
		// If potion in this node, consume and remove
		if (loc.checkEntityByID((byte) 1)) {
			loc.removeEntityByID((byte) 1);
			hp = maxHP;
		}

		// If hammer in this node, consume and remove
		if (loc.checkEntityByID((byte) 2)) {
			useHammer();
			loc.removeEntityByID((byte) 2);
		}
		
		//If key in this node, pick it up and spawn lock.
		if (loc.checkEntityByID((byte) 5)) {
			hasKey = true;
			loc.removeEntityByID((byte) 5);
			
//			Lock lock = new Lock(map);
			Node randomNode = map.getRandomNodeInRoom();
			map.placeEntity(new Lock(map), randomNode);
			if (this instanceof trl.entity.player.Thief) {
				for (Entity entity : randomNode.getEntities()) {
					entity.setSeenByPlayer(true);
				}
			}
		}
		
		//If lock in this node, remove it and spawn stair (or goal).
		if (loc.checkEntityByID((byte) 6)) {
			openedLock = true;
			loc.removeEntityByID((byte) 6);
			if (GameplayState.dungeonLevel == 5) {
				Goal goal = new Goal(map);
				if (this instanceof trl.entity.player.Thief) {
					goal.setSeenByPlayer(true);
				}
			}
			else {
				map.getRandomNodeInRoom().setFeature(new StairDown());
			}
		}
		
		//If down stairway on this node, switch level
		if (loc.isStairDown()) {
			GameplayState.getEnemyGroup().getEnemies().clear();
			GameplayState gameState = (GameplayState) Game.getGameStateManager().getGameState(1);
			GameplayState.getEnemyGroup().listEnemies();
			gameState.setDungeonLevel(gameState.getDungeonLevel() + 1);
			gameState.init();
		}	
	
		if (loc.checkEntityByID((byte) 7)) {
			Game.getGameStateManager().addGameState(2, new WinGameState());
			Game.getGameStateManager().setGameState(2);	
		}
	}
	
	public void handlePath() {
		//Ignore all this if path is empty
		if (path != null && path.size() > 0) {
//			System.out.println("Path size=" + path.size() + ", initial path size=" + initialPathSize);
			//Path size == 1 and initial path size == 1 and enemy in next node.
			//Consider this a deliberate attempt to attack enemy.
			if (path.size() == 1 && initialPathSize == 1 && getNextPathNode().checkEntityByID((byte) 0)) {
//				System.out.println("Attacking.");
				attack(GameplayState.getEnemyGroup().getEnemy(getNextPathNode()));
				GameplayState.getEnemyGroup().getEnemy(path.get(0))
						.setDamageTaken(damageDealt);
//				path.remove(0);
				path.clear();
				attacked = true;
			}
			
			//Path size less than initial path size and enemy in next node. This looks like the player ran into
			//the enemy while traveling a longer path. Don't consider this a deliberate attack
			else if (path.size() < initialPathSize && getNextPathNode().checkEntityByID((byte) 0)) {
//				System.out.println("Blocked by enemy/forgetting path.");
				path.clear();
				acted = true;
			}
			
			/*Path size == 1 and initial path size == 1 and closed door in next node. 
			 * consider this a deliberate attempt to open the door.
			 */
			else if (path.size() == 1 && initialPathSize == 1 && getNextPathNode().getFeature() instanceof trl.map.feature.DoorClosed) {
				openDoor(getNextPathNode());
//				System.out.println("Opening door.");
				path.clear();
				moved = true;
			}
			
			/*Path size less than initial path size and closed door in next node. This looks like the player ran into
			the closed door while traveling a longer path. Don't consider this a deliberate attempt
			to open the door.
			*/
			else if (path.size() >= 1 && path.size() < initialPathSize && getNextPathNode().getFeature() instanceof trl.map.feature.DoorClosed) {
//				System.out.println("Blocked by door/forgetting path.");
				path.clear();
				acted = true;
			}
			
			/* Player close flag set.
			 */			 
			else if (close) {
				if (getNextPathNode().getFeature() instanceof trl.map.feature.DoorOpen) {
					getNextPathNode().makeClosedDoor();
				}
				path.clear();
				close = false;
				acted = true;
			}
			
			// Move to next node
			else {
//				System.out.println("Moving.");
				move(getNextPathNode());
				moved = true;
			}
		}	
	}
	
	public void handleSpecialActions() {
		/*Methods defined in class files for each player class. Actor is upcast 
		 * for method calls. Should stay safe due to player class checks.
		 */
		if (shout) {
			if (timers[0] <= 0) {
				((Barbarian)this).shout();
				acted = true;
			}
			shout = false;
		}
	
		if (blink) {
				if (timers[0] <= 0) {
					((Wizard)this).blink();
					acted = true;
				}
				blink = false;
		}
	
		if (explode) {
			if (timers[1] <= 0) {
				((Wizard)this).explode();
				acted = true;
			}
			explode = false;
		}
		
		if (quicken){			
			if (timers[2] <= 0) {
				((Wizard)this).quicken();
				acted = true;
			}
			quicken = false;
		}
		
		
//		if (!adjacentEnemy()) {
			//If KeyManager says we're supposed to look for targets (getTargets), but haven't yet (!gotTargets)

			if (getTargets && !gotTargets) {
				if (!adjacentEnemy()) {
					targets = ((Ranger)this).getTargets();	//Returns 0-size list if no targets found, not null!
					//targets list size = 0, clear all flags. Should continue turn as normal.
					if (targets.size() == 0) {
						System.out.println("Targets list size = 0. No targets found. Canceling targeting.");
						clearAllTargetingFlags();
					}
					//targets list size != 0, so we found at least one target. Stop looking (getTargets = false) and
					//allow target switching(gotTargets = true)
					else {
						getTargets = false;
						gotTargets = true;
					}
				}
				else {
					clearAllTargetingFlags();
				}
			}
			
			//Should only be possible if targets list size > 0 
			if (!getTargets && gotTargets) {
				//Allow player to cancel targeting
				if (cancel) {
					clearAllTargetingFlags();
				}
				else {
					/*On first pass through, target will be null. If target is null, it's safe for all conditions
					 * to assign target the first element in targets list. This takes care of 1-size target lists,
					 * since they can never be anything but the first element in targets list.
					 */
					if (target == null) {
	//					System.out.println("Target list size = " + targets.size());
						target = targets.get(0);
						target.setTargeted(true);
					}
					/*Target is not null, so we must have passed through at least once. On subsequent
					 * passes, we need to handle nextTarget/previousTarget for enemies lists with size > 1. 
					 * 0-size lists should not exist, and 1-size lists keep their assigned values of 
					 * enemies.get(0)*/
					else {
						if (targets.size() > 1) {
							//Targets list size must be > 1. Consider nextTarget/previousTarget meaningful, but
							//clear them after each pass.
							if (nextTarget) {
								//Changing target, untarget current target
								target.setTargeted(false);
								//If not last target in list, target = current target index + 1
								if ((targets.size() - 1) > targets.indexOf(target)) {
									target = (targets.get(targets.indexOf(target) + 1));
								}
								//Else target wraps around to first target
								else {
									target = targets.get(0);
								}
								//Changed target, target new target
								target.setTargeted(true);
							}
							if (previousTarget) {
								target.setTargeted(false);
								//If not first target, target = current target index - 1
								if (targets.indexOf(target) > 0) {
									target = targets.get(targets.indexOf(target) - 1);
								}
								//Else target wraps around to last in list
								else {
									target = targets.get(targets.size() - 1);
								}
								//Changed target, target new target
								target.setTargeted(true);
							}
						}
					}
				}
				previousTarget = false;
				nextTarget = false;	
			}
	
	
			if (fireArrow) {
				((Ranger)this).fireArrow(target);			
				clearAllTargetingFlags();
				acted = true;
			}
	}
	
	public void incrementEnemiesDefeated(int enemiesDefeated) {
		this.enemiesDefeated += enemiesDefeated;
	}
	
	public void levelUp() {
		double percentHealth = (double)hp / (double)maxHP;
//		System.out.println("Percent health = " + percentHealth);
		level++;
		double exponent = 1 / level; 
		maxHP = maxHP + (int)Math.pow(level, 1.0 + exponent);
		hp = (int)(percentHealth * (double)maxHP);
	}
	
	public void printEnemiesList() {
		System.out.println("=== Enemies ===");
		System.out.println("Player at " + getX() + "," + getY());
		for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
			System.out.println(enemy.toString().substring(enemy.toString().lastIndexOf('.')) + " at " + enemy.getX() + "," + enemy.getY() + " v2p=" + enemy.getVisibleToPlayer() + " ,aop=" + enemy.awareOfPlayer() + ", sbp=" + enemy.seenByPlayer());
		}
	}
	
	public void refresh() {
		hasKey = false;
		openedLock = false;
		turnsOnLevel = 0;
	}
	
	public void rollItems() {
		// Roll for item creation
		Random r = new Random();
		double roll = r.nextDouble();
		if (roll >= .995) {
			if (!map.hammerOnMap()) {
				Hammer hammer = new Hammer(map);
				if (GameplayState.getPlayer() instanceof trl.entity.player.Thief) {
					hammer.setSeenByPlayer(true);
				}
			}
		} 
		else if (roll >= .9) {
			if (!map.potionOnMap()) {
				Potion potion = new Potion(map);
				if (GameplayState.getPlayer() instanceof trl.entity.player.Thief) {
					potion.setSeenByPlayer(true);
				}
			}
		}
	}
	
	public Node setDirection() {
		Node nextNode = null;
		if (up && getY() < Game.ROWS - 1) {
			up = false;
			nextNode = map.getNode(getX(), getY() + 1);
		}
		if (dn && getY() >= 1) {
			dn = false;
			nextNode = map.getNode(getX(), getY() - 1);
		}
		if (lt && getX() >= 1) {
			lt = false;
			nextNode = map.getNode(getX() - 1, getY());
		}
		if (rt && getX() < Game.COLUMNS - 1) {
			rt = false;
			nextNode = map.getNode(getX() + 1, getY());
		}
		if (ur && getY() < Game.ROWS - 1 && getX() < Game.COLUMNS - 1) {
			ur = false;
			nextNode = map.getNode(getX() + 1, getY() + 1);
		}

		if (dr && getY() >= 1 && getX() < Game.COLUMNS - 1) {
			dr = false;
			nextNode = map.getNode(getX() + 1, getY() - 1);
		}

		if (dl && getY() >= 1 && getX() >= 1) {
			dl = false;
			nextNode = map.getNode(getX() - 1, getY() - 1);
		}

		if (ul && getY() < Game.ROWS - 1 && getX() >= 1) {
			ul = false;
			nextNode = map.getNode(getX() - 1, getY() + 1);
		}
		
		return nextNode;
	}
	
	public void setNewEnemies(boolean newEnemies) {
		this.newEnemies = newEnemies;
	}
	
	public void setTurnsOnLevel(int turns) {
		turnsOnLevel = turns;
	}
	
	public void tick() {
		Node nextNode; // = null;	//prospective node for movement/action
		Enemy enemy;			//prospective enemy in nextNode
		
		//Clear activity flags
		clearFlags();

		//Set stance to normal
		if (Game.tickTimer == 0) {
			setStance(true, false, false, false);
		}
		
		//Alert enemies in visibleNodes to player's presence
		alertEnemies();
	
		//Check for keypresses
		if (myTurn && Game.tickTimer == 0) {			
			/*Get direction corresponding to keypress. Will be null until
			 * player presses a key.
			 */
			nextNode = setDirection();
			
			/*If not null, add node to path*/
			if (nextNode != null && !(nextNode.isWall())) {
				path.clear();
				path.add(nextNode);
				initialPathSize = 1;

			}
			
			/*Check the path and handle action depending on state and
			contents of next path node. Could be movement, attacking an
			enemy, opening or closing a door*/
			handlePath();
			
			/*Handle non-movement related actions*/
			handleSpecialActions();
			
			/*End turn without acting. This is equivalent to adding
			 * the current node to the path.
			 */
			if (wait) {
				wait = false;
				acted = true;
			}
		}

		if (moved) {
			handleMovementResult();
		}
		
		if (acted || moved || attacked) {
			Game.turnCounter++;
			turnsOnLevel++;
			if (!attacked) {
				/*If not attacked, set all enemy damageTaken = 0. Prevents enemies 
				 * from displaying damage indicator on subsequent rounds after
				 * damage was previously done.
				 */
				for (Enemy enemies : GameplayState.getEnemyGroup().getEnemies()) {
					enemies.setDamageTaken(0);
				}	
			}
			//If I attacked this round
			else {
				//Set my stance to attacking
				setStance(false, true, false, false);
			}
			
			//Health regen for barbarian. Starting 3 turns after last combat, increment
			//health by one every other turn.
			if (!attacked && getDamageTaken() == 0) {
				turnsSinceCombat++;
				if (this instanceof trl.entity.player.Barbarian) {
					if (turnsSinceCombat >=3 && turnsSinceCombat % 2 == 0) {
						if (hp < maxHP) {
							hp += 1;
						}
					}
				}
			}
			else {
				turnsSinceCombat = 0;
			}
			
			clearAllTargetingFlags();
			rollItems();
			decrementTimers();
			map.updateDisplayedNodes();
//			printEnemiesList();
		}
		
		if (xpEarned >= Math.pow(level + 1, 2)) {
			levelUp();
		}

		// Kill player
		if (!this.isAlive()) {
			setImage(Game.getImageManager().corpse);
		}
	}
	
	public void useHammer() {
		//Set HP of all enemies in visibleNodes = 0
//		int startX = map.getDisplayedNodesMinX();
		int startX = Map.displayedNodesMinX;
//		int startY = map.getDisplayedNodesMinY();
		int startY = Map.displayedNodesMinY;
		for (int x = startX; x < startX + Game.W_COLS; x++) {
			for (int y = startY; y < startY + Game.W_ROWS; y++) {
				if (map.getNode(x,y) != null && map.getNode(x, y).checkEntityByID((byte) 0)) {
					GameplayState.getEnemyGroup().getEnemy(map.getNode(x, y)).setHP(0);
				}
			}
		}
		//Heal player up to 50% health
		if (hp < maxHP / 2) {
			hp = maxHP / 2;
		}
	}
}
