package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.ResourceManager;
import net.toper.Texture;

public class RenderEngine implements Runnable {

	private Frame f;

	public static Texture textureSheet = ResourceManager.loadTexture("/tiles.png", true);
	public static Texture grass = ResourceManager.getTexFromSheet(textureSheet, 0, 0, 16, 16);
	public static Texture water = ResourceManager.getTexFromSheet(textureSheet, 17, 0, 16, 16);

	Generate level = new Generate(10);

	public RenderEngine(Frame f) {
		this.f = f;
	}

	public Frame getFrame() {
		return f;
	}

	public void run() {
		grass.setScale(Generate.TILE_SIZE / grass.getWidth());
		water.setScale(Generate.TILE_SIZE / water.getWidth());
		while (true) {
			f.setTitle("FPS: " + f.getFPS());
			render();
		}
	}

	float i;

	public void render() {
		f.clear(0xfff);
		level.render(f);
		f.endFrame();
	}

}
