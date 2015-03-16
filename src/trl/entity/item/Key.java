package trl.entity.item;

import trl.main.Game;
import trl.map.Map;

public class Key extends Item {
    public Key(Map map) {
        super(map);
        init();
    }
	
    public void init() {
        this.image = Game.getImageManager().key;
        this.loc = map.placeEntity(this, map.getRandomNodeInRoom());
    }    
}
