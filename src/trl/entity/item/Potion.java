package trl.entity.item;

import trl.main.Game;
import trl.map.Map;

public class Potion extends Item {
    public Potion(Map map) {
        super(map);
        init();
    }
	
    public void init() {
        this.image = Game.getImageManager().potion;
        this.loc = map.placeEntity(this, map.getRandomNodeInRoom());
    }    
}
