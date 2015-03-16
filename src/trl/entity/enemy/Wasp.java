package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Wasp extends Enemy {
	public Wasp(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 10;
    	atk = 5;
    	image = Game.getImageManager().wasp;
        hp = maxHP;
        xpReward = 5;
        level = 5;
    }
}
