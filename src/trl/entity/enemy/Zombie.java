package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Zombie extends Enemy {
	public Zombie(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 26;
    	atk = 5;
    	image = Game.getImageManager().zombie;
        hp = maxHP;
        xpReward = 13;
        level = 13;
    }
}
