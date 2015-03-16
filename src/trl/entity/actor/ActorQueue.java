package trl.entity.actor;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import trl.entity.enemy.Enemy;
import trl.gamestate.GameplayState;
import trl.main.Game;

public class ActorQueue {
	private List<Actor> queue;
	
	public ActorQueue() {		
		init();
	}
	
	public void init() {
		queue = new ArrayList<Actor>();
	}
	
	public void addActor(Actor actor) {
		//Add actor at beginning of queue
		queue.add(0, actor);
	}
	
	public void render(Graphics g) {
		for (Actor actor : queue) {
			actor.render(g);
		}
	}
	
	public void tick() {
		Actor actor;
		/*Start tick loop*/
		for (Iterator<Actor> itQueue = queue.iterator(); itQueue.hasNext();) {
			actor = itQueue.next();
//			if (actor instanceof trl.entity.enemy.Enemy && GameplayState.tickEnemies) {
//				actor.tick();
//			}
//			else if (actor instanceof trl.entity.enemy.Enemy) {
//				actor.tick();
//			}
			actor.tick();
			
			/*If actor was enemy*/
			if (actor instanceof trl.entity.enemy.Enemy) {
				if (!actor.isAlive() && Game.tickTimer > 0) {
					GameplayState.getPlayer().incrementEnemiesDefeated(1);
					//Calc xp gain for dead enemy
					double levelDiff = GameplayState.getPlayer().getLevel() - actor.getLevel();
					if (levelDiff <= 0.0d) {
						levelDiff = 1.0d;
					}
					double percentXP = (1.0d / levelDiff) * (double)actor.getLevel();
					if (percentXP > 1.0) {
						percentXP = 1.0;
					}
					double xp = percentXP * (double)actor.getLevel();
					GameplayState.getPlayer().gainXP(xp);
					itQueue.remove();
					GameplayState.getEnemyGroup().removeEnemy(actor);
					
					/*This needs to stay here*/
					//If it's a dead enemy's turn, pass turn to next actor 
					if (actor.getTurn()) {
						actor.endTurn(queue.get(queue.indexOf(actor) + 1));
						
					}
				}
			}
	
			/*If actor acted on its turn, determine next actor */			
			if (actor.getTurn()) {
				if (actor.getActed()) {
//					GameplayState.getMap().updateDisplayedNodes();
					GameplayState.getMap().updateVisibleToPlayer();
					GameplayState.getMap().updateImageMap();
					//If there's another actor in queue, hand turn to that actor
					if (itQueue.hasNext()) {
						//Get next actor by index (current + 1) instead of moving iterator
//						System.out.println(actor.toString() + " passing turn to " + queue.get(queue.indexOf(actor) + 1));
						actor.endTurn(queue.get(queue.indexOf(actor) + 1));
					}
					//Hand turn to first actor in queue
					else {
//						System.out.println(actor.toString() + " passing turn to " + queue.get(0).toString());
						actor.endTurn(queue.get(0));
					}
				}
				else {
					/*If actor did not act. Not currently implemented*/
				}
			}
			

		}
		/*End tick loop*/
		
		GameplayState.getEnemyGroup().spawnEnemies();
	}
	
	public Actor getActor(int index) {
		return queue.get(index);
	}
	
	public void flush() {
		for (Actor actor : queue) {
			actor = null;
		}
	}
}
