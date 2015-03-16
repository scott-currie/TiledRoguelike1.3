package trl.entity.player;

import trl.entity.enemy.Enemy;
import trl.gamestate.GameplayState;
import trl.main.Game;
import trl.map.Map;

public class Barbarian extends Player{   
	
    public Barbarian(Map map) {
        super(map);
        this.maxHP = 50;
        this.atk = 10;
        this.image = Game.getImageManager().barbarian;
        init();
    }

    public void init() {
        this.hp = maxHP;
        this.myTurn = true;
        timers = new int[1];
    } 
    
	public void shout() {
		// Cause all enemies to set path to node occupied by player
		for (Enemy enemy : GameplayState.getEnemyGroup().getEnemies()) {
			enemy.setPathTo(this.loc);
			timers[0] = 31;
		}
	}

}
