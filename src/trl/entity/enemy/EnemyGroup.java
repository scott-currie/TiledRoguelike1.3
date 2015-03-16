package trl.entity.enemy;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trl.entity.actor.Actor;
import trl.gamestate.GameplayState;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;


public class EnemyGroup {
    private List<Enemy> enemies;
    private Map map;

    public EnemyGroup(Map map) {
        this.map = map;
        init();
    }
    public void init() {
        enemies = new ArrayList<Enemy>();
    }
    
    public void spawnEnemies() {
    	int dungeonLevel = GameplayState.dungeonLevel;
    	int maxEnemies = dungeonLevel + (GameplayState.getPlayer().getTurnsOnLevel() / GameplayState.addEnemyInterval);
    	while (enemies.size() < maxEnemies) {
	    	Random r = new Random();
	    	double roll = r.nextDouble();
	    	Enemy enemy = null;
	    	switch(dungeonLevel) {
		    	case 1: {
			    	if (roll > .9) {
			    		enemy = new Rat(map);
			    	}
			    	else if (roll > .6) {
			    		enemy = new Spider(map);
			    	}
			    	else {
			    		enemy = new Bat(map);
			    	}
			    	break;
		    	}
		    	case 2: {
			    	if (roll > .9) {
			    		enemy = new Scorpion(map);
			    	}
			    	else if (roll > .6) {
			    		enemy = new Wasp(map);
			    	}
			    	else {
			    		enemy = new Ant(map);
			    	}
		    	}
		    	break;	
		    	
		    	case 3: {
			    	if (roll > .9) {
			    		enemy = new Panther(map);
			    	}
			    	else if (roll > .6) {
			    		enemy = new Goblin(map);
			    	}
			    	else {
			    		enemy = new GelatinousCube(map);
			    	}
			    	break;
		    	}
		    	case 4: {
			    	if (roll > .9) {
			    		enemy = new Gremlin(map);
			    	}
			    	else if (roll > .6) {
			    		enemy = new Wyvern(map);
			    	}
			    	else {
			    		enemy = new Wolf(map);
			    	}
			    	break;
		    	}
		    	case 5: {
			    	if (roll > .9) {
			    		enemy = new Gargoyle(map);
			    	}
			    	else if (roll > .6) {
			    		enemy = new Ogre(map);
			    	}
			    	else {
			    		enemy = new Zombie(map);
			    	}
			    	break;
		    	}
	    	}

	    	if (GameplayState.getPlayer() instanceof trl.entity.player.Thief) {
	    		enemy.setSeenByPlayer(true);
	    	}
	    	enemies.add(enemy);
	    	GameplayState.getActorQueue().addActor(enemy);
    	}
    }
    
    public void removeEnemy(Actor enemy) {
    	//Remove from enemy list
    	enemies.remove(enemy);
    	//Remove from map node currently occupied
        map.getNodeWith(enemy).removeEntity(enemy);
    }

    public Enemy getEnemy(Node node) {
        for (Enemy enemy : enemies) {
            if (enemy.getLoc().equals(node)) {
                return enemy;
            }
        }
        return null;
    }
    
    public Enemy getEnemy(int index) {
        return (enemies.get(index));
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    public void render(Graphics g) {
    	for (Enemy enemy : enemies) {
    		enemy.render(g);
    	}
    }
    
    public void flush() {
    	for (Enemy enemy : enemies) {
    		enemy  = null;
    	}
    }
    
    public void listEnemies() {
    	System.out.println("Current enemies:");
    	System.out.println("================");
    	for (Enemy enemy : enemies) {
    		System.out.println(enemy.toString().substring(enemy.toString().lastIndexOf(",") + 1));
    	}
    	System.out.println("================");
    }
}
