package com.luciofranco.game.level.tile;

import com.luciofranco.game.graphics.Screen;
import com.luciofranco.game.graphics.Sprite;

public class VoidTile extends Tile {

	public VoidTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}

}
