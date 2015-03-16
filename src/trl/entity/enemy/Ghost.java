package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Ghost extends Enemy {
	public Ghost(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 5;
    	atk = 5;
//    	image = Game.getImageManager().ghost;
        hp = maxHP;
        xpReward = 1;
        level = 1;
    }
}
