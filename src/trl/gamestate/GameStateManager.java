package trl.gamestate;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameStateManager {
	private List<GameState> gameStates;
	public static final int MENU_STATE = 0;
	public static final int GAMEPLAY_STATE = 1;
	private GameState current;
	public GameStateManager() {
		init();
	}
	
	public void init() {		
		gameStates = new ArrayList<GameState>();
		gameStates.add(0, new MenuState());
		current = gameStates.get(0);
	}
	
	public void tick() {
		current.tick();
	}
	
	public void render(Graphics g) {
		current.render(g);
	}
	
	public void setGameState(int state) {
		current = gameStates.get(state);
	}
	
	public int getGameState() {
		return gameStates.indexOf(current);
	}
	
	public GameState getGameState(int index) {
		return gameStates.get(index);
	}
	
	public void addGameState(int index, GameState state) {
		gameStates.add(index, state);
	}
	
	public void removeGameState(int index) {
		int i = 0;
		for (Iterator<GameState> itGS = gameStates.iterator(); itGS.hasNext();) {
			GameState gs = itGS.next();
			if (i == index) {
				itGS.remove();
			}
			i++;
		}
	}
}
