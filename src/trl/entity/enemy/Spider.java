package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Spider extends Enemy {
    public Spider(Map map) {
        super(map);
        init();
    }
    
    public void init() {
    	maxHP = 7;
    	atk = 5;
    	image = Game.getImageManager().spider;
        hp = maxHP;
        level = 2;
        xpReward = 2;
    }
}
