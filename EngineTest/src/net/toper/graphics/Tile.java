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
		Render.render(f, tex, (x * Generate.TILE_SIZE) - xOff, (y * Generate.TILE_SIZE) - yOff,
				-(x * Generate.TILE_SIZE) + xOff - Generate.TILE_SIZE,
				-(y * Generate.TILE_SIZE) + yOff - Generate.TILE_SIZE,
				-(x * Generate.TILE_SIZE) + xOff + f.getScaledWidth() + Generate.TILE_SIZE,
				-(y * Generate.TILE_SIZE) + yOff + f.getScaledHeight() + Generate.TILE_SIZE, 0xffffFFFF, 0);
	}

}
