package trl.entity.item;

import trl.main.Game;
import trl.map.Map;

public class Lock extends Item {
    public Lock(Map map) {
        super(map);
        this.image = Game.getImageManager().lock;
    } 
}
