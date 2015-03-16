package trl.entity.player;

import trl.main.Game;
import trl.map.Map;

public class Thief extends Player{
	 
    public Thief(Map map) {
        super(map);
        this.maxHP = 40;
        this.atk = 7;
        this.image = Game.getImageManager().thief;
        init();
    }
    
    public void init() {
        this.hp = maxHP;
        this.myTurn = true;
        map.revealAll();
        timers = new int[0];
    }
}
