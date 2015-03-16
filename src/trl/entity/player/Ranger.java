package trl.entity.player;

import java.util.ArrayList;
import java.util.List;

import trl.entity.Entity;
import trl.entity.enemy.Enemy;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;

public class Ranger extends Player{   
//	private Node target;
	private List<Enemy> targets;
    public Ranger(Map map) {
        super(map);
        this.maxHP = 40;
        this.atk = 7;
        this.image = Game.getImageManager().ranger;
        init();
    }

    public void init() {
        this.hp = maxHP;
        this.myTurn = true;
        timers = new int[1];
        targets = new ArrayList<Enemy>();
    } 
    
    public List<Enemy> getTargets() {
    	/*For each node in visibleNodes, look through entities list. If an enemy is found,
    	 * add to enemies list, then return the list.
    	 */
    	targets.clear();
    	List<Enemy> enemies = new ArrayList<Enemy>();
    	List<Entity> entities = null;
    	for (Node node : map.getVisibleToPlayer()) {
    		if (node.getEntities() != null) {
    			entities = new ArrayList<Entity>(node.getEntities());
    			for (Entity entity : entities) {
    				if (entity instanceof trl.entity.enemy.Enemy) {
    					enemies.add((Enemy)entity);
    				}
    			}
    		}
    	}
    	return enemies;
     }
    
    public void fireArrow(Enemy target) {
    	int damage = 4;
    	if (getLevel() > 4) {
    		damage = getLevel();
    	}
    	if (damage > 10) {
    		damage = 10;
    	}
    	target.setHP(target.getHP() - damage);
    	damageDealt = damage;
    	target.setDamageTaken(damage);
    	setStance(false, false, false, true);
    	target.setStance(false, false, true, false);
    	fireArrow = false;
//    	attacked = true;
    }
}

