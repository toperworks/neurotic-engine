package net.toper.graphics;

import net.toper.Frame;
import net.toper.ResourceManager;
import net.toper.Texture;

public class RenderEngine implements Runnable {

	private Frame f;

	public static Texture textureSheet = ResourceManager.loadTexture("/tiles.png", true);
	public static Texture grass = ResourceManager.getTexFromSheet(textureSheet, 0, 0, 64, 64);
	public static Texture water = ResourceManager.getTexFromSheet(textureSheet, 64, 0, 64, 64);
	public static Texture nullTex = ResourceManager.getTexFromSheet(textureSheet, 128, 0, 64, 64);

	Generate level = new Generate(1000);

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
