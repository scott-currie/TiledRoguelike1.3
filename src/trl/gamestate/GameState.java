package trl.gamestate;

import java.awt.Graphics;

public abstract class GameState {

	public abstract void init();
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
}

