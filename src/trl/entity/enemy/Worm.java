package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Worm extends Enemy {
	public Worm(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 5;
    	atk = 5;
    	image = Game.getImageManager().worm;
        hp = maxHP;
        xpReward = 1;
        level = 1;
    }
}
