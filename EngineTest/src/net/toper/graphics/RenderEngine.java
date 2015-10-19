package net.toper.graphics;

import net.toper.Frame;
import net.toper.Render;
import net.toper.ResourceManager;
import net.toper.Texture;

public class RenderEngine implements Runnable {

	private Frame f;
	public static Texture textureSheet;
	public static Texture grass;

	public RenderEngine(Frame f) {
		textureSheet = ResourceManager.loadTexture("/tiles.png", true);
		grass = ResourceManager.getTexFromSheet(textureSheet, 0, 0, 16, 16);
		this.f = f;
	}

	public Frame getFrame() {
		return f;
	}

	public void run() {
		while (true) {
			f.setTitle("FPS: " + f.getFPS());
			render();
		}
	}

	float i;

	public void render() {
		f.clear(0xfff);
		f.endFrame();
	}

}
