package trl.entity.enemy;

import trl.entity.actor.Actor;
import trl.gamestate.GameplayState;
import trl.main.Game;
import trl.map.Map;

public abstract class Enemy extends Actor{   
    protected boolean awareOfPlayer = false;
    protected int xpReward;
    protected boolean targeted;
	
	public Enemy(Map map) {
        super(map);
    }
    
    public void init() {
        hp = maxHP;
        myTurn = false;
        awareOfPlayer = false;
    }
    
    public void tick() {

        clearFlags();	//Set acted, attacked, moved to false.
        
        //Set stance to normal
        if (Game.tickTimer == 0) {
        	setStance(true, false, false, false);
        }

        if (myTurn && Game.tickTimer == 0) {

            if (!awareOfPlayer) {
			  	//If all path nodes are consumed, get a new path.
            	//If enemy has been on the same node for 3 turns, get new path.
			  	if (path.size() == 0 || turnsOnNode >= 3) {
//			  		setPathTo(map.getRandomNode());
			  		setPathToConnectedRoom();
			  	}
		  	}
            
            //If enemy is aware of player, attack or update path to player
            else {
			  	//If adjacent to player, attack.
			  	if (this.loc.adjacent(GameplayState.getPlayer().getLoc())) {
				  	attack(GameplayState.getPlayer());
				  	GameplayState.getPlayer().setDamageTaken(damageDealt);                    
				  	attacked = true;
			  	}
			  	else {
				  	//Set path to player's location
				  	setPathTo(GameplayState.getPlayer().getLoc());				  				  		
			  	}   		  
		  	}
                       
            /*If enemy didn't attack, move to next node in path. Compare loc after
             * movement with current node. If they're equal, we didn't change location,
             * so we should increment turnsOnNode. setDamageTaken(0) is a clumsy
             * fix to prevent player from indicating damage on rounds after he wasn't
             * in combat.
             */
		  	if (!attacked) {
		  		previousNode = loc;
		  		move(getNextPathNode());
		  		moved = true;
		  		if (loc.equals(previousNode)) {
		  			turnsOnNode++;
		  		}
		  		else {
		  			turnsOnNode = 0;
		  		}
		  		GameplayState.getPlayer().setDamageTaken(0);
		  	}
        }     
	}
    
    public int getXP() {
    	return xpReward;
    }
    
    public void setAwareOfPlayer(boolean awareOfPlayer) {
    	this.awareOfPlayer = awareOfPlayer;
    }
    
    public boolean awareOfPlayer() {
    	return awareOfPlayer;
    }
    
    public boolean getTargeted() {
    	return targeted;
    }
    
    public void setTargeted(boolean targeted) {
    	this.targeted = targeted;
    }
}
