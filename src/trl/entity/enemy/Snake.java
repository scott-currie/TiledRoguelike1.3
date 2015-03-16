package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Snake extends Enemy {
    public Snake(Map map) {
        super(map);
        init();
    }
    
    public void init() {
    	maxHP = 5;
    	atk = 5;
    	image = Game.getImageManager().snake;
        hp = maxHP;
        level = 1;
        xpReward = 1;
    }
}
