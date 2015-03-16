package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Ogre extends Enemy {
	public Ogre(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 28;
    	atk = 5;
    	image = Game.getImageManager().ogre;
        hp = maxHP;
        xpReward = 14;
        level = 14;
    }
}
