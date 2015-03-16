package trl.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import trl.entity.player.Ranger;
import trl.gamestate.GameStateManager;
import trl.gamestate.GameplayState;
import trl.gamestate.LoseGameState;
import trl.gamestate.MenuState;

public class KeyManager implements KeyListener{
	private GameStateManager gsm;
	
	public KeyManager(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public void keyPressed(KeyEvent e) {
       if (gsm.getGameState() == 0) {
	       if (e.getKeyCode() == KeyEvent.VK_UP) {
	    	  MenuState.up = true; 
	       }
	       if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	   MenuState.down = true;
	       }
	       if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    	   MenuState.enter = false;
	       }
       }
       if (gsm.getGameState() == 2) {
    	   if (e.getKeyCode() == KeyEvent.VK_UP) {
    		   LoseGameState.up = true;
    	   }
    	   if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    		   LoseGameState.down = true;
    	   }
    	   if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    		   LoseGameState.enter = false;
    	   }
       }
    }    
    
    public void keyReleased(KeyEvent e) {
    	if (gsm.getGameState() == 1 && Game.tickTimer <= 0) {
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
		       GameplayState.getPlayer().up = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
		       GameplayState.getPlayer().dn = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
		       GameplayState.getPlayer().lt = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
		       GameplayState.getPlayer().rt = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
		       GameplayState.getPlayer().ur = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
		       GameplayState.getPlayer().dr = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
		       GameplayState.getPlayer().dl = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
		       GameplayState.getPlayer().ul = true;
		   }      
		   if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
		       GameplayState.getPlayer().wait = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_S) {
			   if (GameplayState.getPlayer() instanceof trl.entity.player.Barbarian) {
				   GameplayState.getPlayer().shout = true; 
			   }
		   }
		   
		   if (e.getKeyCode() == KeyEvent.VK_B){
			   if (GameplayState.getPlayer() instanceof trl.entity.player.Wizard) {
				   GameplayState.getPlayer().blink = true;
			   }
		   }
		   if (e.getKeyCode() == KeyEvent.VK_E){
			   if (GameplayState.getPlayer() instanceof trl.entity.player.Wizard) {
				   GameplayState.getPlayer().explode = true;
			   }
		   }
		   if (e.getKeyCode() == KeyEvent.VK_C) {
			   GameplayState.getPlayer().close = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_Q) {
			   if (GameplayState.getPlayer() instanceof trl.entity.player.Wizard) {
				   GameplayState.getPlayer().quicken = true;
			   }
		   }
		   if (e.getKeyCode() == KeyEvent.VK_F) {
			   if (GameplayState.getPlayer() instanceof trl.entity.player.Ranger) {
				   /*If F pressed when neither getTargets nor targetEnemy are true,
				    * get do getTargets() and enable targetEnemy(). getTargets will become
				    * false.
				    */
				   if (!((Ranger)(GameplayState.getPlayer())).gotTargets) {
				   		GameplayState.getPlayer().getTargets = true;
				   }
				   /*If F pressed when getTargets is false and targetEnemy is true, do
				    * fireArrow, using the currently selected target.
				    */
				   if (((Ranger)(GameplayState.getPlayer())).gotTargets) {
					   GameplayState.getPlayer().gotTargets = false;
					   GameplayState.getPlayer().targetEnemy = false;
					   System.out.println("Setting fireArrow to true.");
					   GameplayState.getPlayer().fireArrow = true;   
				   }
			   }
			   	
		   }
		   if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			   GameplayState.getPlayer().cancel = true;
		   }
		   
		   if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			   GameplayState.getPlayer().previousTarget = true;
		   }
		   if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			   GameplayState.getPlayer().nextTarget = true;
		   }
    	}
    	  	
    	if (gsm.getGameState() == 0) {
    		if (e.getKeyCode() == KeyEvent.VK_UP) {
    			MenuState.up = false;
    		}
    		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    			MenuState.down = false;
    		}
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			MenuState.enter = true;
    		}
    	}
    	if (gsm.getGameState() == 2) {
    		if (e.getKeyCode() == KeyEvent.VK_UP) {
    			LoseGameState.up = false;
    		}
    		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    			LoseGameState.down = false;
    		}
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			LoseGameState.enter = true;
    		}
    	}
    }
    
    public void keyTyped(KeyEvent e) {
        
    }    
}
