package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.Texture;

public class Tile {

	protected int x, y, xOff, yOff, width, height;
	protected Texture tex;

	public Tile(int x, int y, Texture tex) {
		this.x = x;
		this.y = y;
		this.tex = tex;
	}

	public void render(Frame f, int xOff, int yOff) {
		Render.render(f, tex, (x * Generate.TILE_SIZE) - xOff, (y * Generate.TILE_SIZE) - yOff, 0, 0, tex.getScaledWidth(),
				tex.getScaledHeight(), 0xffff00ff, 0);
	}

}
