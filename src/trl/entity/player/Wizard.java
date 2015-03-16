package trl.entity.player;

import java.util.List;

import trl.entity.enemy.Enemy;
import trl.gamestate.GameplayState;
import trl.main.Game;
import trl.map.Map;
import trl.map.Node;

public class Wizard extends Player{
    public Wizard(Map map) {
        super(map);
        this.maxHP = 30;
        this.atk = 5;
        this.image = Game.getImageManager().wizard;
        init();
    }
    
    public void init() {
        this.hp = maxHP;
        this.myTurn = true;
        //timers[0] = blink, timers[1] = explode, timers[2] = quicken
        timers = new int[3];
        for (int i : timers) {
        	i = 0;
        }
    }
    
    public void quicken() {
    	this.hp = (int)((double)hp * .75);
    	timers[2] = 3;
    	timers[1] = 0;
    }
    
	public void explode() {
		List<Node> blastArea = map.getAoENodes(loc, 2);
		int blastDamage = (int)(Math.ceil(6.0 + (double)(GameplayState.getPlayer().getLevel() / 1.1)));
		for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
			{
				if (blastArea.contains(enemy.getLoc())) {
					enemy.setHP(enemy.getHP() - blastDamage);
					enemy.setStance(false, false, true, false);
				}
			}
		}
		timers[1] = 11;
	}
	
	public void blink() {
		//Move to random node, make enemies forget player and player forget enemies
		move(map.getRandomNode());
		for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
			enemy.setAwareOfPlayer(false);
			enemy.setSeenByPlayer(false);
		}
		timers[0] = 31;
	}
}
