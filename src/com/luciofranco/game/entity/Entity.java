package com.luciofranco.game.entity;

import java.util.Random;

import com.luciofranco.game.graphics.Screen;
import com.luciofranco.game.level.Level;

public class Entity {
	
	public int x, y;
	
	private boolean removed = false;
	
	protected Level level;
	protected final Random random = new Random();
	
	public void update() {	
	}
	
	public void render(Screen screen) {
	}
	
	public void remove() {
		//Remove from level
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
}
