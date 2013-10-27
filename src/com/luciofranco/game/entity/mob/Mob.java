package com.luciofranco.game.entity.mob;

import com.luciofranco.game.entity.Entity;
import com.luciofranco.game.graphics.Sprite;

public abstract class Mob extends Entity{
	
	protected Sprite sprite;
	protected int dir = 0;
	protected boolean moving = false;
	
	public void move() {
	}
	
	public void update() {
	}
	
	private boolean collision() {
		return false;
	}
}
