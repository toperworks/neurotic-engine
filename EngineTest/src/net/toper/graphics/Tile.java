package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.Texture;

public class Tile {

	protected int x, y, xOff, yOff, width, height;
	protected Texture tex;

	public Tile(int x, int y) {

	}

	public void render(Frame f) {
		Render.render(f, tex, x, y, 0, 0, tex.getWidth(), tex.getHeight(), 0xffff00ff, 0);
	}

}
